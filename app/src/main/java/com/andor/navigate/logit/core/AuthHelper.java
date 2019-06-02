package com.andor.navigate.logit.core;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.andor.navigate.logit.auth.Authorization;
import com.auth0.android.jwt.JWT;

public class AuthHelper {

    /**
     * Key for username in the jwt claim
     */
    private static final String JWT_KEY_EMAIL = "email";
    private static final String JWT_KEY_PASSWORD = "password";
    private static final String PREFS = "com.andor.navigate.authsharedprefs";
    private static final String PREF_TOKEN = "pref_token";
    private SharedPreferences mPrefs;

    private static AuthHelper sInstance;

    private AuthHelper(@NonNull Context context) {
        mPrefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        sInstance = this;
    }

    public static AuthHelper getInstance(@NonNull Context context) {
        if (sInstance == null) {
            sInstance = new AuthHelper(context);
        }
        return sInstance;
    }

    public void setIdToken(@NonNull Authorization authorization) {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(PREF_TOKEN, authorization.getToken());
        editor.apply();
    }

    @Nullable
    public String getIdToken() {
        return mPrefs.getString(PREF_TOKEN, null);
    }

    public boolean isLoggedIn() {
        String token = getIdToken();
        return token != null;
    }

    public String getEmail() {
        if (isLoggedIn()) {
            return decodeEmail(getIdToken());
        }
        return null;
    }

    public String getPassword() {
        if (isLoggedIn()) {
            return decodePass(getIdToken());
        }
        return null;
    }

    @Nullable
    private String decodeEmail(String token) {
        JWT jwt = new JWT(token);
        try {
            return jwt.getClaim(JWT_KEY_EMAIL).asString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    private String decodePass(String password) {
        JWT jwt = new JWT(password);
        try {
            return jwt.getClaim(JWT_KEY_PASSWORD).asString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void clear() {
        mPrefs.edit().clear().apply();
    }
}