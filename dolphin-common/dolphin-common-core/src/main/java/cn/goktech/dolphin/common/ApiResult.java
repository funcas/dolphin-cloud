package cn.goktech.dolphin.common;


import cn.goktech.dolphin.common.enumeration.ValueEnum;

import java.io.Serializable;

/**
 * date:  2016/2/27 16:34
 *
 * @author funcas
 * @version 1.0
 */
public class ApiResult<T> implements Serializable {

    private static final long serialVersionUID = -19369146633454193L;
    private Object retCode;
    private String retMessage;
    private T result;

    public ApiResult(){}

    private ApiResult(Builder<T> builder) {
        this.retCode = builder.retCode;
        this.retMessage = builder.retMessage;
        this.result = builder.result;
    }

    public static <T>Builder<T> builder() {
        return new Builder<>();
    }

    public static class Builder<T> {
        private Object retCode;
        private String retMessage;
        private T result;

        public Builder<T> retCode(Object retCode) {
            this.retCode = retCode;
            return this;
        }

        public Builder<T> retMessage(String retMessage) {
            this.retMessage = retMessage;
            return this;
        }

        public Builder<T> result(T result) {
            this.result = result;
            return this;
        }

        public Builder<T> apiResultEnum(ValueEnum apiResultEnum) {
            this.retCode = apiResultEnum.getValue();
            this.retMessage = apiResultEnum.getName();
            return this;
        }

        public ApiResult<T> build() {
            return new ApiResult(this);
        }
    }

    public Object getRetCode() {
        return retCode;
    }

    public void setRetCode(Object retCode) {
        this.retCode = retCode;
    }

    public String getRetMessage() {
        return retMessage;
    }

    public void setRetMessage(String retMessage) {
        this.retMessage = retMessage;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "ApiResult{" +
                "retCode='" + retCode + '\'' +
                ", retMessage='" + retMessage + '\'' +
                ", result=" + result +
                '}';
    }

}
