package com.chengtai.fashionmgmt.module.sm.dto;

import lombok.Data;

/** 样衣附件保存请求。 */
@Data
public class SmSampleAttachmentRequest {
    private String fileName;
    private String fileUrl;
    private Long fileSize;
    private String mimeType;
}
