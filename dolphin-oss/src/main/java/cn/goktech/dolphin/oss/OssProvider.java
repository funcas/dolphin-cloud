package cn.goktech.dolphin.oss;

import java.io.Closeable;
import java.util.Map;

/**
 * @author funcas
 * @version 1.0
 * @date 2019年04月12日
 */
public interface OssProvider extends Closeable {

    String name();
    /**
     * 文件上传
     * @param object
     * @return
     */
    public String upload(UploadObject object);
    /**
     * 获取文件下载地址
     * @param
     * @return
     */
    public String getDownloadUrl(String fileKey, boolean isInternal);

    /**
     * 删除图片
     * @return
     */
    public boolean delete(String fileKey);

    public String downloadAndSaveAs(String fileKey,String localSaveDir, boolean isInternal);

    public Map<String, Object> createUploadToken(UploadTokenParam param);
}
