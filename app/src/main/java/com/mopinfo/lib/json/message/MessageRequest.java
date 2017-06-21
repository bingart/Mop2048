package com.mopinfo.lib.json.message;

/**
 * Created by mop on 2017/1/22.
 */
public class MessageRequest {

    private String uid;

    private String level;

    private String message;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
