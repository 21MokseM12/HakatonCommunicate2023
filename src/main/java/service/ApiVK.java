package service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;
import static service.ParserVK.parseInformationVKClient;
import static service.ParserVK.parseInformationVKPublic;

public class ApiVK {
    public static String[] getAddress(String groupName, int variant) {
        String[] array1 = new String[3];
        String apiUrl = null;

        String apiKey = "06d9241906d9241906d92419ac05cf603c006d906d924196396ceffbfc03c89911e4c96";

        // Заменить на необходимый метод VK API и параметры запроса

        if (variant == 1) {
            int lastSlashIndex = groupName.lastIndexOf("/");
            String groupId = groupName.substring(lastSlashIndex + 1);

            apiUrl = "https://api.vk.com/method/groups.getById?group_id=" + groupId + "&fields=description,members_count&access_token=" + apiKey + "&v=5.131";

        } else if (variant == 2) {
            int lastSlashIndex = groupName.lastIndexOf("/");
            String idUser = groupName.substring(lastSlashIndex + 1);
            apiUrl = "https://api.vk.com/method/users.get?user_ids=" + idUser + "&fields=city&access_token=" + apiKey + "&v=5.131";
        }

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
            if (variant == 1){
                array1 = parseInformationVKPublic(response);
            }else if (variant == 2){
                array1 = parseInformationVKClient(response);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return array1;
    }
}
