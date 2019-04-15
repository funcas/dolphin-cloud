package cn.goktech.dolphin.oss.provider;

import cn.goktech.dolphin.common.http.HttpUtils;
import cn.goktech.dolphin.oss.OssProvider;

/**
 * @author funcas
 * @version 1.0
 * @date 2019年04月12日
 */
public abstract class AbstractProvider implements OssProvider {

    private static final String HTTP_PREFIX = "http://";
    private static final String HTTPS_PREFIX = "https://";

    protected static final String DIR_SPLITER = "/";

    protected String urlPrefix;

    protected String bucketName;

    protected String getFullPath(String file) {
        if(file.startsWith(HTTP_PREFIX) || file.startsWith(HTTPS_PREFIX)){
            return file;
        }
        return urlPrefix + file;
    }


    @Override
    public String downloadAndSaveAs(String file, String localSaveDir, boolean isInternal) {
        return HttpUtils.downloadFile(getDownloadUrl(file, isInternal), localSaveDir);
    }
}
