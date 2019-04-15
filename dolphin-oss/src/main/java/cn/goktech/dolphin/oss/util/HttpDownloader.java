package cn.goktech.dolphin.oss.util;

import cn.goktech.dolphin.oss.UploadObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * @author funcas
 * @version 1.0
 * @date 2019年04月12日
 */
public class HttpDownloader {
    private static OkHttpClient httpClient = new OkHttpClient();

    public static UploadObject read(String url) throws IOException {

        Request.Builder requestBuilder = new Request.Builder().url(url);
        Response response = httpClient.newCall(requestBuilder.build()).execute();

        return new UploadObject(FilePathUtils.parseFileName(url), response.body().bytes());
    }

}
