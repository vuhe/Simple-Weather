package com.simpleweather.android.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.simpleweather.android.MyApplication;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SpUtils {

    public static String PREFERENCE_NAME = "SimpleApp";
    private static Context context = MyApplication.getContext();

    /**
     * 清空统计数据
     */
    public static void clearSp() {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.clear().commit();
    }

    /**
     * 写入String类型存储
     * @param key 键入条目
     * @param value 存入的值
     * @return 如果新值已成功写入存储，返回True
     */
    public static boolean putString(String key, String value) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        return editor.commit();
    }

    /**
     * 读取String类型存储
     * @param key 键值
     * @return 读取的数据
     * @see #getString(String, String)
     */
    public static String getString(String key) {
        return getString(key, null);
    }

    /**
     * 读取String类型存储
     * @param key 键值
     * @param defaultValue 读取失败默认返回值
     * @return 如果读取成功则返回存储的值，否则返回defaultValue
     */
    public static String getString(String key, String defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME,
                Context.MODE_PRIVATE);
        return settings.getString(key, defaultValue);
    }

    /**
     * 写入int类型存储
     * @param key 键入条目
     * @param value 存入的值
     * @return 如果新值已成功写入存储，返回True
     */
    public static boolean putInt(String key, int value) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(key, value);
        return editor.commit();
    }

    /**
     * 读取int类型存储
     * @param key 键值
     * @return 读取的数据
     * @see #getInt(String, int)
     */
    public static int getInt(String key) {
        return getInt(key, -1);
    }

    /**
     * 读取int类型存储
     * @param key 键值
     * @param defaultValue 读取失败默认返回值
     * @return 如果读取成功则返回存储的值，否则返回defaultValue
     */
    public static int getInt(String key, int defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME,
                Context.MODE_PRIVATE);
        return settings.getInt(key, defaultValue);
    }

    /**
     * 写入long类型存储
     * @param key 键入条目
     * @param value 存入的值
     * @return 如果新值已成功写入存储，返回True
     */
    public static boolean putLong(String key, long value) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(key, value);
        return editor.commit();
    }

    /**
     * 读取long类型存储
     * @param key 键值
     * @return 读取的数据
     * @see #getLong(String, long)
     */
    public static long getLong(String key) {
        return getLong(key, -1);
    }

    /**
     * 读取long类型存储
     * @param key 键值
     * @param defaultValue 读取失败默认返回值
     * @return 如果读取成功则返回存储的值，否则返回defaultValue
     */
    public static long getLong(String key, long defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME,
                Context.MODE_PRIVATE);
        return settings.getLong(key, defaultValue);
    }

    /**
     * 写入float类型存储
     * @param key 键入条目
     * @param value 存入的值
     * @return 如果新值已成功写入存储，返回True
     */
    public static boolean putFloat(String key, float value) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putFloat(key, value);
        return editor.commit();
    }

    /**
     * 读取float类型存储
     * @param key 键值
     * @return 读取的数据
     * @see #getFloat(String, float)
     */
    public static float getFloat(String key) {
        return getFloat(key, -1);
    }

    /**
     * 读取float类型存储
     * @param key 键值
     * @param defaultValue 读取失败默认返回值
     * @return 如果读取成功则返回存储的值，否则返回defaultValue
     */
    public static float getFloat(String key, float defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME,
                Context.MODE_PRIVATE);
        return settings.getFloat(key, defaultValue);
    }

    /**
     * 写入boolean类型存储
     * @param key 键入条目
     * @param value 存入的值
     * @return 如果新值已成功写入存储，返回True
     */
    public static boolean putBoolean(String key, boolean value) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key, value);
        return editor.commit();
    }

    /**
     * 读取boolean类型存储
     * @param key 键值
     * @return 读取的数据
     * @see #getBoolean(String, boolean)
     */
    public static boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    /**
     * 读取boolean类型存储
     * @param key 键值
     * @param defaultValue 读取失败默认返回值
     * @return 如果读取成功则返回存储的值，否则返回defaultValue
     */
    public static boolean getBoolean(String key, boolean defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME,
                Context.MODE_PRIVATE);
        return settings.getBoolean(key, defaultValue);
    }

    public static String SceneList2String(HashMap<String, Integer> hashmap)
            throws IOException {
        // 实例化一个ByteArrayOutputStream对象，用来装载压缩后的字节文件。
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        // 然后将得到的字符数据装载到ObjectOutputStream
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                byteArrayOutputStream);
        // writeObject 方法负责写入特定类的对象的状态，以便相应的 readObject 方法可以还原它
        objectOutputStream.writeObject(hashmap);
        // 最后，用Base64.encode将字节文件转换成Base64编码保存在String中
        String SceneListString = new String(Base64.encode(
                byteArrayOutputStream.toByteArray(), Base64.DEFAULT));
        // 关闭objectOutputStream
        objectOutputStream.close();
        return SceneListString;
    }

    @SuppressWarnings("unchecked")
    public static HashMap<String, Integer> String2SceneList(
            String SceneListString) throws StreamCorruptedException,
            IOException, ClassNotFoundException {
        byte[] mobileBytes = Base64.decode(SceneListString.getBytes(),
                Base64.DEFAULT);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                mobileBytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(
                byteArrayInputStream);
        HashMap<String, Integer> SceneList = (HashMap<String, Integer>) objectInputStream
                .readObject();
        objectInputStream.close();
        return SceneList;
    }

    public static boolean putHashMap(String key, HashMap<String, Integer> hashmap) {
        SharedPreferences settings = context.getSharedPreferences(
                PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        try {
            String listStr = SceneList2String(hashmap);
            editor.putString(key, listStr);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return editor.commit();
    }

    public static HashMap<String, Integer> getHashMap(String key) {
        SharedPreferences settings = context.getSharedPreferences(
                PREFERENCE_NAME, Context.MODE_PRIVATE);
        String listStr = settings.getString(key, "");
        try {
            return String2SceneList(listStr);
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 保存List<String>
     * @param key 键入条目
     * @param values 存入的值
     * @return 如果新值已成功写入存储，返回True
     */
    public static boolean putStringList(String key, List<String> values) {
        SharedPreferences.Editor edit = context.getSharedPreferences(
                PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
        edit.putInt(key, values.size());
        for (int i = 0; i < values.size(); i++) {
            edit.putString(key + i, values.get(i));
        }
        return edit.commit();
    }

    /**
     * 获取List<String>
     * @param key 键值
     * @return 读取的数据
     */
    public static List<String> getStringList(String key) {
        List<String> values = new ArrayList<>();
        SharedPreferences sp = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        int listSize = sp.getInt(key, 0);
        for (int i = 0; i < listSize; i++) {
            values.add(sp.getString(key + i, null));
        }
        return values;
    }

    /**
     * 保存List<Integer>
     * @param key 键入条目
     * @param values 存入的值
     * @return 如果新值已成功写入存储，返回True
     */
    public static boolean putIntList(String key, List<Integer> values) {
        SharedPreferences.Editor edit = context.getSharedPreferences(
                PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
        edit.putInt(key, values.size());
        for (int i = 0; i < values.size(); i++) {
            edit.putInt(key + i, values.get(i));
        }
        return edit.commit();
    }

    /**
     * 获取List<Integer>
     * @param key 键值
     * @return 读取的数据
     */
    public static List<Integer> getIntList(String key) {
        List<Integer> values = new ArrayList<>();
        SharedPreferences sp = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        int listSize = sp.getInt(key, 0);
        for (int i = 0; i < listSize; i++) {
            values.add(sp.getInt(key + i, 0));
        }
        return values;
    }

    /**
     * 保存List<Long>
     * @param key 键入条目
     * @param values 存入的值
     * @return 如果新值已成功写入存储，返回True
     */
    public static boolean putLongList(String key, List<Long> values) {
        SharedPreferences.Editor edit = context.getSharedPreferences(
                PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
        edit.putInt(key, values.size());
        for (int i = 0; i < values.size(); i++) {
            edit.putLong(key + i, values.get(i));
        }
        return edit.commit();
    }

    /**
     * 获取List<Long>
     * @param key 键值
     * @return 读取的数据
     */
    public static List<Long> getLongList(String key) {
        List<Long> values = new ArrayList<>();
        SharedPreferences sp = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        int listSize = sp.getInt(key, 0);
        for (int i = 0; i < listSize; i++) {
            values.add(sp.getLong(key + i, 0));
        }
        return values;
    }

    /**
     * 保存List<Object>
     * @param key 键入条目
     * @param datalist 存入的值
     * @return 如果新值已成功写入存储，返回True
     */
    public <T> void putListBean(String key, List<T> datalist) {
        SharedPreferences.Editor edit = context.getSharedPreferences(
                PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
        if (null == datalist || datalist.size() <= 0) {
            return;
        }
        Gson gson = new Gson();
        //转换成json数据，再保存
        String strJson = gson.toJson(datalist);
        edit.putString(key, strJson);
        edit.commit();
    }

    /**
     * 获取List<Object>
     * @param key 键值
     * @return 读取的数据listbean
     */
    public <T> List<T> getListBean(String key) {
        SharedPreferences sp = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        List<T> dataList = new ArrayList<T>();
        String strJson = sp.getString(key, null);
        if (null == strJson) {
            return dataList;
        }
        Gson gson = new Gson();
        dataList = gson.fromJson(strJson, new TypeToken<List<T>>() {
        }.getType());
        return dataList;
    }

    /**
     * 存放实体类以及任意类型
     * @param key 键入条目
     * @param obj 存入的类
     */
    public static void saveBean(String key, Object obj) {
        SharedPreferences.Editor editor = context
                .getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
        Gson gson = new Gson();
        String objString = gson.toJson(obj);
        editor.putString(key, objString).commit();
    }

    /**
     * 获取实体类
     * @param key 键值
     * @param clazz 这里传入一个类就是我们所需要的实体类(obj)
     * @return 返回我们封装好的该实体类(obj)
     */
    public static <T> T getBean(String key, Class<T> clazz) {
        String objString = context
                .getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
                .getString(key, "");
        Gson gson = new Gson();
        return gson.fromJson(objString, clazz);
    }

}
