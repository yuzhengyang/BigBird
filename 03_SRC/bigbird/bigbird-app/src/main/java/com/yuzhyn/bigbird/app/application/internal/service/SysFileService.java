package com.yuzhyn.bigbird.app.application.internal.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yuzhyn.bigbird.app.aarg.R;
import com.yuzhyn.bigbird.app.application.internal.entity.*;
import com.yuzhyn.bigbird.app.application.internal.mapper.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pers.yuzhyn.azylee.core.datas.collections.ListTool;
import pers.yuzhyn.azylee.core.datas.datetimes.DateTimeFormat;
import pers.yuzhyn.azylee.core.datas.datetimes.DateTimeFormatPattern;
import pers.yuzhyn.azylee.core.datas.ids.UUIDTool;
import pers.yuzhyn.azylee.core.datas.strings.StringTool;
import pers.yuzhyn.azylee.core.ios.dirs.DirTool;
import pers.yuzhyn.azylee.core.ios.files.FileCharCodeTool;
import pers.yuzhyn.azylee.core.ios.files.FileTool;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuple3;
import reactor.util.function.Tuples;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class SysFileService {

    @Autowired
    SysUserFileConfMapper sysUserFileConfMapper;

    @Autowired
    SysFileBucketMapper sysFileBucketMapper;

    @Autowired
    SysFileCursorMapper sysFileCursorMapper;

    @Autowired
    SysFileMapper sysFileMapper;

    @Autowired
    SysFileDownloadLogMapper sysFileDownloadLogMapper;


    public boolean checkSpaceLimit(String userId, long fileSize) {
        SysUserFileConf conf = sysUserFileConfMapper.selectById(userId);
        if (null != conf && conf.getUsedSpace() < conf.getSpaceLimit() &&
                fileSize < (conf.getSpaceLimit() - conf.getUsedSpace())) {
            return true;
        }
        return false;
    }

    //region 文件上传功能函数

    /**
     * 预创建文件实体
     *
     * @param file
     * @return
     */
    public SysFile preCreateSysFile(MultipartFile file, String userId) {
        if (!file.isEmpty() && StringTool.ok(userId)) {
            if (this.checkSpaceLimit(userId, file.getSize())) {
                String fileId = UUIDTool.get();
                LocalDateTime createTime = LocalDateTime.now();
                String fileName = file.getOriginalFilename();
                long size = file.getSize();
                String yearMonthDir = DateTimeFormat.toStr(createTime, DateTimeFormatPattern.SHORT_YEAR_MONTH);
                String path = DirTool.combine(yearMonthDir, fileId);

                SysFile sysFile = new SysFile();
                sysFile.setId(fileId);
                sysFile.setName(fileName);
                sysFile.setExt(FileTool.getExt(fileName));
                sysFile.setSize(size);
                sysFile.setPath(path);
                sysFile.setCreateTime(createTime);
                sysFile.setUserId(userId);
                return sysFile;
            }
        }
        return null;
    }

    /**
     * 保存文件到磁盘
     *
     * @param file
     * @param sysFile
     * @return 操作状态（继续执行标志）、是否已有文件、已有文件信息
     */
    public Tuple3<Boolean, Boolean, SysFile> saveToDisk(MultipartFile file, SysFile sysFile) {
        try {
            // 存储文件到临时文件夹
            File dest = new File(DirTool.combine(R.Paths.SysFileTemp, sysFile.getId()));
            file.transferTo(dest);
            sysFile.setMd5(FileCharCodeTool.md5(dest));
            sysFile.setSha1(FileCharCodeTool.sha1(dest));
            // 查询重复文件（依据文件特征码）
            SysFile record = sysFileMapper.selectOne(new LambdaQueryWrapper<SysFile>()
                    .eq(SysFile::getMd5, sysFile.getMd5())
                    .eq(SysFile::getSha1, sysFile.getSha1())
                    .eq(SysFile::getSize, sysFile.getSize()));
            // 查询到已经有相同文件，重置文件实体类
            if (record != null) {
                FileTool.delete(DirTool.combine(R.Paths.SysFileTemp, sysFile.getId()));
                return Tuples.of(true, true, record);
            }

            // 如果特征码不重复，则将文件移动到标准指定路径中
            String yearMonthDir = DateTimeFormat.toStr(sysFile.getCreateTime(), DateTimeFormatPattern.SHORT_YEAR_MONTH);
            DirTool.create(DirTool.combine(R.Paths.SysFile, yearMonthDir));
            String path = DirTool.combine(yearMonthDir, sysFile.getId());
            String source = DirTool.combine(R.Paths.SysFileTemp, sysFile.getId());
            String target = DirTool.combine(R.Paths.SysFile, path);
            boolean fileMoveFlag = FileTool.move(source, target);
            if (fileMoveFlag) {
                return Tuples.of(true, false, new SysFile());
            }
        } catch (Exception ex) {
            log.error("存储失败");
        }
        return Tuples.of(false, false, new SysFile());
    }

    /**
     * 保存数据实体到数据库中
     *
     * @param sysFile
     * @param existFile
     * @param bucketName
     * @param expiryTime
     * @return
     */
    public SysFileCursor saveDb(SysFile sysFile, SysFile existFile, String bucketName, LocalDateTime expiryTime) {
        // 判断是新增文件还是已经存在文件，如果是新增文件，则保存记录到数据库
        boolean fileSaveFlag = (null != existFile);
        if (null == existFile) {
            int flag = sysFileMapper.insert(sysFile);
            if (flag > 0) fileSaveFlag = true;
        }
        if (fileSaveFlag) {
            // 验证当前用户的Bucket是否存在
            // 如果不存在，则创建新的Bucket
            SysFileBucket bucket = sysFileBucketMapper.selectOne(new LambdaQueryWrapper<SysFileBucket>()
                    .eq(SysFileBucket::getUserId, sysFile.getUserId())
                    .eq(SysFileBucket::getName, bucketName));
            boolean bucketSaveFlag = (null != bucket);
            if (null == bucket) {
                bucket = new SysFileBucket();
                bucket.setId(UUIDTool.get());
                bucket.setName(bucketName);
                bucket.setUserId(sysFile.getUserId());
                int flag = sysFileBucketMapper.insert(bucket);
                if (flag > 0) bucketSaveFlag = true;
            }
            if (bucketSaveFlag) {
                // 创建文件Cursor
                SysFileCursor cursor = new SysFileCursor();
                cursor.setId(UUIDTool.get());
                cursor.setBucketId(bucket.getId());
                if (null != existFile) cursor.setFileId(existFile.getId());
                else cursor.setFileId(sysFile.getId());
                cursor.setFileName(sysFile.getName());
                cursor.setUserId(sysFile.getUserId());
                cursor.setCreateTime(sysFile.getCreateTime());
                cursor.setVersion(System.currentTimeMillis());
                cursor.setExpiryTime(expiryTime);
                int flag = sysFileCursorMapper.insert(cursor);
                if (flag > 0) {
                    // 一切保存成功后，刷新用户文件配额信息
                    SysUserFileConf conf = sysUserFileConfMapper.selectById(sysFile.getUserId());
                    if (conf != null) {
                        conf.setUsedSpace(conf.getUsedSpace() + sysFile.getSize());
                        sysUserFileConfMapper.updateById(conf);
                    }
                    return cursor;
                }
            }
        }
        return null;
    }
    //endregion

    //region 下载文件功能函数
    public Tuple2<SysFileCursor, SysFile> getDownloadFile(String userPrefix, String bucketName, String fileName) {
        if (StringTool.ok(userPrefix, bucketName, fileName)) {
            SysUserFileConf conf = sysUserFileConfMapper.selectOne(new LambdaQueryWrapper<SysUserFileConf>()
                    .eq(SysUserFileConf::getUrlPrefix, userPrefix));

            if (conf != null) {
                SysFileBucket bucket = sysFileBucketMapper.selectOne(new LambdaQueryWrapper<SysFileBucket>()
                        .eq(SysFileBucket::getUserId, conf.getUserId()).eq(SysFileBucket::getName, bucketName));

                if (bucket != null) {
                    List<SysFileCursor> cursorList = sysFileCursorMapper.selectList(new LambdaQueryWrapper<SysFileCursor>()
                            .eq(SysFileCursor::getUserId, conf.getUserId())
                            .eq(SysFileCursor::getBucketId, bucket.getId())
                            .eq(SysFileCursor::getFileName, fileName)
                            .orderByDesc(SysFileCursor::getVersion));
                    SysFileCursor cursor = null;
                    if (ListTool.ok(cursorList)) cursor = cursorList.get(0);
                    if (null != cursor && null != cursor.getExpiryTime() && LocalDateTime.now().isBefore(cursor.getExpiryTime())) {
                        SysFile sysFile = sysFileMapper.selectById(cursor.getFileId());
                        return Tuples.of(cursor, sysFile);
                    }
                }
            }
        }
        return null;
    }

    public Tuple2<SysFileCursor, SysFile> getDownloadFile(String cursorId) {
        if (StringTool.ok(cursorId)) {
            SysFileCursor cursor = sysFileCursorMapper.selectOne(new LambdaQueryWrapper<SysFileCursor>()
                    .eq(SysFileCursor::getId, cursorId));
            if (null != cursor && null != cursor.getExpiryTime() && LocalDateTime.now().isBefore(cursor.getExpiryTime())) {
                SysFile sysFile = sysFileMapper.selectById(cursor.getFileId());
                return Tuples.of(cursor, sysFile);
            }
        }
        return null;
    }

    public void saveDownloadLog(SysFileCursor cursor, SysFile file, String ip) {
        if (cursor != null && file != null) {
            try {
                SysFileDownloadLog log = new SysFileDownloadLog();
                log.setId(UUIDTool.get());
                log.setIp(ip);
                log.setCreateTime(LocalDateTime.now());
                log.setCursorId(cursor.getId());
                log.setFileName(file.getName());
                sysFileDownloadLogMapper.insert(log);
            } catch (Exception ex) {
            }
        }
    }
    //endregion
}
