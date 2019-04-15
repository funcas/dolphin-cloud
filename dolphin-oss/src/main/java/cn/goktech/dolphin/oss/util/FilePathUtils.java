package cn.goktech.dolphin.oss.util;

/**
 * @author funcas
 * @version 1.0
 * @date 2019年04月12日
 */
public class FilePathUtils {

    public static final String HTTP_PREFIX = "http://";
    public static final String HTTPS_PREFIX = "https://";
    public static final String DIR_SPLITER = "/";

    public static String parseFileExtension(String filePath){
        if(filePath.contains("/")){
            filePath = filePath.substring(filePath.lastIndexOf("/"));
        }
        filePath = filePath.split("\\?")[0];
        if(filePath.contains(".")){
            return filePath.substring(filePath.lastIndexOf(".") + 1);
        }
        return null;
    }

    public static String  parseFileName(String filePath){
        filePath = filePath.split("\\?")[0];
        int index = filePath.lastIndexOf("/") + 1;
        if(index > 0){
            return filePath.substring(index);
        }
        return filePath;
    }

    public static void main(String[] args) {
        System.out.println(parseFileExtension("http:www.ssss.com/cccc/123.png?xxx"));
        System.out.println(parseFileExtension("123.png"));
        System.out.println(parseFileExtension("http:www.ssss.com/cccc/dtgh4r4tt/"));

        System.out.println(parseFileName("http:www.ssss.com/cccc/123.png?cfg"));
    }
}
