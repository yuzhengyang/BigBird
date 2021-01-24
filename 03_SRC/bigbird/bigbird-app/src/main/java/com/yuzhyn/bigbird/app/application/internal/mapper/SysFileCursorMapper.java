package com.yuzhyn.bigbird.app.application.internal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yuzhyn.bigbird.app.application.internal.entity.SysFileBucket;
import com.yuzhyn.bigbird.app.application.internal.entity.SysFileCursor;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysFileCursorMapper extends BaseMapper<SysFileCursor> {
}
