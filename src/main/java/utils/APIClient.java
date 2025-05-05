package utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class APIClient {

    public String get(String urlStr) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        return readResponse(conn);
    }

    public String post(String urlStr, String jsonBody) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; utf-8");
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        return readResponse(conn);
    }

    public String put(String urlStr, String jsonBody) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("PUT");
        conn.setRequestProperty("Content-Type", "application/json; utf-8");
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        return readResponse(conn);
    }

    public String delete(String urlStr) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("DELETE");

        return readResponse(conn);
    }

    private String readResponse(HttpURLConnection conn) throws Exception {
        BufferedReader in = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8)
        );
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();

        return content.toString();
    }

    public List<String> splitJsonObjects(String jsonArrayContent) {
        List<String> objects = new ArrayList<>();
        int openBraces = 0;
        int startPos = 0;

        for (int i = 0; i < jsonArrayContent.length(); i++) {
            char c = jsonArrayContent.charAt(i);

            if (c == '{') {
                if (openBraces == 0) {
                    startPos = i;
                }
                openBraces++;
            } else if (c == '}') {
                openBraces--;
                if (openBraces == 0) {
                    objects.add(jsonArrayContent.substring(startPos, i + 1));
                }
            }
        }

        return objects;
    }

    public Map<String, String> parseJsonObject(String jsonObject) {
        Map<String, String> map = new HashMap<>();

        // Remove the outer braces
        jsonObject = jsonObject.trim();
        if (jsonObject.startsWith("{")) {
            jsonObject = jsonObject.substring(1);
        }
        if (jsonObject.endsWith("}")) {
            jsonObject = jsonObject.substring(0, jsonObject.length() - 1);
        }

        // Pattern to extract key-value pairs
        Pattern pattern = Pattern.compile("\"(.*?)\"\\s*:\\s*(\"(.*?)\"|[0-9]+)");
        Matcher matcher = pattern.matcher(jsonObject);

        while (matcher.find()) {
            String key = matcher.group(1);
            String value = matcher.group(2);

            // Remove quotes from string values
            if (value.startsWith("\"") && value.endsWith("\"")) {
                value = value.substring(1, value.length() - 1);
            }

            map.put(key, value);
        }

        return map;
    }
}
