package com.mopinfo.mop2048.log;

import android.util.Log;

/**
 * Created by mop on 2016/12/12.
 */
public class Logger implements ILogger {

    private LogMannger manager;
    private String tagName;
    private String className;

    public Logger(LogMannger manager, String tagName, Class clazz) {
        this.manager = manager;
        this.tagName = tagName;
        this.className = clazz.getSimpleName();
    }

    @Override
    public void debug(String msg) {
        Log.d(tagName, String.format("%s: %s", className, msg));
        sendMessage("DEBUG", msg);
    }

    @Override
    public void info(String msg) {
        Log.i(tagName, String.format("%s: %s", className, msg));
        sendMessage("INFO", msg);
    }

    @Override
    public void warn(String msg) {
        Log.w(tagName, String.format("%s: %s", className, msg));
        sendMessage("WARN", msg);
    }

    @Override
    public void error(String msg) {
        Log.e(tagName, String.format("%s: %s", className, msg));
        sendMessage("ERROR", msg);
    }

    private void sendMessage(String level, String msg) {
        String message = String.format("%s: %s", className, msg);
        this.manager.sendMessage(level, message);
    }
}
