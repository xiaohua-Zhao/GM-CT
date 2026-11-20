package com.chengtai.fashionmgmt.module.ct.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chengtai.fashionmgmt.module.ct.entity.CtStaffDept;
import org.apache.ibatis.annotations.Mapper;

/**
 * 人员部门关系持久层，对应表 t_ct_staff_dept。
 */
@Mapper
public interface CtStaffDeptMapper extends BaseMapper<CtStaffDept> {}
