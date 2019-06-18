package common.tau;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.text.DateFormat;
import java.util.Iterator;

public class JsonUtil {

    public static JsonNode findChildObjectWithAttributeAndValue(JsonNode parent, String attributeName, String value) {
        for (Iterator<JsonNode> i = parent.elements(); i.hasNext(); ) {
            JsonNode node = i.next();
            if (node.has(attributeName) && node.get(attributeName).asText().equals(value)) {
                return node;
            }
        }
        return null;
    }

    public static ObjectMapper getJsonParser() {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper;
    }
}
