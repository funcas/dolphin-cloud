package cn.goktech.dolphin.oss.provider.aliyun;

import cn.goktech.dolphin.oss.UploadObject;
import cn.goktech.dolphin.oss.UploadTokenParam;
import cn.goktech.dolphin.oss.enumeration.Provider;
import cn.goktech.dolphin.oss.provider.AbstractProvider;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.time.DateUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author funcas
 * @version 1.0
 * @date 2019年04月12日
 */
public class AliyunossProvider extends AbstractProvider {

    private static final String URL_PREFIX_PATTERN = "(http).*\\.(com|cn)\\/";
    private static final String DEFAULT_CALLBACK_BODY = "filename=${object}&size=${size}&mimeType=${mimeType}&height=${imageInfo.height}&width=${imageInfo.width}";

    private OSSClient ossClient;
    private String bucketName;
    private boolean isPrivate;
    private String accessKeyId;
    private String host;
    private String internalUrl;

    public AliyunossProvider(String urlprefix,String endpoint, String bucketName, String accessKey, String secretKey,String internalUrl, boolean isPrivate) {

        Validate.notBlank(endpoint, "[endpoint] not defined");
        Validate.notBlank(bucketName, "[bucketName] not defined");
        Validate.notBlank(accessKey, "[accessKey] not defined");
        Validate.notBlank(secretKey, "[secretKey] not defined");
        Validate.notBlank(urlprefix, "[urlprefix] not defined");
        this.internalUrl = internalUrl;
        this.accessKeyId = accessKey;
        ossClient = new OSSClient(endpoint, accessKey, secretKey);
        this.bucketName = bucketName;
        this.urlPrefix = urlprefix.endsWith("/") ? urlprefix : (urlprefix + "/");
        this.isPrivate = isPrivate;
        this.host = StringUtils.remove(urlprefix,"/").split(":")[1];
        if (!ossClient.doesBucketExist(bucketName)) {
            System.out.println("Creating bucket " + bucketName + "\n");
            ossClient.createBucket(bucketName);
            CreateBucketRequest createBucketRequest= new CreateBucketRequest(bucketName);
            createBucketRequest.setCannedACL(isPrivate ? CannedAccessControlList.Private : CannedAccessControlList.PublicRead);
            ossClient.createBucket(createBucketRequest);
        }
    }

    @Override
    public String upload(UploadObject object) {
        try {
            PutObjectRequest request = null;
            if(object.getFile() != null){
                request = new PutObjectRequest(bucketName, object.getFileName(), object.getFile());
            }else if(object.getBytes() != null){
                request = new PutObjectRequest(bucketName, object.getFileName(), new ByteArrayInputStream(object.getBytes()));
            }else if(object.getInputStream() != null){
                request = new PutObjectRequest(bucketName, object.getFileName(), object.getInputStream());
            }else{
                throw new IllegalArgumentException("upload object is NULL");
            }

            PutObjectResult result = ossClient.putObject(request);
            if(result.getResponse() == null){
                return isPrivate ? object.getFileName() : urlPrefix + object.getFileName();
            }
            if(result.getResponse().isSuccessful()){
                return result.getResponse().getUri();
            }else{
                throw new RuntimeException(result.getResponse().getErrorResponseAsString());
            }
        } catch (OSSException e) {
            throw new RuntimeException(e.getErrorMessage());
        }
    }



    //https://help.aliyun.com/document_detail/31926.html
    //https://help.aliyun.com/document_detail/31989.html?spm=a2c4g.11186623.6.907.tlMQcL
    @Override
    public Map<String, Object> createUploadToken(UploadTokenParam param) {

        Map<String, Object> result = new HashMap<>();

        PolicyConditions policyConds = new PolicyConditions();
        if(param.getFsizeMin() != null && param.getFsizeMax() != null){
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, param.getFsizeMin(), param.getFsizeMax());
        }else{
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
        }
        if(param.getUploadDir() != null){
            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, param.getUploadDir());
        }

        if(StringUtils.isBlank(param.getCallbackHost())){
            param.setCallbackHost(host);
        }

        if(StringUtils.isBlank(param.getCallbackBody())){
            param.setCallbackBody(DEFAULT_CALLBACK_BODY);
        }

        Date expire = DateUtils.addSeconds(new Date(), (int)param.getExpires());
        String policy = ossClient.generatePostPolicy(expire, policyConds);
        String policyBase64 = null;
        String callbackBase64 = null;
        try {
            policyBase64 = BinaryUtil.toBase64String(policy.getBytes(StandardCharsets.UTF_8.name()));
            String callbackJson = param.getCallbackRuleAsJson();
            if(callbackJson != null){
                callbackBase64 = BinaryUtil.toBase64String(callbackJson.getBytes(StandardCharsets.UTF_8.name()));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        String signature = ossClient.calculatePostSignature(policy);

        result.put("OSSAccessKeyId", accessKeyId);
        result.put("policy", policyBase64);
        result.put("signature", signature);
        result.put("host", this.urlPrefix);
        result.put("dir", param.getUploadDir());
        result.put("expire", String.valueOf(expire.getTime()));
        if(callbackBase64 != null){
            result.put("callback", callbackBase64);
        }
        return result;
    }

    @Override
    public boolean delete(String fileKey) {
        ossClient.deleteObject(bucketName, fileKey);
        return true;
    }

    @Override
    public String getDownloadUrl(String fileKey, boolean isInternal) {
        String visitedUrl;
        if(isPrivate){
            URL url = ossClient.generatePresignedUrl(bucketName, fileKey, DateUtils.addHours(new Date(), 1));
            visitedUrl = url.toString();
        }else {
            visitedUrl = urlPrefix + fileKey;
        }

        if (isInternal) {
            visitedUrl = visitedUrl.replaceFirst(URL_PREFIX_PATTERN, internalUrl);
        }
        return visitedUrl;
    }


    @Override
    public void close() throws IOException {
        ossClient.shutdown();
    }

    @Override
    public String name() {
        return Provider.ALI_OSS.getValue();
    }
}
