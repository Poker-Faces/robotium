package com.sunnymum.utils;

import java.util.Properties;

import android.content.Context;

public class ReadFile {
	public static String getPropertiesURL(Context c, String s) {
		String url = null;
		Properties properties = new Properties();
		try {
			properties.load(c.getAssets().open("property.properties"));
			url = properties.getProperty(s);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return url;
	}
}
