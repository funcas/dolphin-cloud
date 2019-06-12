package cn.goktech.dolphin.common.exception;

/**
 * @author funcas
 * @version 1.0
 * @date 2019年06月12日
 */
public class SystemException extends ServiceException{


    public SystemException() {
        super();
    }


    public SystemException(String message) {
        super(message);
    }


    public SystemException(Throwable cause) {
        super(cause);
    }


    public SystemException(String message, Throwable cause) {
        super(message, cause);
    }
}
