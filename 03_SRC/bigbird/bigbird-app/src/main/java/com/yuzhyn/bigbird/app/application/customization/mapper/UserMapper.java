package com.yuzhyn.bigbird.app.application.customization.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yuzhyn.bigbird.app.application.customization.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
