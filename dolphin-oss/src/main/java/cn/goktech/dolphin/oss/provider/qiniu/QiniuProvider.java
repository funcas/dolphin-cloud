package cn.goktech.dolphin.oss.provider.qiniu;

import cn.goktech.dolphin.oss.UploadObject;
import cn.goktech.dolphin.oss.UploadTokenParam;
import cn.goktech.dolphin.oss.enumeration.Provider;
import cn.goktech.dolphin.oss.exception.OssOperErrorException;
import cn.goktech.dolphin.oss.provider.AbstractProvider;
import cn.goktech.dolphin.oss.util.FilePathUtils;
import com.google.common.collect.Maps;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.io.IOException;
import java.util.Map;

/**
 * @author funcas
 * @version 1.0
 * @date 2019年04月12日
 */
public class QiniuProvider extends AbstractProvider {

    private static final String DEFAULT_CALLBACK_BODY = "filename=${fname}&size=${fsize}&mimeType=${mimeType}&height=${imageInfo.height}&width=${imageInfo.width}";

    private static final String[] policyFields = new String[]{
            "callbackUrl",
            "callbackBody",
            "callbackHost",
            "callbackBodyType",
            "fileType",
            "saveKey",
            "mimeLimit",
            "fsizeLimit",
            "fsizeMin",
            "deleteAfterDays",
    };

    private static UploadManager uploadManager;
    private static BucketManager bucketManager;
    private Auth auth;
    private boolean isPrivate;
    private String host;

    public QiniuProvider(String urlprefix, String bucketName, String accessKey, String secretKey,boolean isPrivate) {

        Validate.notBlank(bucketName, "[bucketName] not defined");
        Validate.notBlank(accessKey, "[accessKey] not defined");
        Validate.notBlank(secretKey, "[secretKey] not defined");
        Validate.notBlank(urlprefix, "[urlprefix] not defined");

        this.urlPrefix = urlprefix.endsWith(DIR_SPLITER) ? urlprefix : urlprefix + DIR_SPLITER;
        this.bucketName = bucketName;
        auth = Auth.create(accessKey, secretKey);

        Zone z = Zone.autoZone();
        Configuration c = new Configuration(z);
        uploadManager = new UploadManager(c);
        bucketManager = new BucketManager(auth,c);

        this.isPrivate = isPrivate;
        this.host = StringUtils.remove(urlprefix,"/").split(":")[1];
    }

    @Override
    public String upload(UploadObject object) {
        String fileName = object.getFileName();
        if(StringUtils.isNotBlank(object.getCatalog())){
            fileName = object.getCatalog().concat(FilePathUtils.DIR_SPLITER).concat(fileName);
        }
        try {
            Response res = null;
            String upToken = getUpToken(object.getMetadata());
            if(object.getFile() != null){
                res = uploadManager.put(object.getFile(), fileName, upToken);
            }else if(object.getBytes() != null){
                res = uploadManager.put(object.getBytes(), fileName, upToken);
            }else if(object.getInputStream() != null){
                res = uploadManager.put(object.getInputStream(), fileName, upToken, null, object.getMimeType());
            }else if(StringUtils.isNotBlank(object.getUrl())){
                return bucketManager.fetch(object.getUrl(), bucketName, fileName).key;
            }else{
                throw new IllegalArgumentException("upload object is NULL");
            }
            return processUploadResponse(res);
        } catch (QiniuException e) {
            processUploadException(fileName, e);
        }
        return null;
    }

    @Override
    public String getDownloadUrl(String fileKey, boolean isInternal) {
        String path = getFullPath(fileKey);
        if(isPrivate){
            path = auth.privateDownloadUrl(path, 3600);
        }
        return path;
    }

    @Override
    public boolean delete(String fileKey) {
        try {
            if (fileKey.contains(DIR_SPLITER)) {
                fileKey = fileKey.replace(urlPrefix, "");
            }
            bucketManager.delete(bucketName, fileKey);
            return true;
        } catch (QiniuException e) {
            processUploadException(fileKey, e);
        }
        return false;
    }

    @Override
    public Map<String, Object> createUploadToken(UploadTokenParam param) {

        if(StringUtils.isNotBlank(param.getCallbackUrl())){
            if(StringUtils.isBlank(param.getCallbackBody())){
                param.setCallbackBody(DEFAULT_CALLBACK_BODY);
            }
            if(StringUtils.isBlank(param.getCallbackHost())){
                param.setCallbackHost(host);
            }
        }

        Map<String, Object> result = Maps.newHashMap();
        StringMap policy = new StringMap();
        policy.putNotNull(policyFields[0], param.getCallbackUrl());
        policy.putNotNull(policyFields[1], param.getCallbackBody());
        policy.putNotNull(policyFields[2], param.getCallbackHost());
        policy.putNotNull(policyFields[3], param.getCallbackBodyType());
        policy.putNotNull(policyFields[4], param.getFileType());
        policy.putNotNull(policyFields[5], param.getFileKey());
        policy.putNotNull(policyFields[6], param.getMimeLimit());
        policy.putNotNull(policyFields[7], param.getFsizeMin());
        policy.putNotNull(policyFields[8], param.getFsizeMax());
        policy.putNotNull(policyFields[9], param.getDeleteAfterDays());

        String token = auth.uploadToken(bucketName, param.getFileKey(), param.getExpires(), policy, true);
        result.put("uptoken", token);
        result.put("host", this.urlPrefix);
        result.put("dir", param.getUploadDir());

        return result;
    }

    @Override
    public void close() throws IOException {}

    @Override
    public String name() {
        return Provider.QINIU_OSS.getValue();
    }


    /**
     * 处理上传结果，返回文件url
     *
     * @return
     * @throws QiniuException
     */
    private String processUploadResponse(Response res) throws QiniuException {
        if (res.isOK()) {
            UploadResult ret = res.jsonToObject(UploadResult.class);
            return getFullPath(ret.getKey());
        }
        throw new OssOperErrorException(res.toString());
    }

    private void processUploadException(String fileKey, QiniuException e) {
        Response r = e.response;
        String message;
        try {
            message = r.bodyString();
        } catch (Exception e2) {
            message = r.toString();
        }
        throw new OssOperErrorException(name(), e);
    }


    private String getUpToken(Map<String, Object> metadata) {
        return auth.uploadToken(bucketName);
    }
}
