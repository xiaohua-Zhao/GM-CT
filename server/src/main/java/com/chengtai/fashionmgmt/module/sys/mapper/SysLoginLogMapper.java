package com.chengtai.fashionmgmt.module.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chengtai.fashionmgmt.module.sys.entity.SysLoginLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 登录日志持久层。
 */
@Mapper
public interface SysLoginLogMapper extends BaseMapper<SysLoginLog> {}
