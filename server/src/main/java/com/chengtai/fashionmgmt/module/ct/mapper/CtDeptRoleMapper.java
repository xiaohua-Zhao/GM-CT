package com.chengtai.fashionmgmt.module.ct.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chengtai.fashionmgmt.module.ct.entity.CtDeptRole;
import org.apache.ibatis.annotations.Mapper;

/**
 * 部门角色关系持久层，对应表 t_ct_dept_role。
 */
@Mapper
public interface CtDeptRoleMapper extends BaseMapper<CtDeptRole> {}
