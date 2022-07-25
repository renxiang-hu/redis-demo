package com.example.ThreadPool;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ThreadLocalNormal00 {
    public static void main(String[] args) {
       new Thread(new Runnable() {
           @Override
           public void run() {
               String date = new ThreadLocalNormal00().date(10);
               System.out.println(date);
           }
       }).start();

       new Thread(new Runnable() {
           @Override
           public void run() {
               String date = new ThreadLocalNormal00().date(1000);
               System.out.println(date);
           }
       }).start();
    }

    public String date(int seconds){
        Date date = new Date(1000L *seconds);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return simpleDateFormat.format(date);
    }


}
