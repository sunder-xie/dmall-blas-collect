package com.dmall.blas.collect.core;

import com.alibaba.fastjson.JSON;
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
//                            HashMap<String, Object> map = new HashMap<String, Object>();
//                            int indexOfUrl = msg.indexOf("url");
//                            int indexOfFirstMark = msg.indexOf("|");
//                            String content = msg.substring(indexOfFirstMark, msg.length());
//                            String[] split = content.split("\\|-\\|");
//                            for (int i = 0; i < split.length; i++) {
//                                String[] entry = split[i].split("=");
//                                if (entry[0] != null && !entry[0].equals("")) {
//                                    if (entry.length > 1) {
//                                        if (entry[0].equals("headers")) {
//                                            //解析headers(其中有cookie)
//                                            String headerJson = entry[1].substring(0, entry[1].length());
//                                            parseHeaders(map, headerJson);
//                                        } else {
//                                            map.put(entry[0], entry[1]);
//                                        }
//                                    } else {
//                                        map.put(entry[0], "");
//                                    }
//                                }
//                            }
//                            System.out.println(map);
//                            esSubmitQueue.append(map);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void parseHeaders(HashMap map, String headers) {
        String[] result = headers.split(",\"");
        for (int i = 0; i < result.length; i++) {
            String str = result[i].replaceAll("\"", "");
            String[] entry = str.split(":");
            if (entry[0].equals("bigdata")) continue;
            if (entry.length > 1) {
                if (entry[0].equals("cookie")) {
                    System.out.println(entry[1]);
                    String[] cookieArray = entry[1].split("; ");
                    for (int j = 0; j < cookieArray.length; j++) {
                        String[] split = cookieArray[j].split("=");
                        if (split.length > 1) {
                            map.put(split[0], split[1]);
                        } else {
                            map.put(split[0], "");
                        }
                        System.out.println(split[0]);
                    }
                } else {
                    map.put(entry[0], entry[1]);
                }
            } else {
                map.put(entry[0], "");
            }
        }
//        System.out.println(map);
    }

    public static void parseParam(HashMap map, String paramStr) {
        String[] result = paramStr.substring(1, paramStr.length()-1).split(",\"");
        for (int i = 0; i < result.length; i++) {
            String str = result[i].replaceAll("\"", "");
            String[] entry = str.split(":");
            if (entry.length > 1) {
                map.put(entry[0], entry[1]);
            } else {
                map.put(entry[0], "");
            }
        }
        System.out.println(map);
    }

