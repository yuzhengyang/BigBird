package com.yuzhyn.bigbird.app.application.internal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yuzhyn.bigbird.app.application.internal.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
}
