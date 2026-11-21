package com.dingdingcatering.service.common.impl;

import com.dingdingcatering.enumeration.ErrorCode;
import com.dingdingcatering.exception.UploadFileFailException;
import com.dingdingcatering.exception.UploadFileInvalidException;
import com.dingdingcatering.service.common.FileUploadService;
import com.dingdingcatering.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Locale;
import java.util.Set;

/**
 * 文件上传服务实现
 * <p>
 * 先做本地校验（空文件、体积、扩展名），再上传到阿里云 OSS。
 * 校验失败抛 {@link UploadFileInvalidException}（客户端错误，HTTP 400），
 * OSS 调用失败抛 {@link UploadFileFailException}（服务端错误，HTTP 500）。
 */
@Service
@Slf4j
public class FileUploadServiceImpl implements FileUploadService {

    /** 单文件大小上限：5MB */
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024L;

    /** 允许的图片扩展名 */
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("jpg", "jpeg", "png", "gif", "webp");

    private final AliOssUtil aliOssUtil;

    public FileUploadServiceImpl(AliOssUtil aliOssUtil) {
        this.aliOssUtil = aliOssUtil;
    }

    @Override
    public String upload(MultipartFile file) {
        validate(file);

        String originalFilename = file.getOriginalFilename();
        try {
            String url = aliOssUtil.upload(file.getBytes(), originalFilename);
            log.info("文件上传成功: name={}, size={}B", originalFilename, file.getSize());
            return url;
        } catch (Exception e) {
            log.error("文件上传失败: name={}, size={}B", originalFilename, file.getSize(), e);
            throw new UploadFileFailException(ErrorCode.UPLOAD_FAILED);
        }
    }

    /**
     * 校验待上传文件。
     *
     * @param file 待上传的文件
     */
    private void validate(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new UploadFileInvalidException(ErrorCode.UPLOAD_FAILED, "上传文件不能为空");
        }
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new UploadFileInvalidException(ErrorCode.UPLOAD_FAILED, "文件大小不能超过 5MB");
        }
        String ext = StringUtils.getFilenameExtension(file.getOriginalFilename());
        if (ext == null || !ALLOWED_EXTENSIONS.contains(ext.toLowerCase(Locale.ROOT))) {
            throw new UploadFileInvalidException(ErrorCode.UPLOAD_FAILED,
                    "仅支持 jpg、jpeg、png、gif、webp 格式的图片");
        }
    }
}