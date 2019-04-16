package cn.goktech.dolphin.concurrent.lock.exception;

/**
 * @author funcas
 * @version 1.0
 * @date 2019年04月16日
 */
public class LockException extends RuntimeException{

    public LockException() {
        super();
    }

    public LockException(String message) {
        super(message);
    }

    public LockException(Throwable cause) {
        super(cause);
    }

    public LockException(String message, Throwable cause) {
        super(message, cause);
    }
}
