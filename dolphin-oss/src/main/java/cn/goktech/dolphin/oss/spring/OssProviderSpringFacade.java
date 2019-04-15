package cn.goktech.dolphin.oss.spring;

import cn.goktech.dolphin.oss.OssProvider;
import cn.goktech.dolphin.oss.UploadObject;
import cn.goktech.dolphin.oss.UploadTokenParam;
import cn.goktech.dolphin.oss.enumeration.Provider;
import cn.goktech.dolphin.oss.provider.aliyun.AliyunossProvider;
import cn.goktech.dolphin.oss.provider.qiniu.QiniuProvider;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.io.File;
import java.io.InputStream;
import java.util.Map;

/**
 * @author funcas
 * @version 1.0
 * @date 2019年04月12日
 */
public class OssProviderSpringFacade implements InitializingBean, DisposableBean {
    private OssProvider ossProvider;
    private String endpoint;
    private String provider;
    private String bucketName;
    private String accessKey;
    private String secretKey;
    private String urlPrefix;
    private String servers;
    private String internalUrl;
    private long connectTimeout = 3000;
    private int maxThreads = 50;
    private boolean ifPrivate;

    @Override
    public void destroy() throws Exception {
        ossProvider.close();
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        if(Provider.QINIU_OSS.getValue().equals(provider)){
            Validate.notBlank(accessKey, "[accessKey] not defined");
            Validate.notBlank(secretKey, "[secretKey] not defined");
            ossProvider = new QiniuProvider(urlPrefix, bucketName, accessKey, secretKey, ifPrivate);
        }else if(Provider.ALI_OSS.getValue().equals(provider)){
            Validate.notBlank(endpoint, "[endpoint] not defined");
            ossProvider = new AliyunossProvider(urlPrefix, endpoint, bucketName ,accessKey, secretKey, internalUrl, ifPrivate);
        }else{
            throw new RuntimeException("Provider[" + provider + "] not support");
        }
    }

    public String upload(String fileName, File file) {
        return ossProvider.upload(new UploadObject(fileName, file));
    }


    public String upload(String fileName, InputStream in, String mimeType) {
        return ossProvider.upload(new UploadObject(fileName, in, mimeType));
    }

    public boolean delete(String fileName) {
        return ossProvider.delete(fileName);
    }

    public Map<String, Object> createUploadToken(UploadTokenParam param) {
        return ossProvider.createUploadToken(param);
    }

    public void setConnectTimeout(long connectTimeout) {
        this.connectTimeout = connectTimeout;
    }


    public OssProvider getFsProvider() {
        return ossProvider;
    }

    public void setFsProvider(OssProvider fsProvider) {
        this.ossProvider = fsProvider;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getUrlPrefix() {
        return urlPrefix;
    }

    public void setUrlPrefix(String urlPrefix) {
        this.urlPrefix = urlPrefix;
    }

    public String getServers() {
        return servers;
    }

    public void setServers(String servers) {
        this.servers = servers;
    }

    public long getConnectTimeout() {
        return connectTimeout;
    }

    public int getMaxThreads() {
        return maxThreads;
    }

    public void setMaxThreads(int maxThreads) {
        this.maxThreads = maxThreads;
    }

    public boolean isIfPrivate() {
        return ifPrivate;
    }

    public void setIfPrivate(boolean ifPrivate) {
        this.ifPrivate = ifPrivate;
    }

    public String getInternalUrl() {
        return internalUrl;
    }

    public void setInternalUrl(String internalUrl) {
        this.internalUrl = internalUrl;
    }
}
