package com.fang.service.parseTelemetry.blockIngQueue;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class TelemetryBlockingQueue<T> {
    private LinkedBlockingDeque<T> queue;

    public TelemetryBlockingQueue(LinkedBlockingQueue<T> queue) {
        //this.queue = queue;
    }

//入队
    public void enqueue(T item) throws InterruptedException {

        this.queue.put(item);

    }
//出队

    public T dequeue(int seconds) throws InterruptedException {
        T result = null;
        if (seconds < 0) {
            result = this.queue.take();

        } else {
            result = this.queue.poll(seconds, TimeUnit.SECONDS);
        }
        return result;
    }
}