//    public static void main(String[] args) {
//        HashMap<String, String> map = new HashMap<String, String>();
//        String paramStr = "{\"phone\":\"15652533746\",\"setPwd\":0,\"loginType\":0,\"pwd\":\"54cab4b7c1ffec465a8efca0298aabd5\",\"cid\":\"1ebd5bb95559a730fb8da54a7f8111b9\"}";
//        parseParam(map, paramStr);
//    }

    public static void main(String[] args) {
        String msg = "[INFO ] 2017-05-23 22:05:55.711 [http-8105-454] [LOG_USER_VISIT] - url=/app/passport/login|-|param={\"phone\":\"15652533746\",\"setPwd\":0,\"loginType\":0,\"pwd\":\"54cab4b7c1ffec465a8efca0298aabd5\",\"cid\":\"1ebd5bb95559a730fb8da54a7f8111b9\"}|-|userId=3175017|-|loginId=e8259738-8ed6-47b3-864f-dda3e4a31f4e|-|IP=192.168.90.83|-|storeId=230|-|platform=IOS|-|device=iPhone 6s Plus|-|sysVersion=9.3.5|-|version=3.5.0|-|http_x_forwarded_for=114.242.249.197|-|headers={\"accept\":\"*/*\",\"accept-encoding\":\"gzip, deflate\",\"accept-language\":\"zh-Hans-CN;q=1\",\"apiversion\":\"3.5.0\",\"appname\":\"com.dmall.dmall\",\"bigdata\":\"H4sIAAAAAAAAA01QS07DMBS8SmWxTKo4/ia7Llg3ouwrN35pLFInsp1WCCHBHVjBGdiz4TYgwS2IU5DYPc3Me29m7tA4Go1KVMi8AZwVWSaw4jTLRdNoKbkGtitwrTNcS86oQAm66P32CM6b3sbFJVmyCa1bZS10E7Kqqs31+uoySmvlnAE3oR9vr58v71+PT98PzxMTzAF8UIcBlZgWjFFJGGMcTzu+dgB224LZtwGVnIsEjR7cdjZKsGAZnn38Ck9GhxaVRLAEdf3e2LMQZM4KQWQqQfOUih1JJadNqrUiQBXBDYV45dDr2bap2t7CgvtF1Y0+MhbCqXc323A7QPwcMTUM/8JP0ZdZhE+mMahsVOchQT70Ds4mchLpep7nZ8qOjarD6OZSVsPQwbnS6GC9meYjWP0XFqP7Hy34f+WiAQAA\",\"cache-control\":\"no-cache\",\"channelid\":\"APPSTORE\",\"connection\":\"keep-alive\",\"content-length\":\"190\",\"content-type\":\"application/x-www-form-urlencoded\",\"cookie\":\"Hm_lvt_3c760cfd485a098a377d76ceec857d7d=1495243294,1495376689,1495438698,1495499725; _digger_uuid=4d8c97c7686357f4; cookie_id=1494658191872QDgzt; tempid=C71CB1D642200002BE307F74DBBB8C001466235338786\",\"currenttime\":\"1495548355561\",\"device\":\"iPhone 6s Plus\",\"dmall_trace_id\":\"e7b1204b-3cbf-4509-a4e9-7f1b928e511d\",\"dmall_trace_instance\":\"192.168.90.83:9983\",\"dmall_trace_thread\":\"Thread[pool-4-thread-144-appapi_pool,5,main]\",\"host\":\"appapi.dmall.com\",\"idfa\":\"06A11BCD-4E6E-4419-8FA4-3496394E25A9\",\"networktype\":\"3\",\"platform\":\"IOS\",\"pragma\":\"no-cache\",\"screen\":\"667*375\",\"smartloading\":\"1\",\"storeid\":\"230\",\"sysversion\":\"9.3.5\",\"ticketname\":\"164F1C7755BAD5081D968E5BE55FDBE1FA569E1E167E55D78FD4A19B6734B40E2F445F277DB59B4BE71B1623647AACD55FEA5DD2882E45D8FFC6A9291A2FD59A9DBBCC329C13A48F14035BD3CFCA11BB524DB0C85FA45343F0AB3D812AD2AD13DA8ABD63548A94373A743D5F5AB818B8A665A1AFE323FDD38F003E6DB231DB11\",\"token\":\"appapi_007579010ed622046e83eee61174a565\",\"user-agent\":\"dmall/3.5.0 (iPhone; iOS 9.3.5; Scale/3.00)\",\"uuid\":\"982fe1090071a64027ffd886de5b91cd01c86547\",\"venderid\":\"1\",\"version\":\"3.5.0\",\"x-forwarded-for\":\"114.242.249.197\",\"x-real-ip\":\"114.242.249.197\",\"xyz\":\"ac\"}";
        HashMap<String, Object> map = new HashMap<String, Object>();
        int indexOfUrl = msg.indexOf("url");
        int indexOfFirstMark = msg.indexOf("|");
        String content = msg.substring(indexOfFirstMark, msg.length());
        String[] split = content.split("\\|-\\|");
        for (int i = 1; i < split.length; i++) {
            int markIndex = split[i].indexOf("=");
            String[] entry = {};
            if (split[i].length() == markIndex + 1) {
                entry = new String[]{split[i].substring(0, markIndex), ""};
            } else {
                entry = new String[]{split[i].substring(0, markIndex), split[i].substring(markIndex + 1, split[i].length())};
            }
            if (entry[0] != null && !entry[0].equals("")) {
                if (entry.length > 1) {
                    if (entry[0].equals("headers")) {
                        int length = entry[1].length();
                        String headerJson = entry[1].substring(2, length);
                        parseHeaders(map, headerJson);
                    } else if (entry[0].equals("param")) {
                        parseParam(map, entry[1]);
                    } else {
                        map.put(entry[0], entry[1]);
                    }
                } else {
                    map.put(entry[0], "");
                }
            }
        }
        System.out.println(map);
    }
}
