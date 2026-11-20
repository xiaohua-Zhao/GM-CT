package com.chengtai.fashionmgmt.module.ct.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chengtai.fashionmgmt.module.ct.entity.CtStaffRole;
import org.apache.ibatis.annotations.Mapper;

/**
 * 人员角色关系持久层，对应表 t_ct_staff_role。
 */
@Mapper
public interface CtStaffRoleMapper extends BaseMapper<CtStaffRole> {}
