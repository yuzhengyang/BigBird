package com.yuzhyn.bigbird.app.application.internal.controller;

import ch.qos.logback.core.util.SystemInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuzhyn.bigbird.app.aarg.R;
import com.yuzhyn.bigbird.app.application.internal.entity.SysFile;
import com.yuzhyn.bigbird.app.application.internal.entity.SysFileCursor;
import com.yuzhyn.bigbird.app.application.internal.entity.SysUserFileConf;
import com.yuzhyn.bigbird.app.application.internal.mapper.SysFileBucketMapper;
import com.yuzhyn.bigbird.app.application.internal.mapper.SysFileCursorMapper;
import com.yuzhyn.bigbird.app.application.internal.mapper.SysFileMapper;
import com.yuzhyn.bigbird.app.application.internal.mapper.SysUserFileConfMapper;
import com.yuzhyn.bigbird.app.application.internal.service.SysFileService;
import com.yuzhyn.bigbird.app.common.model.ResponseData;
import com.yuzhyn.bigbird.app.utils.ClientIPTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pers.yuzhyn.azylee.core.datas.collections.ListTool;
import pers.yuzhyn.azylee.core.datas.collections.MapTool;
import pers.yuzhyn.azylee.core.datas.datetimes.LocalDateTimeTool;
import pers.yuzhyn.azylee.core.datas.ids.UUIDTool;
import pers.yuzhyn.azylee.core.datas.strings.StringTool;
import pers.yuzhyn.azylee.core.ios.dirs.DirTool;
import pers.yuzhyn.azylee.core.ios.files.FileTool;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuple3;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping({"i/sysfile", "i/f"})
public class SysFileController {

    @Autowired
    SysFileService sysFileService;

    @Autowired
    SysUserFileConfMapper sysUserFileConfMapper;

    @Autowired
    SysFileBucketMapper sysFileBucketMapper;

    @Autowired
    SysFileCursorMapper sysFileCursorMapper;

    @Autowired
    SysFileMapper sysFileMapper;

    @GetMapping({"fileList", "ls"})
    public ResponseData fileList() {
        List<SysFile> list = sysFileMapper.selectList(null);
        return ResponseData.okData(list);
    }

    @PostMapping({"fileListPage", "lsp"})
    public ResponseData fileListPage(@RequestBody Map<String, Object> params) {
        int current = MapTool.getInt(params, "current", 1);
        int size = MapTool.getInt(params, "size", 1);
        IPage<SysFile> sysFilePage = sysFileMapper.selectPage(new Page<SysFile>(current, size), null);
        List<SysFile> list = sysFilePage.getRecords();
        return ResponseData.okData(list);
    }

    /**
     * 上传文件
     *
     * @param userId
     * @param expiryTime
     * @param bucketName
     * @param files
     * @return
     */
    @PostMapping({"upload", "up"})
    public ResponseData upload(@RequestParam("userId") String userId, @RequestParam(value = "expiryTime", required = false) LocalDateTime expiryTime, @RequestParam(value = "bucketName", required = false) String bucketName, @RequestParam("file") MultipartFile[] files) {
        if (ListTool.ok(files)) {
            if (StringTool.ok(userId) && sysFileService.checkSpaceLimit(userId, files[0].getSize())) {
                List<SysFileCursor> sysFileList = new ArrayList<>();
                if (null == bucketName) bucketName = UUIDTool.get();
                if (null == expiryTime) expiryTime = LocalDateTimeTool.max();

                for (MultipartFile file : files) {
                    if (StringTool.ok(bucketName) && expiryTime != null) {
                        SysFile sysFile = sysFileService.preCreateSysFile(file, userId);
                        if (sysFile != null) {
                            Tuple3<Boolean, Boolean, SysFile> saveToDisk = sysFileService.saveToDisk(file, sysFile);
                            SysFile existFile = null;
                            if (saveToDisk.getT2()) existFile = saveToDisk.getT3();
                            if (saveToDisk.getT1()) {
                                SysFileCursor cursor = sysFileService.saveDb(sysFile, existFile, bucketName, expiryTime);
                                if (cursor != null) sysFileList.add(cursor);
                            }
                        }
                    }
                }
                return ResponseData.okData(sysFileList);
            } else {
                return ResponseData.error("用户无权限或配额受限");
            }
        }
        return ResponseData.error("请选择文件");
    }

//    @GetMapping("download22")
//    @ResponseBody
//    public ResponseEntity<InputStreamResource> download22(String id) throws Exception {
//        SysFile sysFile = sysFileMapper.selectById(id);
//        if (sysFile.getExpiryTime() != null && LocalDateTime.now().isBefore(sysFile.getExpiryTime())) {
//
//            String pathName = DirTool.combine(R.Paths.SysFile, sysFile.getPath());
//            FileSystemResource file = new FileSystemResource(pathName);
//            HttpHeaders headers = new HttpHeaders();
//            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
//            headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", sysFile.getName()));
//            headers.add("Pragma", "no-cache");
//            headers.add("Expires", "0");
//
//            return ResponseEntity
//                    .ok()
//                    .headers(headers)
//                    .contentLength(file.contentLength())
//                    .contentType(MediaType.parseMediaType("application/octet-stream"))
//                    .body(new InputStreamResource(file.getInputStream()));
//        } else {
//            throw new Exception();
//        }
//    }

