package com.karumi.loginkata;

public class TimeProvider {

    public long getCurrentTimeSeconds() {
        return System.currentTimeMillis()/1000;
    }

}
