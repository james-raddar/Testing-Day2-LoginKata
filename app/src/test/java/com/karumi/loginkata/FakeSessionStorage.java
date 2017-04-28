package com.karumi.loginkata;

public class FakeSessionStorage implements SessionStorage {

    private String email;

    @Override
    public void saveUser(String email) {
        this.email=email;
    }

    @Override
    public String getUser() {
        return email;
    }
}
