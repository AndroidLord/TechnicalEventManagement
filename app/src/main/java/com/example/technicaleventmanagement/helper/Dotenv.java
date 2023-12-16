package com.example.technicaleventmanagement.helper;

import android.content.Context;
import android.content.res.AssetManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class Dotenv {

    private final Map<String, String> values;

    public Dotenv(Context context) {
        this.values = loadValues(context);
    }

    private Map<String, String> loadValues(Context context) {
        Map<String, String> result = new HashMap<>();
        try {
            AssetManager assetManager = context.getAssets();
            InputStream inputStream = assetManager.open(".env");

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=");
                if (parts.length >= 2) {
                    result.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String get(String key) {
        return values.get(key);
    }
}

