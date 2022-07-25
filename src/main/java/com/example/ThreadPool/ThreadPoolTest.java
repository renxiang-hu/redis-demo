package com.example.ThreadPool;

public class ThreadPoolTest {
    public static ThreadLocal<Long> threadLocal = new ThreadLocal<Long>(){
        @Override
        protected Long initialValue() {
            System.out.println("print data");
            return 100L;
        }
    };

    public static void main(String[] args) {
        threadLocal.set(2000L);
        System.out.println(threadLocal.get());
        System.out.println(threadLocal.get());
    }
}
