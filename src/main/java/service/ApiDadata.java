package service;

import config.DataPreparation;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class ApiDadata {
    public static void getAddress(double latitude, double longitude, int radius_meters) {
        try {
            String apiKey = "e848939afd0358185e60e769e275647144e48e9d";

            // Формируем URL для запроса
            String apiUrl = "https://suggestions.dadata.ru/suggestions/api/4_1/rs/geolocate/address";
            URI uri = URI.create(apiUrl);

            // Формируем заголовки и параметры запроса
            Map<Object, Object> requestData = new HashMap<>();
            requestData.put("lat", latitude);
            requestData.put("lon", longitude);
            requestData.put("radius_meters", radius_meters);

            // Создаем HTTP-клиент
            HttpClient client = HttpClient.newHttpClient();

            // Формируем тело запроса в формате JSON
            String requestBody = "{\"lat\":" + latitude + ", \"lon\":" + longitude + ", \"radius_meters\":" + radius_meters + "}";


            // Формируем запрос
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .header("Authorization", "Token " + apiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody, StandardCharsets.UTF_8))
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

