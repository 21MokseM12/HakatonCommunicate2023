package service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;
import static service.ParserVK.parseInformationVKClient;
import static service.ParserVK.parseInformationVKPublic;

public class ApiVK {
    public static void getAddress() {

        String apiUrl = null;
        int variant = 0;

        Scanner scanner = new Scanner(System.in);
            System.out.print("Какую информацию вы хотите получить?\n1)Про паблик\n2)Про аккаунт\n");
            variant = Integer.parseInt(scanner.nextLine());

            String apiKey = "06d9241906d9241906d92419ac05cf603c006d906d924196396ceffbfc03c89911e4c96";

            // Заменить на необходимый метод VK API и параметры запроса

            if (variant == 1) {
                System.out.print("Введите ссылку паблика: ");
                String groupName = scanner.nextLine();
                int lastSlashIndex = groupName.lastIndexOf("/");
                String groupId = groupName.substring(lastSlashIndex + 1);

                apiUrl = "https://api.vk.com/method/groups.getById?group_id=" + groupId + "&fields=description,members_count&access_token=" + apiKey + "&v=5.131";

            } else if (variant == 2) {
                System.out.print("Введите ссылку профиля: ");
                String groupName = scanner.nextLine();
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
                parseInformationVKPublic(response);
            }else if (variant == 2){
                parseInformationVKClient(response);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
