package utils;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

public class DataDriven {

    public static JSONObject jsonReader(String key) {
        JSONParser parser = new JSONParser();
        try {
            FileReader reader = new FileReader("testData/testData.json");
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            return (JSONObject) jsonObject.get(key);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
