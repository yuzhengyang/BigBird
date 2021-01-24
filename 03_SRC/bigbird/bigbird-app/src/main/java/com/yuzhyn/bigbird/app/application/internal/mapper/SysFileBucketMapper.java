package com.yuzhyn.bigbird.app.application.internal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yuzhyn.bigbird.app.application.internal.entity.SysFile;
import com.yuzhyn.bigbird.app.application.internal.entity.SysFileBucket;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysFileBucketMapper extends BaseMapper<SysFileBucket> {
}
