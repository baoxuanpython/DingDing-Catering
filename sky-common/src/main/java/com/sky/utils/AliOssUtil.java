package com.sky.utils;

import com.aliyun.sdk.service.oss2.OSSClient;
import com.aliyun.sdk.service.oss2.OSSClientBuilder;
import com.aliyun.sdk.service.oss2.credentials.CredentialsProvider;
import com.aliyun.sdk.service.oss2.credentials.StaticCredentialsProvider;
import com.aliyun.sdk.service.oss2.models.DeleteObjectRequest;
import com.aliyun.sdk.service.oss2.models.PutObjectRequest;
import com.sky.properties.AliOssProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import com.aliyun.sdk.service.oss2.transport.BinaryData;

/**
 * 阿里云 OSS 文件操作工具类
 * 负责将文件上传至指定 Bucket，并返回可访问的文件 URL
 */

@Slf4j
public class AliOssUtil {
    private final String bucketName;
    private final String region;
    private final CredentialsProvider credentialsProvider;

    /**
     * 构造器注入 OSS 配置信息
     *
     * @param aliyunOSSProperties 阿里云 OSS 配置对象（含 bucketName、region、accessKeyId、accessKeySecret）
     */
    public AliOssUtil(AliOssProperties aliyunOSSProperties) {
        this.bucketName = aliyunOSSProperties.getBucketName();
        this.region = aliyunOSSProperties.getRegion();
        this.credentialsProvider = new StaticCredentialsProvider(
                aliyunOSSProperties.getAccessKeyId(),
                aliyunOSSProperties.getAccessKeySecret()
        );
    }

    /**
     * 上传文件到阿里云 OSS
     * 存储路径格式：yyyy/MM/UUID.后缀名，避免文件名冲突
     *
     * @param content          文件二进制内容
     * @param originalFilename 原始文件名（用于提取后缀名）
     * @return 文件访问 URL（格式：https://{bucket}.oss-{region}.aliyuncs.com/{objectName}）
     * @throws Exception 客户端创建或上传过程中发生异常时抛出
     */
    public String upload(byte[] content, String originalFilename) throws Exception {
        String dir = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM"));
        // 提取后缀名
        String ext = FilenameUtils.getExtension(originalFilename);
        String newFileName = UUID.randomUUID() + "." + ext;
        String objectName = dir + "/" + newFileName;

        OSSClientBuilder clientBuilder = OSSClient.newBuilder()
                .credentialsProvider(credentialsProvider)
                .region(region);
        try (OSSClient client = clientBuilder.build()) {
            client.putObject(PutObjectRequest.newBuilder()
                    .bucket(bucketName)
                    .key(objectName)
                    .body(BinaryData.fromBytes(content))
                    .build());
        }
        log.info("图片网址：{}", "https://" + bucketName + ".oss-" + region + ".aliyuncs.com/" + objectName);
        return "https://" + bucketName + ".oss-" + region + ".aliyuncs.com/" + objectName;
    }

    /**
     * 从阿里云 OSS 删除指定文件
     *
     * @param fileUrl 文件完整访问 URL（含 bucket 域名和 objectName）
     */
    public void deleteFileFromOSS(String fileUrl) {
        String prefix = "https://" + bucketName + ".oss-" + region + ".aliyuncs.com/";
        if (fileUrl == null || !fileUrl.startsWith(prefix)) {
            return;
        }
        String objectName = fileUrl.substring(prefix.length());
        OSSClientBuilder clientBuilder = OSSClient.newBuilder()
                .credentialsProvider(credentialsProvider)
                .region(region);
        try (OSSClient client = clientBuilder.build()) {
            client.deleteObject(DeleteObjectRequest.newBuilder()
                    .bucket(bucketName)
                    .key(objectName)
                    .build());
        } catch (Exception e) {
            throw new RuntimeException("OSS 文件删除失败：" + fileUrl, e);
        }
    }
}
