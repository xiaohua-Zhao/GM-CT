package com.chengtai.fashionmgmt.module.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chengtai.fashionmgmt.module.sys.entity.SysStyleProcess;
import org.apache.ibatis.annotations.Mapper;

/**
 * 款式工序持久层，对应表 t_sys_style_process。
 */
@Mapper
public interface SysStyleProcessMapper extends BaseMapper<SysStyleProcess> {}
