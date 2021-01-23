package com.yuzhyn.bigbird.app.application.internal.service;

import com.sun.org.apache.xpath.internal.operations.Bool;
import com.yuzhyn.bigbird.app.aarg.R;
import com.yuzhyn.bigbird.app.application.internal.entity.SysFile;
import com.yuzhyn.bigbird.app.common.model.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.javassist.runtime.Desc;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pers.yuzhyn.azylee.core.datas.datetimes.DateTimeFormat;
import pers.yuzhyn.azylee.core.datas.datetimes.DateTimeFormatPattern;
import pers.yuzhyn.azylee.core.datas.strings.StringTool;
import pers.yuzhyn.azylee.core.datas.uuids.UUIDTool;
import pers.yuzhyn.azylee.core.ios.dirs.DirTool;
import pers.yuzhyn.azylee.core.ios.files.FileCharCodeTool;
import pers.yuzhyn.azylee.core.ios.files.FileTool;
import reactor.core.publisher.Flux;
import reactor.util.function.*;

import java.io.File;
import java.time.LocalDateTime;

@Slf4j
@Service
public class SysFileService {


    /**
     * 预创建文件实体
     * @param file
     * @return
     */
    public SysFile preCreateSysFile(MultipartFile file){

        return null;
    }

    /**
     * 保存文件到磁盘
     * @param file
     * @param targetPathName
     * @return
     */
    public Tuple3<Boolean, String, String> saveToDisk(MultipartFile file, String targetPathName) {
        if (!file.isEmpty()) {
            File dest = new File(targetPathName);
            try {
                file.transferTo(dest);
                String md5 = FileCharCodeTool.md5(dest);
                String sha1 = FileCharCodeTool.sha1(dest);
                return Tuples.of(true, md5, sha1);
            } catch (Exception ex) {
                log.error("存储失败");
            }
        }
        return Tuples.of(false, null, null);
    }
}
