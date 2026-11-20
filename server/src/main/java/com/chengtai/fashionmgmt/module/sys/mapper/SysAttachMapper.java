package com.chengtai.fashionmgmt.module.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chengtai.fashionmgmt.module.sys.entity.SysAttach;
import org.apache.ibatis.annotations.Mapper;

/**
 * 附件管理持久层，对应表 t_sys_attach。
 */
@Mapper
public interface SysAttachMapper extends BaseMapper<SysAttach> {}
