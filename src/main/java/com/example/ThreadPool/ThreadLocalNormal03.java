package com.example.ThreadPool;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 加锁解决线程安全问题
 */
public class ThreadLocalNormal03 {

    public static ExecutorService executorService = Executors.newFixedThreadPool(10);
    static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    public static void main(String[] args) {
            for (int i = 0; i < 1000; i++) {
                int value = i;
                    executorService.submit(new Runnable() {
                        @Override
                        public void run() {
                            String date = new ThreadLocalNormal03().date(value);
                            System.out.println(date);
                        }
                    });
            }
        executorService.shutdown();
    }

    public String date(int seconds){
        Date date = new Date(1000L * seconds);
        String s = null;
        synchronized (ThreadLocalNormal03.class){
            s = simpleDateFormat.format(date);
        }
        return s;
    }
}
