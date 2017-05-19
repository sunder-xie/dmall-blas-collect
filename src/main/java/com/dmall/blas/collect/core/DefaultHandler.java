package com.dmall.blas.collect.core;

import com.dmall.spotmix.common.serializer.protobuf.DataPacketMsg;
import com.dmall.spotmix.sdk.handler.SubscribeHandler;
import com.google.protobuf.ProtocolStringList;

import java.util.List;

/**
 * Created by lrkin on 2017/5/18.
 */
public class DefaultHandler implements SubscribeHandler {
    public void onLoggingMessages(List<DataPacketMsg.DataPacket> dps) {
        for (DataPacketMsg.DataPacket dataPacket : dps) {
            ProtocolStringList list = dataPacket.getDataMessageList();
            for (String msg : list) {
                System.out.println(msg);
                // TODO 自定义日志消费处理逻辑
            }
        }
    }
}
