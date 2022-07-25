package com.example.ThreadPool;

/**
 * 需求：线程隔离
 * 在多线程并发的场景下，每个线程中的变量都是独立的
 * 线程a：设置变量1  获取变量1
 * 线程b: 设置变量2  获取变量2
 *
 * ThreadLocal:
 *      set() : 将变量绑定到当前线程中
 *      get() : 获取当前线程绑定的变量
 */
public class GeliThread {

    ThreadLocal<String> threadLocal = new ThreadLocal<>();

    private String content;

    private String getContent(){
//        return content;
        return threadLocal.get();
    }

    private void setContent(String content){
//        this.content = content;
        threadLocal.set(content);
    }

    public static void main(String[] args) {
        GeliThread geliThread = new GeliThread();
        for (int i = 0; i < 5; i++) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    geliThread.setContent(Thread.currentThread().getName() + "的数据");
                    System.out.println("==================");
                    System.out.println(Thread.currentThread().getName() + "------>" + geliThread.getContent());
                }
            });
            thread.setName("线程" + i);
            thread.start();
        }
    }
}
