package com.yuanwj.poetry.utils;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;

public class ExecutorUtils {

    private static ExecutorService executorService ;
    private static ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
            .setNameFormat("test-pool-%d").build();


    public static synchronized ExecutorService getExecutorService() {
        if (executorService == null) {

            executorService = new ThreadPoolExecutor(10, 15, 20, TimeUnit.MINUTES,
                    new ArrayBlockingQueue<>(20), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
//            executorService = Executors.newFixedThreadPool(10);
        }
        return executorService;
    }
}
