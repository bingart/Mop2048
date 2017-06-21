package com.mopinfo.lib.log;

import android.util.Log;

import com.mopinfo.lib.common.NutchException;
import com.mopinfo.mop2048.config.ConfigManager;
import com.mopinfo.lib.util.MessageHelper;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by mop on 2016/12/12.
 */
public class LogMannger implements Runnable {

    private static LogMannger s;

    private class LogItem {

        private String level;
        private String message;

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

    private static String TAG_NAME = "nutch";
    private static int QUEUE_SIZE = 1024;

    private boolean running;
    private BlockingQueue<LogItem> queue;
    private boolean isUsingMessageServer;
    private String messageServerHost;
    private String uid;

    public static LogMannger getInstance() {
        if (s == null)
            s = new LogMannger();
        return s;
    }

    private LogMannger() {
        this.running = false;
        this.queue = null;
    }

    public ILogger getLogger(Class clazz) {
        return new Logger(this, TAG_NAME, clazz);
    }

    public synchronized boolean isRunning() { return running; }

    public synchronized void start() {
        if (!running) {
            // Start only once
            running = true;

            // LogManager instance created before ConfigManager
            this.isUsingMessageServer = ConfigManager.getInstance().isUsingMessageServer();
            this.messageServerHost = ConfigManager.getInstance().getMessageServerHost();
            this.uid = ConfigManager.getInstance().getUid();

            if (isUsingMessageServer) {
                this.queue = new ArrayBlockingQueue<LogItem>(QUEUE_SIZE);
                new Thread(this).start();
            }
        }
    }

    @Override
    public void run() {
        while (isRunning()) {
            try {
                LogItem item = queue.poll(1000, TimeUnit.MILLISECONDS);
                if (item != null) {
                    MessageHelper.doMessage(this.messageServerHost, this.uid, item.getLevel(), item.getMessage());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (NutchException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void sendMessage(String level, String message) {
        if (this.queue != null) {
            try {
                LogItem item = new LogItem();
                item.setLevel(level);
                item.setMessage(message);
                // Non-blocking when queue is full, only throw exception
                boolean result = this.queue.add(item);
                if (!result) {
                    // TODO
                }
            } catch (IllegalStateException ex) {
                Log.e("nutch", "sendMessage error, queue is full");
            }
        }
    }
}
