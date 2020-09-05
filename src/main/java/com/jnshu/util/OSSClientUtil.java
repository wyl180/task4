package com.jnshu.util;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.*;
import com.aliyuncs.utils.StringUtils;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.region.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.net.URL;
import java.util.Date;
import java.util.Random;

/**
 * @ClassName OOSUtil
 * @Description
 * @Author 韦延伦
 * @Date 2020/9/4 21:23
 * @Version 1.0
 */
@Component
public class OSSClientUtil {
    private Logger log = LoggerFactory.getLogger(OSSClientUtil.class);
    /**
     * 阿里密钥
     */
    @Value("${accessKeyId}")
    private String accessKeyId;
    @Value("${accessKeySecret}")
    private String accessKeySecret;
    /**
     * 腾讯密钥
     */
    @Value("${tentcentAccessKeyId}")
    private String tencentAccessKeId;
    @Value("${tentcentAccessKeySecret}")
    private String tencentAccessKeySecret;
    /**
     * 图片上传业务
     */
    // Endpoint以杭州为例，其它Region请按实际情况填写。
    private String endpoint = "http://oss-cn-shenzhen.aliyuncs.com";
    /**
     * 阿里桶名称
     */
    @Value("${bucket}")
    private String bucketName;
    /**
     * 腾讯云名称
     */
    @Value("${tencentBucket}")
    private String tencentBucketName;

    //文件存储目录
    private String filedir = "data/";



    /**
     * 上传文件到oss
     *
     * @param file
     * @return
     * @throws ImgException
     */
    public String uploadImg2Oss(MultipartFile file) throws ImgException {
        if (file.getSize() > 1024 * 1024) {
            throw new ImgException("上传图片大小不能超过1M！");
        }
        String originalFilename = file.getOriginalFilename();
        String substring = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
        Random random = new Random();
        //文件名是随机数+当前时间戳+后缀名构成。
        String name = random.nextInt(10000) + System.currentTimeMillis() + substring;
        try {
            InputStream inputStream = file.getInputStream();
            this.uploadFile2OSS(inputStream, name);
            return name;
        } catch (Exception e) {
            throw new ImgException("图片上传失败");
        }
    }

    /**
     * 获得图片路径
     *
     * @param fileUrl
     * @return
     */
    public String getImgUrl(String fileUrl) {
        if (!StringUtils.isEmpty(fileUrl)) {
            String[] split = fileUrl.split("/");
            return this.getUrl(this.filedir + split[split.length - 1]);
        }
        return null;
    }

