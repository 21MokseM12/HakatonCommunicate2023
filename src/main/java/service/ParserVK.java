package service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.http.HttpResponse;

public class ParserVK {
    public static String[] parseInformationVKClient(HttpResponse<String> response) {
        String[] array = new String[3];
        try {
            // Создаем ObjectMapper, который поможет нам работать с JSON
            ObjectMapper objectMapper = new ObjectMapper();

            // Преобразуем строку с JSON в объект JsonNode
            JsonNode jsonNode = objectMapper.readTree(response.body());

            // Извлекаем нужные данные
            String firstName = jsonNode.get("response").get(0).get("first_name").asText();
            String lastName = jsonNode.get("response").get(0).get("last_name").asText();
            String city = jsonNode.get("response").get(0).get("city").get("title").asText();
          //  String birthDate = jsonNode.get("response").get(0).get("bdate").asText();

            // Выводим полученные данные
//            System.out.println("Имя: " + firstName);
//            System.out.println("Фамилия: " + lastName);
//            System.out.println("Город: " + city);
          //  System.out.println("Дата рождения: " + birthDate);
            array[0] = firstName;
            array[1] = lastName;
            array[2] = city;

            return array;

        } catch (NullPointerException e){
            array[0] = "Некорректная ссылка";
            return array;
        }
        catch (Exception e) {
            e.printStackTrace();
            return array;
        }
    }

    public static String[] parseInformationVKPublic(HttpResponse<String> response){
        String[] array = new String[3];
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            // Преобразуем строку с JSON в объект JsonNode
            JsonNode jsonNode = objectMapper.readTree(response.body());

            // Извлекаем нужные данные
            String groupName = jsonNode.get("response").get(0).get("name").asText();
            String groupDescription = jsonNode.get("response").get(0).get("description").asText();
            int membersCount = jsonNode.get("response").get(0).get("members_count").asInt();

            // Выводим полученные данные
            if (groupDescription.equals("")){
                groupDescription = "нет описания";
            }


            array[0] = groupName;
            array[1] = groupDescription;
            array[2] = Integer.toString(membersCount);

            return array;

//            System.out.println("Название сообщества: " + groupName);
//            System.out.println("Описание сообщества: " + groupDescription);
//            System.out.println("Количество участников: " + membersCount);

        } catch (NullPointerException e){
            array[0] = "Некорректная ссылка";
            return array;
        }
        catch (Exception e) {
            e.printStackTrace();
            return array;
        }
    }
}
