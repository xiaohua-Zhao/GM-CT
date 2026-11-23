package com.chengtai.fashionmgmt.module.file.service;

import com.chengtai.fashionmgmt.module.file.config.AttachmentProperties;
import com.chengtai.fashionmgmt.module.file.dto.AttachmentUploadResponse;
import com.chengtai.fashionmgmt.module.file.exception.AttachmentException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AttachmentStorageService {
    private static final DateTimeFormatter DATE_DIR = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    private final AttachmentProperties properties;
    private Path root;

    /**
     * 初始化附件存储根目录，并确保目录存在。
     */
    @PostConstruct
    public void init() throws IOException {
        root = properties.getRoot().toAbsolutePath().normalize();
        Files.createDirectories(root);
        log.info("附件目录初始化完成: {}", root);
    }

    /**
     * 校验并保存上传附件，生成唯一文件名后返回附件访问信息。
     */
    public AttachmentUploadResponse store(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new AttachmentException("上传文件不能为空");
        }
        if (file.getSize() > properties.getMaxSize()) {
            throw new AttachmentException("上传文件超过大小限制");
        }

        String originalName = file.getOriginalFilename() == null ? "file" : file.getOriginalFilename();
        originalName = originalName.replace('\\', '/');
        originalName = originalName.substring(originalName.lastIndexOf('/') + 1);
        if (originalName.isBlank()) {
            originalName = "file";
        }

        String extension = "";
        int dot = originalName.lastIndexOf('.');
        if (dot >= 0) {
            extension = originalName.substring(dot).toLowerCase();
        }

        String datePath = LocalDate.now().format(DATE_DIR);
        String storedName = UUID.randomUUID().toString().replace("-", "") + extension;
        Path dir = root.resolve(datePath).normalize();
        Path target = dir.resolve(storedName).normalize();
        if (!target.startsWith(root)) {
            throw new AttachmentException("非法文件路径");
        }

        try {
            Files.createDirectories(dir);
            try (InputStream in = file.getInputStream()) {
                Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
            }
            String relativePath = root.relativize(target).toString().replace('\\', '/');
            log.info("附件上传成功: {} -> {}", originalName, relativePath);
            return new AttachmentUploadResponse(
                    originalName,
                    storedName,
                    relativePath,
                    "/files/" + relativePath,
                    file.getSize(),
                    file.getContentType()
            );
        } catch (IOException ex) {
            log.error("附件保存失败: {}", originalName, ex);
            throw new AttachmentException("附件保存失败", ex);
        }
    }
}
