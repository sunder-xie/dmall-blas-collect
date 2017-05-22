package com.dmall.blas.collect.core;

import com.dmall.spotmix.common.serializer.protobuf.DataPacketMsg;
import com.dmall.spotmix.sdk.handler.SubscribeHandler;
import com.google.protobuf.ProtocolStringList;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * Created by lrkin on 2017/5/18.
 */
public class DefaultHandler implements SubscribeHandler {
    @Resource
    private EsSubmitQueue esSubmitQueue;

    public void onLoggingMessages(List<DataPacketMsg.DataPacket> dps) {
        for (DataPacketMsg.DataPacket dataPacket : dps) {
            ProtocolStringList list = dataPacket.getDataMessageList();
            for (String msg : list) {
                System.out.println(msg);
                // TODO 自定义日志消费处理逻辑
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("id", 12);
                map.put("title", "title");
                map.put("posttime", "2016-09-22");
                map.put("content", "content");
                map.put("updatetime", "2017-09-12");
                esSubmitQueue.append(map);
            }
        }
    }
}
