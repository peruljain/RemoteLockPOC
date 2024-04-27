package org.example;

import org.example.lock.DistributedLockManager;
import org.example.lock.RemoteLockManager;
import org.example.redis.JedisClient;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {
//        remoteLockTesting();
        distributedLockTesting();
    }

    public static void  remoteLockTesting() throws InterruptedException {
        RemoteLockManager lockManager = new RemoteLockManager(new JedisClient(6379));
        String key = "lockKey-1";

        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i<5; i++) {
            Thread thread = new Thread(() -> lockManager.processTask(key));
            threads.add(thread);
            thread.start();
        }

        for(int i = 0; i<threads.size(); i++) {
            threads.get(i).join();
        }

    }

    public static void distributedLockTesting() throws InterruptedException {
        DistributedLockManager lockManager = new DistributedLockManager(List.of(new JedisClient(6379), new JedisClient(6380), new JedisClient(6381),
                new JedisClient(6382), new JedisClient(6383)));

        String key = "lockKey-1";

        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i<100; i++) {
            Thread thread = new Thread(() -> lockManager.processTask(key));
            threads.add(thread);
            thread.start();
        }

        for(int i = 0; i<threads.size(); i++) {
            threads.get(i).join();
        }

    }


}