package com.twodfire.log.appender;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.rolling.RollingFileAppender;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * 异步日志处理
 */
public class AsyncRollingFileAppender extends RollingFileAppender<ILoggingEvent> {

    BlockingQueue<ILoggingEvent> blockingQueue;
    /**
     * The default buffer size.
     */
    int queueSize = 256;

    int discardingThreshold = 0;

    int maxFlushTime = 1000;
    boolean includeCallerData = true;//默认该为true

    Worker worker = new Worker();

    @Override
    public void start() {
        if (queueSize < 1) {
            addError("Invalid queue size [" + queueSize + "]");
            return;
        }
        blockingQueue = new ArrayBlockingQueue<ILoggingEvent>(queueSize);
        addInfo("Setting discardingThreshold to " + discardingThreshold);
        worker.setDaemon(true);
        worker.setName("AsyncAppender-Worker-" + getName());
        // make sure this instance is marked as "started" before staring the worker Thread
        super.start();
        worker.start();
    }

    @Override
    public void stop() {
        if (!isStarted())
            return;

        // mark this appender as stopped so that Worker can also processPriorToRemoval if it is invoking aii.appendLoopOnAppenders
        // and sub-appenders consume the interruption
        super.stop();

        // interrupt the worker thread so that it can terminate. Note that the interruption can be consumed
        // by sub-appenders
        worker.interrupt();
        try {
            worker.join(maxFlushTime);

            //check to see if the thread ended and if not add a warning message
            if (worker.isAlive()) {
                addWarn("Max queue flush timeout (" + maxFlushTime + " ms) exceeded. Approximately " + blockingQueue.size() +
                        " queued events were possibly discarded.");
            } else {
                addInfo("Queue flush finished successfully within timeout.");
            }

        } catch (InterruptedException e) {
            addError("Failed to join worker thread. " + blockingQueue.size() + " queued events may be discarded.", e);
        }
    }

    @Override
    protected void append(ILoggingEvent eventObject) {
        if (isQueueBelowDiscardingThreshold()) {
            return;
        }
        eventObject.prepareForDeferredProcessing();
        eventObject.getCallerData();
        put(eventObject);
    }

    private boolean isQueueBelowDiscardingThreshold() {
        return (blockingQueue.remainingCapacity() < discardingThreshold);
    }

    private void put(ILoggingEvent eventObject) {
        try {
            blockingQueue.put(eventObject);
        } catch (InterruptedException e) {
        }
    }

    public int getQueueSize() {
        return queueSize;
    }

    public void setQueueSize(int queueSize) {
        this.queueSize = queueSize;
    }

    public int getDiscardingThreshold() {
        return discardingThreshold;
    }

    public void setDiscardingThreshold(int discardingThreshold) {
        this.discardingThreshold = discardingThreshold;
    }

    public int getMaxFlushTime() {
        return maxFlushTime;
    }

    public void setMaxFlushTime(int maxFlushTime) {
        this.maxFlushTime = maxFlushTime;
    }

    public boolean isIncludeCallerData() {
        return includeCallerData;
    }

    public void setIncludeCallerData(boolean includeCallerData) {
        this.includeCallerData = includeCallerData;
    }

    class Worker extends Thread {

        public void run() {
            AsyncRollingFileAppender parent = AsyncRollingFileAppender.this;

            // loop while the parent is started
            while (parent.isStarted()) {
                try {
                    ILoggingEvent e = parent.blockingQueue.take();
                    parent.subAppend(e);
                } catch (InterruptedException ie) {
                    break;
                }
            }

            addInfo("Worker thread will flush remaining events before exiting. ");

            for (ILoggingEvent e : parent.blockingQueue) {
                parent.subAppend(e);
                parent.blockingQueue.remove(e);
            }

        }
    }
}
