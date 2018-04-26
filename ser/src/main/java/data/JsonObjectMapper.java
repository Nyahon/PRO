package data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * This class provides utility methods to convert Plain Old Java Objects (POJOs)
 * into their json representation, and vice-versa. It relies on the jackson
 * library.
 *
 * @author Olivier Liechti
 */
public class JsonObjectMapper {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Converts a json string into a POJO of the specified class
     *
     * @param <T> the class that we want to instantiate
     * @param json the json representation of the object
     * @param type the class to instantiate
     * @return an instance of T, which value corresponds to the json string
     * @throws IOException
     */
    public static <T> T parseJson(String json, Class<T> type) throws IOException {
        return objectMapper.readValue(json, type);
    }

    /**
     * Converts a POJO into its json representation
     *
     * @param o the object to serialize
     * @return the json representation of o
     * @throws JsonProcessingException
     */
    public static String toJson(Object o) throws JsonProcessingException {
        return objectMapper.writeValueAsString(o);
    }
    /*
    public static <T> String toJsonList(List<T> l) throws JsonProcessingException{
        return objectMapper.writeValueAsString(l);
    }
*/

    /**
     * Converts a json string into a list of POJO of the specified class
     * @param json
     * @param type
     * @param <T>
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     *
     * https://stackoverflow.com/questions/6349421/how-to-use-jackson-to-deserialise-an-array-of-objects#6349488
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> parseJsonArray(String json, Class<T> type) throws IOException, ClassNotFoundException {
        ObjectMapper mapper = new ObjectMapper();
        Class<T[]> arrayClass = (Class<T[]>) Class.forName("[L" + type.getName() + ";");
        T[] objects = mapper.readValue(json, arrayClass);
        return Arrays.asList(objects);
    }
}
