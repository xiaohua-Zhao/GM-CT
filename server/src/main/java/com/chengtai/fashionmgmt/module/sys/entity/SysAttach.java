package com.chengtai.fashionmgmt.module.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.chengtai.fashionmgmt.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 附件管理实体，对应表 t_sys_attach。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ct_gm.t_sys_attach")
public class SysAttach extends BaseEntity {
    @TableId(value = "attach_id", type = IdType.AUTO)
    private Long id;

    private Long deptId; // 归属部门
    private String bizType; // 业务类型
    private Long bizId; // 业务主键
    private String category; // 附件分类
    private String fileName; // 原始文件名
    private String fileExt; // 扩展名
    private Long fileSize = 0L; // 文件大小（字节）
    private String mimeType; // MIME类型
    private String fileUrl; // 存储路径
    private String fileHash; // 文件指纹
    private Integer versionNo = 1; // 版本号
    private Integer isPublic = 0; // 是否对外可见
    private Long uploaderId; // 上传人ID
    private String uploadTime; // 上传时间
    private Integer sortNo = 0; // 排序
    private Integer status = 1; // 状态
    private String remark; // 备注
}
