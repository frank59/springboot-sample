package com.geewaza.code.minio;

import com.geewaza.code.common.util.ToolMethods;
import io.minio.MinioClient;
import io.minio.PutObjectOptions;
import io.minio.errors.MinioException;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author wangh
 */
public class FileUploader {

    static String uploadFilePath = "upload/";
    static String downloadFilePath = "download/";
    static String uploadFileName = "test01.txt";
    static String bucketName = "test-bucket";

    static String host = "http://192.168.10.10";
    static String accessKey = "wangheng";
    static String secretKey = "wangheng@geewaza.com";

    public static void main(String[] args) {
        upload();
        download();
    }

    public static void upload() {
        try {
            // 使用MinIO服务的URL，端口，Access key和Secret key创建一个MinioClient对象
            MinioClient minioClient = new MinioClient(host, 9000, accessKey, secretKey);

            // 检查存储桶是否已经存在
            boolean isExist = minioClient.bucketExists(bucketName);
            if (isExist) {
                System.out.println("Bucket already exists.");
            } else {
                // 创建一个名为asiatrip的存储桶，用于存储照片的zip文件。
                minioClient.makeBucket(bucketName);
            }

            // 使用putObject上传一个文件到存储桶中。
            File file = ToolMethods.getResourceFile(uploadFilePath + uploadFileName);

            try (InputStream inputStream = new FileInputStream(file)) {
                PutObjectOptions putObjectOptions = new PutObjectOptions(file.length(), PutObjectOptions.MAX_PART_SIZE);
                minioClient.putObject(bucketName, uploadFileName, inputStream, putObjectOptions);
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println(file.getAbsolutePath() + " is successfully uploaded as " + uploadFileName + " to `" + bucketName + "` bucket.");
        } catch (Exception e) {
            System.out.println("Error occurred: " + e);
        }
    }


    public static void download() {
        try {
            // 使用MinIO服务的URL，端口，Access key和Secret key创建一个MinioClient对象
            MinioClient minioClient = new MinioClient(host, 9000, accessKey, secretKey);
            try (InputStream inputStream = minioClient.getObject(bucketName, uploadFileName)) {
                File file = ToolMethods.buildNewFile(downloadFilePath + uploadFileName);
                FileUtils.copyInputStreamToFile(inputStream, file);
                System.out.println(uploadFileName + " is successfully download from " + bucketName + " to " + file.getAbsolutePath() + ".");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (MinioException e) {
            System.out.println("Error occurred: " + e);
        }
    }


}
