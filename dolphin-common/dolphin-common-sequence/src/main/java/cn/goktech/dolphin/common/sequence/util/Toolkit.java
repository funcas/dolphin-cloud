package cn.goktech.dolphin.common.sequence.util;

import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;

/**
 * @author funcas
 * @version 1.0
 * @date 2019年04月08日
 */
@Slf4j
public class Toolkit {

    public static String getIp() {
        String ip;
        try {
            InetAddress addr = InetAddress.getLocalHost();
            ip = addr.getHostAddress();
        } catch(Exception ex) {
            ip = "";
            log.warn("Utils get IP warn", ex);
        }
        return ip;
    }

//    public static <T> T getProps(String fileName, Class<T> cls) {
//        InputStream fileInputStream = null;
//        try {
//            Yaml yaml = new Yaml();
//            fileInputStream = Toolkit.class.getClassLoader().getResourceAsStream(fileName);
//            return yaml.loadAs(fileInputStream, cls);
//        }catch(Exception e) {
//            log.error("文件地址错误");
//            e.printStackTrace();
//        }finally {
//            try {
//                if(fileInputStream!=null) {
//                    fileInputStream.close();
//                }
//            }catch (IOException e){
//                e.printStackTrace();
//            }
//        }
//        return null;
//    }
//
//    public static <T> T getProps(Class<T> cls) {
//        return getProps("application.yml", cls);
//    }
}
