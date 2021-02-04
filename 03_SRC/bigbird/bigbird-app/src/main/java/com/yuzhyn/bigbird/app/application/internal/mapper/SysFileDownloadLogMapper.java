package com.yuzhyn.bigbird.app.application.internal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yuzhyn.bigbird.app.application.internal.entity.SysFile;
import com.yuzhyn.bigbird.app.application.internal.entity.SysFileDownloadLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysFileDownloadLogMapper extends BaseMapper<SysFileDownloadLog> {
}
