package com.pingao.enums;

/**
 * Created by pingao on 2018/7/13.
 */
public enum MiMeType {
    HTML("text/html"),
    PLAIN("text/plain"),
    JAVASSCRIPT("application/javascript"),
    CSS("text/css"),
    JSON("application/json");
    private String type;

    MiMeType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
