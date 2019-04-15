package cn.goktech.dolphin.oss.provider.qiniu;

import lombok.Data;

/**
 * @author funcas
 * @version 1.0
 * @date 2019年04月12日
 */
@Data
public class UploadResult {
    private long fsize;
    private String key;
    private String hash;
    private int width;
    private int height;
}
