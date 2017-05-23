package com.dmall.blas.collect.core;

import com.dmall.spotmix.common.serializer.protobuf.DataPacketMsg;
import com.dmall.spotmix.sdk.handler.SubscribeHandler;
import com.google.protobuf.ProtocolStringList;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Created by lrkin on 2017/5/18.
 */
public class DefaultHandler implements SubscribeHandler {
    @Resource
    private EsSubmitQueue esSubmitQueue;

    private static String[] list = new String[]{"/app/passport/login", "/app/passport/smsLogin", "/app/passport/weChatLogin", "/app/passport/register", "/app/passport/validCode"};

    public void onLoggingMessages(List<DataPacketMsg.DataPacket> dps) {
        for (DataPacketMsg.DataPacket dataPacket : dps) {
            ProtocolStringList list = dataPacket.getDataMessageList();
            for (String msg : list) {
//                System.out.println(msg);
                String message = msg;
                try {
                    if (!msg.contains("ERROR")) {
                        int indexOfUrl = msg.indexOf("url");
                        int indexOfFirstMark = msg.indexOf("|");
                        String url = msg.substring(indexOfUrl + 4, indexOfFirstMark);
                        if (list.contains(url)) {
                            HashMap<String, Object> map = new HashMap<String, Object>();
                            String content = msg.substring(indexOfFirstMark, msg.length());
                            String[] split = content.split("\\|-\\|");
                            for (int i = 0; i < split.length; i++) {
                                String[] entry = split[i].split("=");
                                if (entry.length > 1) {
                                    map.put(entry[0], entry[1]);
                                } else {
                                    map.put(entry[0], "");
                                }
                            }
                            esSubmitQueue.append(map);
                        }
                    }
                } catch (Exception e) {
                    System.out.println(message);
                    e.printStackTrace();
                }


            }
        }
    }
}
