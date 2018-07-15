package com.pingao.model;

/**
 * Created by pingao on 2018/7/12.
 */
public class WebSocketMsg {
    private String command;
    private String path;
    private String content;

    public WebSocketMsg(String command, String path, String content) {
        this.command = command;
        this.path = path;
        this.content = content;
    }

    @Override
    public String toString() {
        return "WebSocketMsg[command=" + command +
            ", path=" + path +
            ", content=" + content;
    }
}
