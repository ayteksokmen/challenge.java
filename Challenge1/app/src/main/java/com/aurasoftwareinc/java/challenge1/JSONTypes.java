package com.aurasoftwareinc.java.challenge1;

import org.json.JSONArray;
import org.json.JSONObject;

public class JSONTypes implements JsonMarshalInterface {
    public JSONObject jsonObject;
    private JSONArray jsonArray;

    public void populateTestData() {
        try {
            jsonObject = new JSONObject();

            jsonObject.put("jsonFloat", 42.5f);
            jsonObject.put("jsonDouble", 42.123456789012345);
            jsonObject.put("jsonInt", 47114711);
            jsonObject.put("jsonLong", 12345678901234567L);
            jsonObject.put("jsonString", "hello");
            jsonObject.put("jsonBoolean", true);

            jsonArray = new JSONArray();

            jsonArray.put("a");
            jsonArray.put("b");
            jsonArray.put("c");
            jsonArray.put("d");
            jsonArray.put("e");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public JSONObject marshalJSON() {
        return JsonMarshal.marshalJSON(this);
    }

    @Override
    public boolean unmarshalJSON(JSONObject json) {
        return JsonMarshal.unmarshalJSON(this, json);
    }
}
