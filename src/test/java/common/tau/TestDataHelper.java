package common.tau;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.stream.Collectors;




public class TestDataHelper {


    /**
     * @param filePath   the path to the file you want to load relative to the modules resource root.
     * @param parameters a map of Strings, the key represents the data you want to replace with the actual value.
     *                   The parameters have to be surrounded with '%' in the resource file.
     * @return A String containing the given file with the replaced values
     */
    public static String getParametrizedTextFromFile(String filePath, Map<String, String> parameters) {
        InputStream is = ClassLoader.getSystemResourceAsStream(filePath);
        BufferedReader bf = new BufferedReader(new InputStreamReader(is));
        String message = bf.lines().collect(Collectors.joining(""));
        for (String parameterName : parameters.keySet()) {
            message = message.replace("%" + parameterName + "%", parameters.get(parameterName));
        }
        return message;
    }
}
