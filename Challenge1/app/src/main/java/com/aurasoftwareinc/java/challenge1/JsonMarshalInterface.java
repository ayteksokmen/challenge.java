package com.aurasoftwareinc.java.challenge1;

import org.json.JSONObject;

public interface JsonMarshalInterface {
    JSONObject marshalJSON();

    boolean unmarshalJSON(JSONObject json);
}
