package com.ifitmix.common.client;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.*;
import com.ifitmix.common.spring.ApplicationContextHolder;
import org.apache.commons.io.FileUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.*;

/**
 * Created by sin on 2015/11/10.
 * 阿里云 oss 文件工具类
 */
public class AliyunCenterClient {

    public static final String LOCATION_CACHE = "../cache/";

    private static final String ENDPOINT = ApplicationContextHolder.constant.ALIYUN_ENDPOINT;

    private static final String ACCESS_ID = ApplicationContextHolder.constant.ALIYUN_ACCESS_ID;

    private static final String ACCESS_KEY = ApplicationContextHolder.constant.ALIYUN_ACCESS_KEY;
    /**
     * bucketName
     */
    private static final String BUCKET_NAME = ApplicationContextHolder.constant.ALIYUN_BUCKET_NAME;

    private static final String STORAGE_PATH = ApplicationContextHolder.constant.STORAGE_PATH;

    // 创建OSSClient实例
    private static OSSClient client = new OSSClient(ENDPOINT, ACCESS_ID, ACCESS_KEY);

    /**
     * put 文件
     * @param fileType 文件类型 {@link com.ifitmix.common.constants.FileConstants}
     * @param multipartFile springMVC 文件工具
     * @return 文件地址
     */
    public static String putFile(String fileType, MultipartFile multipartFile) {
        String uName = UUID.randomUUID().toString().replace("-", "") + multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf("."));
        try {
            return putFile(fileType, uName, multipartFile.getBytes().length, multipartFile.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String putFile(Integer fileType, MultipartFile multipartFile) {
        return putFile(String.valueOf(fileType), multipartFile);
    }

    /**
     * put 文件
     * @param fileType 文件类型 {@link com.ifitmix.common.constants.FileConstants}
     * @param bytes 文件流
     * @return 文件地址
     */
    public static String putFile(String fileType, String name, byte[] bytes) {
        String uName = UUID.randomUUID().toString().replace("-", "") + name.substring(name.lastIndexOf("."));
        return putFile(fileType, uName, bytes.length, new ByteArrayInputStream(bytes));
    }

    /**
     * put 文件
     * @param fileType 文件类型 {@link com.ifitmix.common.constants.FileConstants}
     * @param uName 名称（uuid 名称）
     * @param length 文件长度
     * @param inputStream 文件流
     * @return 文件地址
     */
    public static String putFile(String fileType, String uName, long length, InputStream inputStream) {
        String path = fileType + "/" + uName;
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(length);
        client.putObject(BUCKET_NAME, path, inputStream, objectMetadata);
        return path;
    }

    /**
     * 删除文件
     * @param path 文件地址
     * @return true 成功 false 失败
     */
    public static boolean deleteFile(String path) {
        try {
            if (path.matches("^\\/.*$")) {
                path = path.substring(1);
            }
            client.deleteObject(BUCKET_NAME, path);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }



    /**
     * 累加文件，在之前文件上累加数据
     * @param fileType 文件类型
     * @param multipartFile 文件数据
     * @param suffix 文件后缀
     * @param part 累加的部分
     * @return 文件地址，旧文件是之前地址
     */
    public static String appendFile(String fileType, String url, MultipartFile multipartFile, String suffix, Long part) {
        String uName = UUID.randomUUID().toString().replace("-", "") + suffix;
        /// 新文件没有 名称
        if (!StringUtils.isEmpty(url)) {
            uName = url.substring(url.lastIndexOf("/") + 1);
        }
        String key = fileType+ "/" + uName;

        /// 存在 则获取 Object 创建，没有 第一次创建内容需要 position 0
        try {
            // 设置content-type，注意设置object meta只能在使用Append创建Object设置
            ObjectMetadata meta = new ObjectMetadata();
            meta.setContentType(multipartFile.getContentType());
            AppendObjectResult appendObjectResult;
            AppendObjectRequest appendObjectRequest;
            if (part == null || part == 0 ) {

                appendObjectRequest = new AppendObjectRequest(BUCKET_NAME, key, multipartFile.getInputStream(), meta);
                // 设置追加位置为0,发送追加Object请求
                appendObjectRequest.setPosition(0L);
                appendObjectResult = client.appendObject(appendObjectRequest);
            }
            else {
                // 获取Object，返回结果为OSSObject对象
                OSSObject object = client.getObject(BUCKET_NAME, key);
                // 创建 appendObjectResult
                appendObjectResult = new AppendObjectResult();
                appendObjectResult.setNextPosition(object.getObjectMetadata().getContentLength());
            }
            // 发起第二次追加Object请求，追加位置为第一次追加后的Object长度
            appendObjectRequest = new AppendObjectRequest(BUCKET_NAME, key, multipartFile.getInputStream(), meta);

            // 设置追加位置为前一次追加文件的大小,发送追加Object请求
            appendObjectRequest.setPosition(appendObjectResult.getNextPosition());
            client.appendObject(appendObjectRequest);
        } catch (Exception e) {
            throw new RuntimeException("append file upload error...", e);
        }
        return key;
    }

    public static String multipartFile(String fileType, MultipartFile multipartFile, String name, long size, long partSize, long part) {
        String path = "";
        try {
            File file = new File(LOCATION_CACHE + name);
            /// append file
            FileUtils.writeByteArrayToFile(file, multipartFile.getBytes(), true);
            /// 上传 aliyun
            if (partSize * part >= size) {
                path = multipartFile(fileType, file);
                // 删除本地缓存文件
                FileUtils.deleteQuietly(file);
            }
        } catch (Exception e) {
           throw new RuntimeException("multipart file upload error...", e);
        }
        return path;
    }

    public static String multipartFile(String fileType, File file) {
        String suffix = file.getName().substring(file.getName().lastIndexOf("."));
        String uName = UUID.randomUUID().toString().replace("-", "") + suffix;
        String path = fileType + "/" + uName;
        // 发起首次追加Object请求，注意首次追加需要设置追加位置为0
        try {
            // 开始Multipart Upload
            InitiateMultipartUploadRequest initiateMultipartUploadRequest = new InitiateMultipartUploadRequest(BUCKET_NAME, path);
            InitiateMultipartUploadResult initiateMultipartUploadResult = client.initiateMultipartUpload(initiateMultipartUploadRequest);

            // 设置每块为 5M
            final int partSize = 1024 * 1024 * 5;
            // 计算分块数目
            int partCount = (int) (file.length() / partSize);
            if (file.length() % partSize != 0){
                partCount++;
            }
            // 新建一个List保存每个分块上传后的ETag和PartNumber
            List<PartETag> partETags = new ArrayList<>();
            for(int i = 0; i < partCount; i++){
                // 获取文件流
                FileInputStream fis = new FileInputStream(file);
                // 跳到每个分块的开头
                long skipBytes = partSize * i;
                fis.skip(skipBytes);
                // 计算每个分块的大小
                long size = partSize < file.length() - skipBytes ?
                        partSize : file.length() - skipBytes;
                // 创建UploadPartRequest，上传分块
                UploadPartRequest uploadPartRequest = new UploadPartRequest();
                uploadPartRequest.setBucketName(BUCKET_NAME);
                uploadPartRequest.setKey(path);
                uploadPartRequest.setUploadId(initiateMultipartUploadResult.getUploadId());
                uploadPartRequest.setInputStream(fis);
                uploadPartRequest.setPartSize(size);
                uploadPartRequest.setPartNumber(i + 1);
                UploadPartResult uploadPartResult = client.uploadPart(uploadPartRequest);
                // 将返回的PartETag保存到List中。
                partETags.add(uploadPartResult.getPartETag());
                // 关闭文件
                fis.close();
            }
            CompleteMultipartUploadRequest completeMultipartUploadRequest = new CompleteMultipartUploadRequest(BUCKET_NAME, path, initiateMultipartUploadResult.getUploadId(), partETags);
            // 完成分块上传
            CompleteMultipartUploadResult completeMultipartUploadResult = client.completeMultipartUpload(completeMultipartUploadRequest);
            /// 删除缓存文件
            FileUtils.deleteQuietly(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }

    /**
     * 构建 MultipartFile
     * @param request 请求
     * @return 构建好的 MultipartFile Map
     */
    public static MultiValueMap<String, MultipartFile> buildMultipartFiles (HttpServletRequest request) {
        return ((DefaultMultipartHttpServletRequest) request).getMultiFileMap();
    }

    /**
     * 构建 MultipartFile
     * @param request 请求
     * @return 构建好的 MultipartFile Map
     */
    public static Map<String, MultipartFile> buildMultipartFile (HttpServletRequest request) {
        Map<String, MultipartFile> fileMap = new LinkedHashMap<>(0);
        if (request instanceof MultipartHttpServletRequest) {
            fileMap = ((MultipartHttpServletRequest) request).getFileMap();
        }
        return fileMap;
    }

    /**
     * 构建文件地址
     * @param uri
     * @return
     */
    public static String buildUrl (String uri) {
        if (StringUtils.isEmpty(uri)) {
            return uri;
        }
        if (uri.indexOf("http:") > -1 || uri.indexOf("https:") > -1) {
            return uri;
        }
        return STORAGE_PATH + "/" + uri;
    }
}
