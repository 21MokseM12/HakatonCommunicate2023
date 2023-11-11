package service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static service.ParserDadata.parseInformation;

public class ApiVK {
    public static void getAddress() {
        String apiKey = "06d9241906d9241906d92419ac05cf603c006d906d924196396ceffbfc03c89911e4c96";

        // Заменить на необходимый метод VK API и параметры запроса
        String apiUrl = "https://api.vk.com/method/users.get?user_ids=davinchi4&fields=bdate,city&access_token=" + apiKey + "&v=5.131";
        URI uri = URI.create(apiUrl);

        try {
            // Создаем HTTP-клиент
            HttpClient client = HttpClient.newHttpClient();

            // Создаем GET-запрос
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .GET()
                    .build();

            // Отправляем запрос и получаем ответ
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Выводим результат
            System.out.println(response.body());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
