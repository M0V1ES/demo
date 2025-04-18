package com.example.ivanovppapi;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import org.json.JSONArray;
import org.json.JSONObject;
import org.odftoolkit.odfdom.doc.OdfTextDocument;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HelloController {

    @FXML
    private Label poluchitText;

    @FXML
    private Label otpravitText;

    @FXML
    private ListView<String> list;

    private String forbiddenChar = null;
    public int count = 0;

    @FXML
    protected void PoluchitDannie() {
        otpravitText.setText("");
        try {
            URL url = new URL("http://localhost:4444/TransferSimulator/fullName");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                StringBuilder jsonString = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    jsonString.append(line);
                }

                JSONArray json = new JSONArray("[" + jsonString.toString() + "]");
                for (int i = 0; i < json.length(); i++) {
                    JSONObject obj = json.getJSONObject(i);
                    String fullName = obj.getString("value");
                    poluchitText.setText(fullName);

                    // Проверяем наличие запрещённых символов
                    Pattern pattern = Pattern.compile("[^А-я\\s]");
                    Matcher matcher = pattern.matcher(fullName);

                    if (matcher.find()) {
                        forbiddenChar = matcher.group(); // Сохраняем найденный символ
                        otpravitText.setText("ФИО содержит запрещенные символы: " + forbiddenChar);
                        list.getItems().add("Найден запрещенный символ: " + forbiddenChar); // Добавляем в ListView
                    } else {
                        forbiddenChar = null; // Сбрасываем, если ошибок нет
                    }
                }
            }
        } catch (IOException e) {
            otpravitText.setText("Ошибка при получении данных");
            list.getItems().add("Ошибка при получении данных: " + e.getMessage()); // Добавляем в ListView
            e.printStackTrace();
        }
    }

    @FXML
    void OtpravitDannie(ActionEvent event) {
        if (forbiddenChar == null) {
            otpravitText.setText("Нет запрещённых символов для проверки ");
            list.getItems().add("Нет запрещённых символов для проверки."); // Добавляем в ListView
            return;
        }
        try {
            // Очищаем ListView перед новой проверкой
            list.getItems().clear();

            // Загружаем документ
            OdfTextDocument odt = OdfTextDocument.loadDocument("TestCase.odt");
            String cellValue = odt.getTableByName("Таблица1").getCellByPosition(0, 2 ).getStringValue();
            String cellValue1 = odt.getTableByName("Таблица1").getCellByPosition(0, 3 ).getStringValue();
            
            // Проверяем на наличие запрещённых символов
            Pattern pattern = Pattern.compile("[^А-я\\s]");
            Matcher matcher = pattern.matcher(cellValue);
            Matcher matcher1 = pattern.matcher(cellValue1);

            if (matcher.find() && matcher1.find()) {
                String foundChar = matcher.group();
                String foundChar1 = matcher1.group();// Найденный запрещённый символ

                otpravitText.setText("ФИО не содержит запрещенные символы: " + foundChar + "\n" + "ФИО не содержит запрещенные символы: " + foundChar1);
                  if (forbiddenChar.equals(foundChar)) {
                    list.getItems().add("Успешно!");
                    odt.getTableByName("Таблица1").getCellByPosition(2, 2).setStringValue("Успешно");
                    otpravitText.setText("ФИО содержит запрещенные символы: " + foundChar + "\n" + "ФИО не содержит запрещенные символы: " + foundChar1);
                }  else {
                      odt.getTableByName("Таблица1").getCellByPosition(2, 2).setStringValue("Успешно");
                  }
                if (forbiddenChar.equals(foundChar1)) {
                    list.getItems().add("Успешно!");
                    odt.getTableByName("Таблица1").getCellByPosition(2, 3).setStringValue("Успешно");
                    otpravitText.setText("ФИО не содержит запрещенные символы: " + foundChar + "\n" + "ФИО содержит запрещенные символы: " + foundChar1);
                } else {
                    odt.getTableByName("Таблица1").getCellByPosition(2, 3).setStringValue("Успешно");
                }
                // Сохраняем документ только если найдены запрещённые символы
                odt.save("TestCase.odt");
            } else {
                // Если запрещённые символы не найдены, просто добавляем сообщение в ListView
                list.getItems().add("Не успешно! Запрещённый символ не найден."); // Добавляем в ListView
            }


        } catch (Exception e) {
            otpravitText.setText("Ошибка при обработке файла");
            list.getItems().add("Ошибка при обработке файла: " + e.getMessage()); // Добавляем в ListView
            e.printStackTrace();
        }


    }
}