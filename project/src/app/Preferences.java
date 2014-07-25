package app;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {
	
	private final static String PREF_FILE = "settings";
	private final static String KEY_MESSAGE = "message";
	
	private final static String DEFAULT_MESSAGE = "Sample response message";
	
	private static SharedPreferences sharedPreferences = null;
	
	
	public static String getResponseMessage(Context context) {
		return getSharedPreferences(context).getString(KEY_MESSAGE, DEFAULT_MESSAGE);
	}
	
	
	public static void setResponseMessage(Context context, String message) {
		getSharedPreferences(context).edit().putString(KEY_MESSAGE, message).commit();
	}
	
	private static SharedPreferences getSharedPreferences(Context context) {
		
		if (sharedPreferences == null) {
			sharedPreferences = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
		}
		
		return sharedPreferences;
	}

}
