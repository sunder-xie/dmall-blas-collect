package com.dmall.blas.collect.core;

import com.dmall.spotmix.common.serializer.protobuf.DataPacketMsg;
import com.dmall.spotmix.sdk.handler.SubscribeHandler;
import com.google.protobuf.ProtocolStringList;

import javax.annotation.Resource;
import java.io.BufferedWriter;
import java.io.FileWriter;
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

    private static String[] urlList = new String[]{"/app/passport/login", "/app/passport/smsLogin", "/app/passport/weChatLogin", "/app/passport/register", "/app/passport/validCode"};

    public void onLoggingMessages(List<DataPacketMsg.DataPacket> dps) {
        for (DataPacketMsg.DataPacket dataPacket : dps) {
            ProtocolStringList list = dataPacket.getDataMessageList();
            for (String msg : list) {
                try {
                    if (!msg.contains("ERROR")) {
                        if (msg.contains(urlList[0]) || msg.contains(urlList[1]) || msg.contains(urlList[2]) || msg.contains(urlList[3])) {
                            System.out.println(msg);
                            HashMap<String, Object> map = new HashMap<String, Object>();
                            int indexOfUrl = msg.indexOf("url");
                            int indexOfFirstMark = msg.indexOf("|");
                            String content = msg.substring(indexOfFirstMark, msg.length());
                            String[] split = content.split("\\|-\\|");
                            for (int i = 0; i < split.length; i++) {
                                String[] entry = split[i].split("=");
                                if (entry[0] != null && !entry[0].equals("")) {
                                    if (entry.length > 1) {
                                        map.put(entry[0], entry[1]);
                                    } else {
                                        map.put(entry[0], "");
                                    }
                                }
                            }
//                            System.out.println(map);
                            esSubmitQueue.append(map);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

//    private void writeToFile(String msg) {
//        FileWriter fw = new FileWriter("rfm_data_after.csv");
//        BufferedWriter bw = new BufferedWriter(fw);
//        try {
//            bw.write("hello");
//            bw.newLine();
//            bw.write("world");
//        } finally {
//            bw.flush();
//            bw.close();
//        }
//    }
}
