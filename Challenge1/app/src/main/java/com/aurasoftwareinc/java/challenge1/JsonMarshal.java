package com.aurasoftwareinc.java.challenge1;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * The JsonMarshal class.
 */
public class JsonMarshal {

    //Object Types for marshalling.
    private static List objectTypes = Arrays.asList(Byte.class, Short.class, Integer.class,
            Long.class, Float.class, Double.class, Boolean.class, String.class);

    /**
     * Marshal json objects, objects or classes with implement JsonMarshalInterface.
     *
     * @param object the object
     * @return the marshalled json object
     */
    public static JSONObject marshalJSON(Object object) {
        JSONObject json = new JSONObject();

        try {
            //
            // Every field is marshalling with loop
            //
            for (Field field : object.getClass().getDeclaredFields()) {
                boolean isAccessible = field.isAccessible();
                field.setAccessible(Boolean.TRUE);
                Class<?> fieldType = field.getType();
                Object fieldObject = field.get(object);
                String fieldName = field.getName();
                //
                // No marshalling for null objects
                //
                if (fieldObject == null) continue;

                if (isJsonDataType(fieldType)) {
                    json.put(fieldName, fieldObject);
                } else if (fieldType.isArray()) {
                    List<Object> fieldObjectList = marshalArray(fieldObject);
                    json.put(fieldName, new JSONArray(fieldObjectList));
                } else if (JsonMarshalInterface.class.isAssignableFrom(fieldType)) {
                    //
                    // If the object implements from JsonMarshalInterface;
                    //
                    json.put(fieldName, ((JsonMarshalInterface) fieldObject)
                            .marshalJSON());
                }
                field.setAccessible(isAccessible);
            }
        } catch (Exception ignore) {
            ignore.printStackTrace();
        }

        return json;
    }

    /**
     * Unmarshal and set json objects fields via JsonMarshalInterface.
     *
     * @param object the object
     * @param json   the json
     * @return the boolean
     */
    public static boolean unmarshalJSON(Object object, JSONObject json) {

        try {
            for (Field field : object.getClass().getDeclaredFields()) {
                boolean isAccessible = field.isAccessible();
                field.setAccessible(Boolean.TRUE);
                Class<?> fieldType = field.getType();
                String fieldName = field.getName();
                //
                // No unmarshalling for objects without fields
                //
                if (!json.has(fieldName))
                    continue;

                if (isJsonDataType(fieldType)) {
                    field.set(object, json.get(fieldName));
                } else if (fieldType.isArray()) {
                    JSONArray jsonArray = json.getJSONArray(fieldName);
                    Object unMarshalArray = unmarshalArray(fieldType.getComponentType(), jsonArray);
                    field.set(object, unMarshalArray);
                } else if (JsonMarshalInterface.class.isAssignableFrom(fieldType)) {
                    //
                    // If the object implements from JsonMarshalInterface;
                    //
                    Object newInstance = fieldType.newInstance();
                    ((JsonMarshalInterface) newInstance).unmarshalJSON((JSONObject) json.get(fieldName));
                    field.set(object, newInstance);
                }
                field.setAccessible(isAccessible);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    /**
     * This boolean is check for the objClass is marshaled to JSON without any implementation or not.
     *
     * @param objClassType the object class type for marshaling
     * @return the isJsonDataType
     */
    private static boolean isJsonDataType(Class objClassType) {
        return objectTypes.contains(objClassType)
                || Objects.equals(objClassType.getName(), String.class.getName())
                || Objects.equals(objClassType.getName(), JSONObject.class.getName())
                || Objects.equals(objClassType.getName(), JSONArray.class.getName())
                || objClassType.isPrimitive();
    }


    /**
     * For return the recursively marshal array list items.
     *
     * @param array the object array for marshaling
     * @return the object list
     */
    private static List<Object> marshalArray(Object array) {
        List<Object> objectList = new ArrayList<>();
        int i = 0;
        while (i < Array.getLength(array)) {
            Object arrayObject = Array.get(array, i);
            if (arrayObject != null) {
                if (isJsonDataType(arrayObject.getClass())) {
                    objectList.add(arrayObject);
                } else if (arrayObject.getClass().isArray()) {
                    //
                    // Marshalling recursively if the object is array
                    //
                    objectList.add(marshalArray(arrayObject));
                } else if (arrayObject instanceof JsonMarshalInterface) {
                    //
                    // If the object implements from JsonMarshalInterface;
                    //
                    objectList.add(((JsonMarshalInterface) arrayObject).marshalJSON());
                }
            }
            i++;
        }
        return objectList;
    }


    /**
     * For return the recursively unmarshal array list items.
     *
     * @param type      the type
     * @param jsonArray the json array
     * @return the object
     */
    private static Object unmarshalArray(Class<?> type, JSONArray jsonArray) {
        Object array = Array.newInstance(type, jsonArray.length());
        try {
            int i = 0;
            while (i < jsonArray.length()) {
                Object jsonElement = jsonArray.get(i);
                if (jsonElement instanceof JSONArray) {
                    //
                    // Unmarshalling recursively if the object is array
                    //
                    Array.set(array, i, unmarshalArray(type, (JSONArray) jsonElement));
                } else if (isJsonDataType(type)) {
                    Array.set(array, i, jsonElement);
                } else if (JsonMarshalInterface.class.isAssignableFrom(type)) {
                    //
                    // If the object implements from JsonMarshalInterface;
                    //
                    JsonMarshalInterface jsonMarshalInterface = (JsonMarshalInterface) type.newInstance();
                    jsonMarshalInterface.unmarshalJSON((JSONObject) jsonElement);
                    Array.set(array, i, jsonMarshalInterface);
                }
                i++;
            }
            return array;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}