package com.yuzhyn.bigbird.app.application.internal.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuzhyn.bigbird.app.aarg.R;
import com.yuzhyn.bigbird.app.application.internal.entity.SysFile;
import com.yuzhyn.bigbird.app.application.internal.entity.SysUser;
import com.yuzhyn.bigbird.app.application.internal.mapper.SysFileMapper;
import com.yuzhyn.bigbird.app.common.model.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pers.yuzhyn.azylee.core.datas.collections.ListTool;
import pers.yuzhyn.azylee.core.datas.collections.MapTool;
import pers.yuzhyn.azylee.core.datas.datetimes.DateTimeFormat;
import pers.yuzhyn.azylee.core.datas.datetimes.DateTimeFormatPattern;
import pers.yuzhyn.azylee.core.datas.numbers.IntTool;
import pers.yuzhyn.azylee.core.datas.strings.StringTool;
import pers.yuzhyn.azylee.core.datas.uuids.UUIDTool;
import pers.yuzhyn.azylee.core.ios.dirs.DirTool;
import pers.yuzhyn.azylee.core.ios.files.FileTool;
import pers.yuzhyn.azylee.core.ios.streams.InputStreamTool;

import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.Response;
import java.io.*;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("i/sysfile")
public class SysFileController {

    @Autowired
    SysFileMapper sysFileMapper;

    @GetMapping("fileList")
    public ResponseData fileList() {
        List<SysFile> list = sysFileMapper.selectList(null);
        return ResponseData.okData(list);
    }

    @PostMapping("fileListPage")
    public ResponseData fileListPage(@RequestBody Map<String, Object> params) {
        int current = MapTool.getInt(params, "current", 1);
        int size = MapTool.getInt(params, "size", 1);
//        IPage<SysFile> sysFilePage = sysFileMapper.selectPage(new Page<SysFile>(current, size), new LambdaQueryWrapper<SysFile>().gt(SysFile::getSize, 0));
        IPage<SysFile> sysFilePage = sysFileMapper.selectPage(new Page<SysFile>(current, size), null);
        List<SysFile> list = sysFilePage.getRecords();
        return ResponseData.okData(list);
    }

    @PostMapping("upload")
    public ResponseData upload(@RequestParam("userId") String userId, @RequestParam("expiryTime") LocalDateTime expiryTime, @RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) ResponseData.error("上传失败，请选择文件");

        if (StringTool.ok(userId) && expiryTime != null) {
            String fileId = UUIDTool.get();
            LocalDateTime createTime = LocalDateTime.now();
            String fileName = file.getOriginalFilename();
            long size = file.getSize();
            String yearMonthDir = DateTimeFormat.toStr(createTime, DateTimeFormatPattern.SHORT_YEAR_MONTH);
            String path = DirTool.combine(yearMonthDir, fileId);
            DirTool.create(DirTool.combine(R.Paths.SysFile, yearMonthDir));
            File dest = new File(DirTool.combine(R.Paths.SysFile, path));
            try {
                file.transferTo(dest);

                // 上传文件成功，增加文件数据记录
                SysFile sysFile = new SysFile();
                sysFile.setId(fileId);
                sysFile.setName(fileName);
                sysFile.setExt(FileTool.getExt(fileName));
                sysFile.setSize(size);
                sysFile.setPath(path);
                sysFile.setCreateTime(createTime);
                sysFile.setUserId(userId);
                sysFile.setExpiryTime(expiryTime);
                int flag = sysFileMapper.insert(sysFile);
                if (flag > 0) {
                    return ResponseData.okData("sysFile", sysFile);
                }
            } catch (Exception ex) {
                log.error("上传文件失败");
            }
            ResponseData.error("上传失败");
        }
        return ResponseData.error();
    }

    @PostMapping("uploads")
    public ResponseData uploads(@RequestParam("userId") String userId, @RequestParam("expiryTime") LocalDateTime expiryTime, @RequestParam("file") MultipartFile[] files) {
        if (ListTool.ok(files)) {
            List<SysFile> sysFileList = new ArrayList<>();

            for (MultipartFile file : files) {
                if (!file.isEmpty() && StringTool.ok(userId) && expiryTime != null) {
                    String fileId = UUIDTool.get();
                    LocalDateTime createTime = LocalDateTime.now();
                    String fileName = file.getOriginalFilename();
                    long size = file.getSize();
                    String yearMonthDir = DateTimeFormat.toStr(createTime, DateTimeFormatPattern.SHORT_YEAR_MONTH);
                    String path = DirTool.combine(yearMonthDir, fileId);
                    DirTool.create(DirTool.combine(R.Paths.SysFile, yearMonthDir));
                    File dest = new File(DirTool.combine(R.Paths.SysFile, path));
                    try {
                        file.transferTo(dest);

                        // 上传文件成功，增加文件数据记录
                        SysFile sysFile = new SysFile();
                        sysFile.setId(fileId);
                        sysFile.setName(fileName);
                        sysFile.setExt(FileTool.getExt(fileName));
                        sysFile.setSize(size);
                        sysFile.setPath(path);
                        sysFile.setCreateTime(createTime);
                        sysFile.setUserId(userId);
                        sysFile.setExpiryTime(expiryTime);
                        int flag = sysFileMapper.insert(sysFile);
                        if (flag > 0) {
                            sysFileList.add(sysFile);
                        }
                    } catch (Exception ex) {
                        log.error("上传文件失败");
                    }
                }
            }
            return ResponseData.okData(sysFileList);
        }
        return ResponseData.error();
    }

    @GetMapping("download22")
    @ResponseBody
    public ResponseEntity<InputStreamResource> download22(String id) throws Exception {
        SysFile sysFile = sysFileMapper.selectById(id);
        if (sysFile.getExpiryTime() != null && LocalDateTime.now().isBefore(sysFile.getExpiryTime())) {

            String pathName = DirTool.combine(R.Paths.SysFile, sysFile.getPath());
            FileSystemResource file = new FileSystemResource(pathName);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", sysFile.getName()));
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentLength(file.contentLength())
                    .contentType(MediaType.parseMediaType("application/octet-stream"))
                    .body(new InputStreamResource(file.getInputStream()));
        } else {
            throw new Exception();
        }
    }

    @GetMapping("download")
    @ResponseBody
    public void download(String id, HttpServletResponse response) throws UnsupportedEncodingException {
        SysFile sysFile = sysFileMapper.selectById(id);
        if (sysFile != null && sysFile.getExpiryTime() != null && LocalDateTime.now().isBefore(sysFile.getExpiryTime())) {
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
        }
    }
}