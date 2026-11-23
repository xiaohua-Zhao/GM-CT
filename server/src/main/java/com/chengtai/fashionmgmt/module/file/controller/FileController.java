package com.chengtai.fashionmgmt.module.file.controller;

import com.chengtai.fashionmgmt.common.Result;
import com.chengtai.fashionmgmt.module.file.dto.AttachmentUploadResponse;
import com.chengtai.fashionmgmt.module.file.service.AttachmentStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {
    private final AttachmentStorageService storageService;

    /**
     * HTTP 接口：接收上传文件并保存到附件目录。
     */
    @PostMapping("/upload")
    public Result<AttachmentUploadResponse> upload(@RequestPart("file") MultipartFile file) {
        log.info("收到附件上传请求: {}", file.getOriginalFilename());
        return Result.success("附件上传成功", storageService.store(file));
    }
}
