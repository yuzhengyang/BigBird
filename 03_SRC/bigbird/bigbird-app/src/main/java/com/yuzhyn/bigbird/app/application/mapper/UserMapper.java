package com.yuzhyn.bigbird.app.application.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yuzhyn.bigbird.app.application.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
