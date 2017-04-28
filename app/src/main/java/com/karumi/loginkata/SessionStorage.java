package com.karumi.loginkata;

public interface SessionStorage {

    void saveUser(String email);

    String getUser();
}
