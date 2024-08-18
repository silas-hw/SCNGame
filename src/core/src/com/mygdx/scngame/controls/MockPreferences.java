package com.mygdx.scngame.controls;

import com.badlogic.gdx.Preferences;

import java.util.HashMap;
import java.util.Map;

/**
 * Used to ock preferences without actually flushing to a persistent file
 */
public class MockPreferences implements Preferences {

    HashMap<String, Integer> integers = new HashMap<>();
    HashMap<String, Float> floats = new HashMap<>();
    HashMap<String, Boolean> booleans = new HashMap<>();
    HashMap<String, Long> longs = new HashMap<>();
    HashMap<String, String> strings = new HashMap<>();

    @Override
    public Preferences putBoolean(String key, boolean val) {
        booleans.put(key, val);
        return this;
    }

    @Override
    public Preferences putInteger(String key, int val) {
        integers.put(key, val);
        return this;
    }

    @Override
    public Preferences putLong(String key, long val) {
        longs.put(key, val);
        return this;
    }

    @Override
    public Preferences putFloat(String key, float val) {
        floats.put(key, val);
        return this;
    }

    @Override
    public Preferences putString(String key, String val) {
        strings.put(key, val);
        return this;
    }

    @Override
    public Preferences put(Map<String, ?> vals) {
        for(Map.Entry<String, ?> entry : vals.entrySet()) {
            if(entry.getValue() instanceof Integer) {
                integers.put(entry.getKey(), (Integer) entry.getValue());
            } else if(entry.getValue() instanceof Float) {
                floats.put(entry.getKey(), (Float) entry.getValue());
            } else if(entry.getValue() instanceof Boolean) {
                booleans.put(entry.getKey(), (Boolean) entry.getValue());
            } else if(entry.getValue() instanceof Long) {
                longs.put(entry.getKey(), (Long) entry.getValue());
            } else if(entry.getValue() instanceof String) {
                strings.put(entry.getKey(), (String) entry.getValue());
            }
        }

        return this;
    }

    @Override
    public boolean getBoolean(String key) {
        return booleans.get(key);
    }

    @Override
    public int getInteger(String key) {
        return integers.get(key);
    }

    @Override
    public long getLong(String key) {
        return longs.get(key);
    }

    @Override
    public float getFloat(String key) {
        return floats.get(key);
    }

    @Override
    public String getString(String key) {
        return strings.get(key);
    }

    @Override
    public boolean getBoolean(String key, boolean defValue) {
        Boolean t = booleans.get(key);
        return t != null ? t : defValue;
    }

    @Override
    public int getInteger(String key, int defValue) {
        Integer t = integers.get(key);
        return t != null ? t : defValue;
    }

    @Override
    public long getLong(String key, long defValue) {
        Long t = longs.get(key);
        return t != null ? t : defValue;
    }

    @Override
    public float getFloat(String key, float defValue) {
        Float t = floats.get(key);
        return t != null ? t : defValue;
    }

    @Override
    public String getString(String key, String defValue) {
        String t = strings.get(key);
        return t != null ? t : defValue;
    }

    @Override
    public Map<String, ?> get() {
        Map<String, Object> out = new HashMap<>();

        out.putAll(integers);
        out.putAll(floats);
        out.putAll(booleans);
        out.putAll(longs);
        out.putAll(strings);

        return out;
    }

    @Override
    public boolean contains(String key) {
        return floats.containsKey(key)
                || booleans.containsKey(key)
                || longs.containsKey(key)
                || strings.containsKey(key)
                || integers.containsKey(key);
    }

    @Override
    public void clear() {
        floats.clear();
        booleans.clear();
        longs.clear();
        strings.clear();
        integers.clear();
    }

    @Override
    public void remove(String key) {
        floats.remove(key);
        booleans.remove(key);
        longs.remove(key);
        strings.remove(key);
        integers.remove(key);
    }

    @Override
    public void flush() {}
}
