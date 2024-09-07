package com.yupi.yurpc;

import io.etcd.jetcd.ByteSequence;
import io.etcd.jetcd.Client;
import io.etcd.jetcd.KV;
import io.etcd.jetcd.kv.GetResponse;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * ClassName: EtcdTest
 * Package: com.yupi.yurpc
 * Description:
 *
 * @Author Joy_瑶
 * @Create 2024/6/10 12:09
 * @Version 1.0
 */
public class EtcdTest {
    public static void main(String[] args) throws ExecutionException,InterruptedException {
        //使用endpoints创建client
        Client client = Client.builder().endpoints("http://localhost:2379")
                .build();

        KV kvClient = client.getKVClient();
        ByteSequence key = ByteSequence.from("test_key".getBytes());
        ByteSequence value = ByteSequence.from("test_value".getBytes());

        //将key-value存入etcd
        kvClient.put(key,value).get();

        //获取completableFuture
        CompletableFuture<GetResponse> getFuture = kvClient.get(key);

        //从completableFuture中获取value
        GetResponse response = getFuture.get();

        //删除key
//        kvClient.delete(key).get();
    }
}
