package com.yupi.yurpc.mytest;

import com.yupi.yurpc.protocol.ProtocolMessageTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.*;

/**
 * ClassName: Test1
 * Package: com.yupi.yurpc.mytest
 * Description:
 *
 * @Author Joy_瑶
 * @Create 2024/6/17 20:42
 * @Version 1.0
 */
@Slf4j
public class Test1 {
    @Test
    public void test1() throws ExecutionException, InterruptedException {

        System.out.println("开始测试！");
        Thread t1=new Thread(){
            @Override
            public void run() {
                log.debug("Hello, Thread!");
                System.out.println("hello");
            }
        };
        t1.setName("myThread1");
        t1.start();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                log.debug("Hello, Thread+Runnable");
            }
        };
        Thread t2 = new Thread(runnable, "myThread2");
        t2.start();

        FutureTask<Integer> task = new FutureTask<>(() -> {
            log.debug("Hello, Thread+FutureTask+Callable");
            return 100;
        });
        Thread t3 = new Thread(task, "myThread3");
        t3.start();

        Integer integer = task.get();
        System.out.println(integer);
    }
    @Test
    public void test2() throws InterruptedException {
        //线程池:
        // 创建一个核心线程数为2的线程池
        //使用jdk提供的线程池

        //自己创建线程池
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2, 10, 1, TimeUnit.MINUTES, new ArrayBlockingQueue<>(5, true),
                Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
        for(int i=0;i<10;i++){
            final int index=i;
            threadPoolExecutor.execute(()->{
                System.out.println(Thread.currentThread().getName()+" "+index);
            });
            Thread.sleep(2000);
        }
    }
    @Test
    public void test3() throws InterruptedException {
        //使用固定线程池
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        for(int i=0;i<10;i++){
            final int index=i;
            executorService.execute(()->{
                System.out.println(Thread.currentThread().getName()+" "+index);
            });
            Thread.sleep(2000);
        }
    }
    @Test
    public void test4() throws InterruptedException {
        //使用可缓存的线程池
        ExecutorService executorService = Executors.newCachedThreadPool();
        for(int i=0;i<10;i++){
            final int index=i;
            executorService.execute(()->{
                System.out.println(Thread.currentThread().getName()+" "+index);
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
    }
    @Test
    public void test5() throws InterruptedException {
        //创建单例线程池
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        for(int i=0;i<10;i++){
            final int index=i;
            executorService.execute(()->{
                System.out.println(Thread.currentThread().getName()+" "+index);
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        Thread.sleep(15000);
    }
    @Test
    public void test6() throws InterruptedException {
        //创建任务调度线程池
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(3);
        System.out.println("提交任务");
        for(int i=0;i<10;i++){
            final int index=i;
            scheduledExecutorService.schedule(()->{
                System.out.println(Thread.currentThread().getName()+" "+index);
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            },3,TimeUnit.SECONDS);

        }
        Thread.sleep(15000);
    }
    @Test
    public void test7(){
        Queue<Integer> queue=new LinkedList<>();



    }
}
