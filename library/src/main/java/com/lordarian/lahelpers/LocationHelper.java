package com.lordarian.lahelpers;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Iterator;

public class LocationHelper {

    public static JSONObject cities;
    public static JSONObject capitals;
    private static Context context;

    public LocationHelper(Context context){
        LocationHelper.context = context;
        readCities();
        readCapitals();
    }

    private void readCapitals() {
        InputStream is = null;
        try {
            is = context.getAssets().open("jsons/markaz.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String myJson = new String(buffer, "UTF-8");
            LocationHelper.capitals = new JSONObject(myJson);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void readCities() {
        InputStream is = null;
        try {
            is = context.getAssets().open("jsons/cities.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String myJson = new String(buffer, "UTF-8");
            LocationHelper.cities = new JSONObject(myJson);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static String[] getProvinces(){
        String[] provinces = new String[cities.length()];
        int i = 0;
        for (Iterator<String> it = cities.keys(); it.hasNext(); ) {
            String p = it.next();
            provinces[i] = p;
            i++;
        }
        Arrays.sort(provinces);
        return provinces;
    }

    public static String[] getCities(String province) {
        try {
            JSONArray citiess = cities.getJSONArray(province);
            String[] array = new String[citiess.length()];
            for (int i = 0; i < array.length; i++)
                array[i] = citiess.getString(i);
            Arrays.sort(array);
            return array;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getCapital(String province){
        try {
            return capitals.getString(province);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getDefaultCity(){
        return "تهران";
    }
}