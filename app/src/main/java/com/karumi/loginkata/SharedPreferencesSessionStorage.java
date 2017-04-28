package com.karumi.loginkata;

import android.content.SharedPreferences;

public class SharedPreferencesSessionStorage implements SessionStorage {

    private final SharedPreferences sharedPreferences;

    public SharedPreferencesSessionStorage(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public void saveUser(String email) {
        sharedPreferences.edit().putString("username",email).apply();
    }

    @Override
    public String getUser() {
        return sharedPreferences.getString("username", null);
    }
}
