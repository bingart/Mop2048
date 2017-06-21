package com.mopinfo.lib.log;

/**
 * Created by mop on 2016/12/12.
 */
public interface ILogger {

    void debug(String msg);

    void info(String msg);

    void warn(String msg);

    void error(String msg);

}
