package cn.goktech.dolphin.oss.exception;

/**
 * @author funcas
 * @version 1.0
 * @date 2019年04月12日
 */
public class OssOperErrorException extends RuntimeException {

    private static final long serialVersionUID = 5809777201458417996L;

    public OssOperErrorException(String message) {
        super(message);
    }

    public OssOperErrorException(Throwable cause) {
        super(cause);
    }

    public OssOperErrorException(String message, Throwable cause) {
        super(message, cause);
    }


}
