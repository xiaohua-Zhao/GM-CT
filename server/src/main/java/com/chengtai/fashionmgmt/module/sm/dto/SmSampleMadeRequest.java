package com.chengtai.fashionmgmt.module.sm.dto;

import lombok.Data;

/** 样衣制作完成及附件信息请求。 */
@Data
public class SmSampleMadeRequest {
    private String fileName;
    private String fileUrl;
    private Long fileSize;
    private String mimeType;
    private String remark;
}
