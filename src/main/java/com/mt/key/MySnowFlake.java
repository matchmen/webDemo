package com.mt.key;

import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * author: liqm
 * 2020-03-09
 */
@Component
public class MySnowFlake {

    /**
     * 机械ID BIT数
     */
    private long workerBits = 5L;

    /**
     * 最大机械ID
     */
    private long maxWorkerId = ~(-1L << workerBits);

    /**
     * 机房ID BIT数
     */
    private long datacenterIdBits = 5L;

    /**
     * 最大机房ID
     */
    private long maxDatacenterId = ~(-1L << datacenterIdBits);

    /**
     * 时间戳
     */
    private long lastTimestamp = -1L;

    /**
     * 序列号BIT数
     */
    private long sequenceBits = 12;

    /**
     * 最大序列号
     */
    private long maxSequence = ~(-1L << sequenceBits);

    /**
     * 机械ID
     */
    private long workerId;

    /**
     * 机房ID
     */
    private long datacenterId;

    private Random random = new Random();

    private int random_sq =~(-1<<sequenceBits);


    /**
     * 序列号
     */
    private long sequence;

    public MySnowFlake(long datacenterId, long workerId, long sequence) {

        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("workerId can not more than %d or less than 0", maxWorkerId));
        }

        if (datacenterId > maxDatacenterId || datacenterId < 0) {
            throw new IllegalArgumentException(String.format("datacenterId can not more than %d or less than 0", maxDatacenterId));
        }

        this.workerId = workerId;
        this.datacenterId = datacenterId;
        this.sequence = sequence;
    }

    public MySnowFlake() {
        this.workerId = 1;
        this.datacenterId = 1;
        this.sequence = 0;
    }

    /**
     * 生成Long Id
     *
     * @return
     */
    public synchronized Long generate() {
        //获取时间戳,单位毫秒
        long timestamp = System.currentTimeMillis();
        if (timestamp < lastTimestamp) {
            System.err.printf(
                    "clock is moving backwards. Rejecting requests until %d.", lastTimestamp);
            throw new RuntimeException(
                    String.format("Clock moved backwards. Refusing to generate id for %d milliseconds",
                            lastTimestamp - timestamp));
        }
        //同一毫秒内第二次发起ID生成，这时把seqence依次递增1
        if (timestamp == lastTimestamp) {
            //sequence最大为4096 ，为防止sequence超过4096，作如下验证
            //System.out.println(lastTimestamp);
            sequence = (sequence + 1) & maxSequence;
            //System.out.println(sequence);
            if (sequence == 0) {
                timestamp = getNextMillis(timestamp);
            }
        } else {
            //原生雪花算法的写法
            //sequence = 0;
            //奇偶均匀
            sequence = random.nextInt(random_sq);
        }
        //记录最近一次生成ID的时间戳
        lastTimestamp = timestamp;
        //开始拼接ID
        //1.时间戳左移机房ID BIT数 + 机械ID BIT数 + 序列号BIT数
        long timestampLeftShift = datacenterIdBits + workerBits + sequenceBits;
        //2.机房ID左移机械ID BIT数 + 序列号BIT数
        long datacenterIdLeftShift = workerBits + sequenceBits;
        //3.机械ID左移序列号BIT数
        long workerIdLeftShift = sequenceBits;
        //4.或运算生成ID,将时间戳、机房ID、机械ID分别左移相应位数，然后进行或运算
        //System.out.println("timestamp << timestampLeftShift:"+Long.toBinaryString(timestamp << timestampLeftShift ));
        //System.out.println("datacenterId << datacenterIdLeftShift:"+Long.toBinaryString(datacenterId << datacenterIdLeftShift ));
        //System.out.println("workerId << workerIdLeftShift:"+Long.toBinaryString(workerId << workerIdLeftShift ));
        //System.out.println("sequence:"+Long.toBinaryString(sequence ));
        //System.out.println("sequence:"+sequence);
        return (timestamp << timestampLeftShift) | (datacenterId << datacenterIdLeftShift) | (workerId << workerIdLeftShift) | sequence;
    }

    /**
     * 下一毫秒
     *
     * @param timestamp
     * @return
     */
    private Long getNextMillis(long timestamp) {

        long nextMillis = System.currentTimeMillis();
        while (nextMillis == timestamp) {
            nextMillis = System.currentTimeMillis();
        }
        return nextMillis;
    }


    public static void main(String[] args) throws InterruptedException {

        Integer threadNum = 20;
        /**
         * 开启20个线程并发
         */
        ExecutorService executorService = Executors.newFixedThreadPool(threadNum);
        final MySnowFlake mySnowFlake = new MySnowFlake(5, 5, 1);
        final CountDownLatch countDownLatch = new CountDownLatch(threadNum);
        Runnable runnable = new Runnable() {
            public void run() {
                for (int i = 0; i < 10000; i++) {
                    Long id = mySnowFlake.generate();

                    System.out.println("currThreadName:" + Thread.currentThread().getName() + "---》ID:" + id);
                }
                countDownLatch.countDown();
            }
        };
        Long start = System.currentTimeMillis();
        for (int i = 0; i < threadNum; i++) {
            executorService.execute(runnable);
        }
        countDownLatch.await();
        executorService.shutdown();
    }


}
