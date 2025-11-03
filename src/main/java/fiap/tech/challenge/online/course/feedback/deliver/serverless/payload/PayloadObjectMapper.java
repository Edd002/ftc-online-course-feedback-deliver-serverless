package fiap.tech.challenge.online.course.feedback.deliver.serverless.payload;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PayloadObjectMapper {

    private static final ObjectMapper payloadObjectMapper = new ObjectMapper();

    public static String writeValueAsString(Object value) {
        try {
            return payloadObjectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T readValue(String content, Class<T> valueType) {
        try {
            return payloadObjectMapper.readValue(content, valueType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
