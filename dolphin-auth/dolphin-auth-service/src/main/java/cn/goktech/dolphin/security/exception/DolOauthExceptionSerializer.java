package cn.goktech.dolphin.security.exception;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

/**
 * @author funcas
 * @version 1.0
 * @date 2019年06月12日
 */
public class DolOauthExceptionSerializer extends StdSerializer<DolOauthException> {

    protected DolOauthExceptionSerializer() {
        super(DolOauthException.class);
    }

    @Override
    public void serialize(DolOauthException e, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("retCode", String.valueOf(e.getHttpErrorCode()));
        jsonGenerator.writeStringField("retMessage", e.getMessage());
        jsonGenerator.writeEndObject();
    }
}