    /**
     * 上传到OSS服务器  如果同名文件会覆盖服务器上的
     *
     * @param instream 文件流
     * @param fileName 文件名称 包括后缀名
     * @return 出错返回"" ,唯一MD5数字签名
     */
    public String uploadFile2OSS(InputStream instream, String fileName) {
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        String ret = "";
        try {
            //创建上传Object的Metadata,这是用户对object的描述
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(instream.available());
            objectMetadata.setCacheControl("no-cache");
            objectMetadata.setHeader("Pragma", "no-cache");
            //设置文件类型
            objectMetadata.setContentType(getcontentType(fileName.substring(fileName.lastIndexOf("."))));
            objectMetadata.setContentDisposition("inline;filename=" + fileName);
            //上传文件
            PutObjectResult putResult = ossClient.putObject(bucketName, filedir + fileName, instream, objectMetadata);
            ret = putResult.getETag();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            try {
                if (instream != null) {
                    instream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ossClient.shutdown();
        return ret;
    }

    /**
     * Description: 判断OSS服务文件上传时文件的contentType
     *
     * @param FilenameExtension 文件后缀
     * @return String
     */
    public static String getcontentType(String FilenameExtension) {
        if (FilenameExtension.equalsIgnoreCase(".bmp")) {
            return "image/bmp";
        }
        if (FilenameExtension.equalsIgnoreCase(".gif")) {
            return "image/gif";
        }
        if (FilenameExtension.equalsIgnoreCase(".jpeg") ||
                FilenameExtension.equalsIgnoreCase(".jpg") ||
                FilenameExtension.equalsIgnoreCase(".png")) {
            return "image/jpeg";
        }
        if (FilenameExtension.equalsIgnoreCase(".html")) {
            return "text/html";
        }
        if (FilenameExtension.equalsIgnoreCase(".txt")) {
            return "text/plain";
        }
        if (FilenameExtension.equalsIgnoreCase(".vsd")) {
            return "application/vnd.visio";
        }
        if (FilenameExtension.equalsIgnoreCase(".pptx") ||
                FilenameExtension.equalsIgnoreCase(".ppt")) {
            return "application/vnd.ms-powerpoint";
        }
        if (FilenameExtension.equalsIgnoreCase(".docx") ||
                FilenameExtension.equalsIgnoreCase(".doc")) {
            return "application/msword";
        }
        if (FilenameExtension.equalsIgnoreCase(".xml")) {
            return "text/xml";
        }
        return "image/jpeg";
    }

    /**
     * 获得url链接
     *
     * @param key 上传的文件
     * @return
     */
    public String getUrl(String key) {
        // 设置URL过期时间为10年  3600l* 1000*24*365*10
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        Date expiration = new Date(System.currentTimeMillis() + 3600L * 1000 * 24 * 365 * 10);
        // 生成URL
        URL url = ossClient.generatePresignedUrl(bucketName, key, expiration);
        if (url != null) {
            ossClient.shutdown();
            return url.toString();
        }
        return null;
    }

    /**
     * 从阿里云批量下载文件的方法
     */
    public void downLoad(String localPath) {
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        //构造ListObjectRequest请求
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest(bucketName);
        //设置为“/"时，罗列该文件夹下所有的文件
        listObjectsRequest.setDelimiter("/");
        listObjectsRequest.setPrefix("data/");

        ObjectListing listing = ossClient.listObjects(listObjectsRequest);

        //遍历该文件夹下的所有文件
        for (OSSObjectSummary ossObjectSummary : listing.getObjectSummaries()) {
            String key = ossObjectSummary.getKey();

            //判断文件所在本地路径是否存在，若无，则创建目录
            File file = new File(localPath + key);

            File fileParent = file.getParentFile();
            if (!fileParent.exists()) {
                fileParent.mkdirs();
            }
            //        下载Object到文件
            ossClient.getObject(new GetObjectRequest(bucketName, key), file);
            System.out.println("路径=======" + file.getAbsoluteFile());
            System.out.println("key======" + key);
        }
        log.info("下载完成");
        ossClient.shutdown();
    }

    /**
     * 迁移方法
     */
    public void ossToCos(String localPath) {
        /**
         * 腾讯云信息
         */
        String secretId = tencentAccessKeId;
        String secretKey =tencentAccessKeySecret;
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        Region region = new Region("ap-shenzhen-fsi");
        ClientConfig clientConfig = new ClientConfig(region);
        COSClient cosClient = new COSClient(cred, clientConfig);
        /**
         * 阿里云信息
         */
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        //构造ListObjectRequest请求
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest(bucketName);
        //设置为“/"时，罗列该文件夹下所有的文件
        listObjectsRequest.setDelimiter("/");
        listObjectsRequest.setPrefix("data/");

        ObjectListing listing = ossClient.listObjects(listObjectsRequest);

        //遍历该文件夹下的所有文件
        for (OSSObjectSummary ossObjectSummary : listing.getObjectSummaries()) {
            String key = ossObjectSummary.getKey();

            //判断文件所在本地路径是否存在，若无，则创建目录
            File file = new File(localPath + key);

            File fileParent = file.getParentFile();
            if (!fileParent.exists()) {
                fileParent.mkdirs();
            }
            // 下载Object到文件
            ossClient.getObject(new GetObjectRequest(bucketName, key), file);
            // 指定要上传到的存储桶
            String bucketName = tencentBucketName;
            // 指定要上传到 COS 上对象键
            com.qcloud.cos.model.PutObjectRequest putObjectRequest = new  com.qcloud.cos.model.PutObjectRequest(bucketName, key, file);
            com.qcloud.cos.model.PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
            System.out.println("路径=======" + file.getAbsoluteFile());
            System.out.println("key======" + key);
        }
        ossClient.shutdown();
        cosClient.shutdown();
    }

}
