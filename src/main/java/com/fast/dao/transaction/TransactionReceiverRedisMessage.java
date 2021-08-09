package com.fast.dao.transaction;

public class TransactionReceiverRedisMessage {

    public void receiveMessage(String jsonMsg) {
        System.out.println(jsonMsg);
    }


}
