/**
 * 
 */
package com.instirepo.app.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class ZPreferences {

	private static final String KEY = "instirepo.prefs";

	private static final String IS_USER_LOGIN = "is_user_login";
	private static final String DEVICE_ID = "device_id";
	private static final String IS_TUTORIAL_SHOWN = "is_show_tutorial";
	private static final String APP_VERSION_NO = "app_version_no";
	private static final String USER_PROFILE_ID = "user_profile_id";
	private static final String USER_IMAGE_URL = "userimageurl";
	private static final String USER_NAME = "username";
	private static final String USER_EMAIL = "useremail";
	private static final String GCM_TOKEN = "gcm_token";
	private static final String DROPBOX_TOKEN = "DROPBOX_TOKEN";

	public static void setDropboxToken(Context context, String text) {
		Editor editor = context.getSharedPreferences(KEY, Context.MODE_PRIVATE)
				.edit();
		editor.putString(DROPBOX_TOKEN, text);
		editor.commit();
	}

	public static String getDropboxToken(Context context) {
		SharedPreferences savedSession = context.getSharedPreferences(KEY,
				Context.MODE_PRIVATE);
		return savedSession.getString(DROPBOX_TOKEN, null);
	}

	public static void setGcmToken(Context context, String text) {
		Editor editor = context.getSharedPreferences(KEY, Context.MODE_PRIVATE)
				.edit();
		editor.putString(GCM_TOKEN, text);
		editor.commit();
	}

	public static String getGcmToken(Context context) {
		SharedPreferences savedSession = context.getSharedPreferences(KEY,
				Context.MODE_PRIVATE);
		return savedSession.getString(GCM_TOKEN, null);
	}

	public static void setIsTutorialShown(Context context,
			boolean isTutorialShown) {
		Editor editor = context.getSharedPreferences(KEY, Context.MODE_PRIVATE)
				.edit();
		editor.putBoolean(IS_TUTORIAL_SHOWN, isTutorialShown);
		editor.commit();
	}

	public static boolean isTutorialShown(Context context) {
		SharedPreferences savedSession = context.getSharedPreferences(KEY,
				Context.MODE_PRIVATE);
		return savedSession.getBoolean(IS_TUTORIAL_SHOWN, false);
	}

	public static void setIsUserLogin(Context context, boolean isUserSignUp) {
		Editor editor = context.getSharedPreferences(KEY, Context.MODE_PRIVATE)
				.edit();
		editor.putBoolean(IS_USER_LOGIN, isUserSignUp);
		editor.commit();
	}

	public static boolean isUserLogIn(Context context) {

		SharedPreferences savedSession = context.getSharedPreferences(KEY,
				Context.MODE_PRIVATE);
		return savedSession.getBoolean(IS_USER_LOGIN, false);
	}

	public static String getUserProfileID(Context context) {
		SharedPreferences savedSession = context.getSharedPreferences(KEY,
				Context.MODE_PRIVATE);
		return savedSession.getString(USER_PROFILE_ID, "");
	}

	public static void setUserProfileID(Context context, String token) {
		Editor editor = context.getSharedPreferences(KEY, Context.MODE_PRIVATE)
				.edit();
		editor.putString(USER_PROFILE_ID, token);
		editor.commit();
	}

	public static String getUserImageURL(Context context) {
		SharedPreferences savedSession = context.getSharedPreferences(KEY,
				Context.MODE_PRIVATE);
		return savedSession.getString(USER_IMAGE_URL, "");
	}

	public static void setUserImageURL(Context context, String token) {
		Editor editor = context.getSharedPreferences(KEY, Context.MODE_PRIVATE)
				.edit();
		editor.putString(USER_IMAGE_URL, token);
		editor.commit();
	}

	public static String getUserName(Context context) {
		SharedPreferences savedSession = context.getSharedPreferences(KEY,
				Context.MODE_PRIVATE);
		return savedSession.getString(USER_NAME, "");
	}

	public static void setUserName(Context context, String token) {
		Editor editor = context.getSharedPreferences(KEY, Context.MODE_PRIVATE)
				.edit();
		editor.putString(USER_NAME, token);
		editor.commit();
	}

	public static String getUserEmail(Context context) {
		SharedPreferences savedSession = context.getSharedPreferences(KEY,
				Context.MODE_PRIVATE);
		return savedSession.getString(USER_EMAIL, "");
	}

	public static void setUserEmail(Context context, String token) {
		Editor editor = context.getSharedPreferences(KEY, Context.MODE_PRIVATE)
				.edit();
		editor.putString(USER_EMAIL, token);
		editor.commit();
	}

	public static void setDeviceID(Context context, String token) {
		Editor editor = context.getSharedPreferences(KEY, Context.MODE_PRIVATE)
				.edit();
		editor.putString(DEVICE_ID, token);
		editor.commit();
	}

	public static String getDeviceID(Context context) {

		SharedPreferences savedSession = context.getSharedPreferences(KEY,
				Context.MODE_PRIVATE);
		return savedSession.getString(DEVICE_ID, "");
	}

	public static void setVersionNo(Context context, int version) {
		Editor editor = context.getSharedPreferences(KEY, Context.MODE_PRIVATE)
				.edit();
		editor.putFloat(APP_VERSION_NO, version);
		editor.commit();
	}

	public static float getVersionNo(Context context) {

		SharedPreferences savedSession = context.getSharedPreferences(KEY,
				Context.MODE_PRIVATE);
		return savedSession.getFloat(APP_VERSION_NO, 0);
	}
}
