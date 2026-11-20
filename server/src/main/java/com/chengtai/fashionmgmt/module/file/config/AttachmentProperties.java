package com.chengtai.fashionmgmt.module.file.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "file.attachment")
public class AttachmentProperties {
    private Path root = Paths.get("attachments");
    private long maxSize = 20 * 1024 * 1024L;
}
