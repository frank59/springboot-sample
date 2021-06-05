package com.geewaza.code.minio;

import com.geewaza.code.common.util.ToolMethods;
import io.minio.MinioClient;
import io.minio.PutObjectOptions;
import io.minio.errors.MinioException;
import org.apache.commons.io.IOUtils;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @author wangh
 */
public class FileUploader {

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException, InvalidKeyException, XmlPullParserException {
        String uploadFilePath = "upload/";
        String uploadFileName = "test01.txt";
        String bucketName = "test-bucket";

        String host = "http://192.168.10.10";
        String accessKey = "wangheng";
        String secretKey = "wangheng@geewaza.com";
        try {
            // 使用MinIO服务的URL，端口，Access key和Secret key创建一个MinioClient对象
            MinioClient minioClient = new MinioClient(host, 9000, accessKey, secretKey);

            // 检查存储桶是否已经存在
            boolean isExist = minioClient.bucketExists(bucketName);
            if(isExist) {
                System.out.println("Bucket already exists.");
            } else {
                // 创建一个名为asiatrip的存储桶，用于存储照片的zip文件。
                minioClient.makeBucket(bucketName);
            }

            // 使用putObject上传一个文件到存储桶中。
            File file = ToolMethods.getResourceFile(uploadFilePath + uploadFileName);

            try (InputStream inputStream = new FileInputStream(file)){
                PutObjectOptions putObjectOptions = new PutObjectOptions(file.length(), PutObjectOptions.MAX_PART_SIZE);
                minioClient.putObject(bucketName,uploadFileName, inputStream, putObjectOptions);
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println(file.getAbsolutePath() + " is successfully uploaded as " + uploadFileName+ " to `" + bucketName+ "` bucket.");
        } catch(MinioException e) {
            System.out.println("Error occurred: " + e);
        }
    }
}
