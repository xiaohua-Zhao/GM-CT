package com.chengtai.fashionmgmt.module.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chengtai.fashionmgmt.module.sys.entity.SysCodeSequence;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * 编码序列持久层，使用 MySQL 行锁保证并发安全。
 */
@Mapper
public interface SysCodeSequenceMapper extends BaseMapper<SysCodeSequence> {
    /**
     * 确保编码序列记录存在，用于首次生成编码时初始化。
     */
    @Insert("INSERT INTO ct_gm.t_sys_code_sequence(rule_id,period_key,current_value,version,created_at,updated_at,is_del) " +
            "VALUES(#{ruleId},#{periodKey},#{initialValue},0,NOW(),NOW(),0) " +
            "ON DUPLICATE KEY UPDATE sequence_id=sequence_id")
    int ensureSequence(@Param("ruleId") Long ruleId, @Param("periodKey") String periodKey,
                       @Param("initialValue") Long initialValue);

    /**
     * 使用 SELECT FOR UPDATE 锁定并读取当前编码序列记录。
     */
    @Select("SELECT sequence_id AS id,rule_id,period_key,current_value,version,created_at,updated_at " +
            "FROM ct_gm.t_sys_code_sequence WHERE rule_id=#{ruleId} AND period_key=#{periodKey} AND is_del=0 FOR UPDATE")
    SysCodeSequence selectForUpdate(@Param("ruleId") Long ruleId, @Param("periodKey") String periodKey);

    /**
     * 更新编码序列当前值并递增版本号。
     */
    @Update("UPDATE ct_gm.t_sys_code_sequence SET current_value=#{currentValue},version=version+1,updated_at=NOW() " +
            "WHERE sequence_id=#{sequenceId}")
    int updateCurrentValue(@Param("sequenceId") Long sequenceId, @Param("currentValue") Long currentValue);
}