    /**
     * http://127.0.0.1:24001/i/sysfile/download/sa/apple/sugar_nacos.sql
     * http://127.0.0.1:24001/i/sysfile/download/sa/apple/禅与摩托车维修艺术.pdf
     *
     * @param userPrefix
     * @param bucketName
     * @param fileName
     * @param response
     */
    @GetMapping({"download/{userPrefix}/{bucketName}/{fileName}", "dl/{userPrefix}/{bucketName}/{fileName}"})
    @ResponseBody
    public void download(@PathVariable String userPrefix, @PathVariable String bucketName, @PathVariable String fileName, HttpServletRequest request, HttpServletResponse response) {
        Tuple2<SysFileCursor, SysFile> fileInfo = sysFileService.getDownloadFile(userPrefix, bucketName, fileName);
        this.download(fileInfo, request, response);
    }

    /**
     * http://127.0.0.1:24001/i/sysfile/download/b3f746673926444f9c4cd071e5befdf1
     *
     * @param cursorId
     * @param response
     */
    @GetMapping({"download/{cursorId}", "dl/{cursorId}"})
    @ResponseBody
    public void download(@PathVariable String cursorId, HttpServletRequest request, HttpServletResponse response) {
        Tuple2<SysFileCursor, SysFile> fileInfo = sysFileService.getDownloadFile(cursorId);
        this.download(fileInfo, request, response);
    }

    public void download(Tuple2<SysFileCursor, SysFile> fileInfo, HttpServletRequest request, HttpServletResponse response) {
        SysFileCursor sysFileCursor = fileInfo.getT1();
        SysFile sysFile = fileInfo.getT2();
        if (sysFileCursor != null && sysFile != null) {
            try {
                sysFileService.saveDownloadLog(sysFileCursor, sysFile, ClientIPTool.getIp(request));

                String pathName = DirTool.combine(R.Paths.SysFile, sysFile.getPath());
                if (FileTool.isExist(pathName)) {
                    File file = new File(pathName);
                    // 配置文件下载
                    response.setHeader("content-type", "application/octet-stream");
                    response.setContentType("application/octet-stream");
                    // 下载文件能正常显示中文
                    response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(sysFile.getName(), "UTF-8"));
                    response.setContentLengthLong(sysFile.getSize());
                    // 实现文件下载
                    byte[] buffer = new byte[1024];
                    try (FileInputStream fis = new FileInputStream(file); BufferedInputStream bis = new BufferedInputStream(fis)) {
                        OutputStream os = response.getOutputStream();
                        int i = bis.read(buffer);
                        while (i != -1) {
                            os.write(buffer, 0, i);
                            i = bis.read(buffer);
                        }
                        log.info("Download  successfully!");
                    } catch (IOException e) {
                        log.error("Download  failed!");
                    }
                }
            } catch (Exception ex) {
            }
        }
    }
}
