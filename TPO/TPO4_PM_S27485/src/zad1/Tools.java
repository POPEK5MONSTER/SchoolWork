/**
 *
 *  @author Popowski Mateusz S27485
 *
 */

package zad1;

import org.yaml.snakeyaml.Yaml;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class Tools {

    public static Options createOptionsFromYaml(String fileName) throws Exception {
        try (
                FileInputStream fileInputStream = new FileInputStream(fileName)
        ) {
            Map<String, Object> data = new Yaml().load(fileInputStream);
            Options options = mapToOptions(data);
            return options;
        }
    }

    private static Options mapToOptions(Map<String, Object> data) {
        String host = (String) data.get("host");
        int port = (int) data.get("port");
        boolean concurMode = (boolean) data.get("concurMode");
        boolean showSendRes = (boolean) data.get("showSendRes");
        Map<String, List<String>> clientsMap = (Map<String, List<String>>) data.get("clientsMap");
        return new Options(host, port, concurMode, showSendRes, clientsMap);
    }
}
