package service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.http.HttpResponse;


public class ParserDadata {
    public static void parseInformation(HttpResponse<String> response) {
        try {
                String responseBody = response.body();
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(responseBody);

            JsonNode suggestions = jsonNode.path("suggestions");
            ExcelDataOutput excel = ExcelDataOutput.getInstance();

            for (int i = 0; i < suggestions.size(); i++) {
                String city = jsonNode.path("suggestions").path(i).path("data").path("city").asText();
                String street = jsonNode.path("suggestions").path(i).path("data").path("street").asText();
                String houseNumber = jsonNode.path("suggestions").path(i).path("data").path("house").asText();
                String postalCode = jsonNode.path("suggestions").path(i).path("data").path("postal_code").asText();

                excel.write(postalCode, city, street, houseNumber);
            }

            excel.save();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
