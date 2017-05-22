package com.dmall.blas.collect.core;

/**
 * Created by lrkin on 2017/5/22.
 */
public class Test {
    public static void main(String[] args) {
        String msg = "[INFO ] 2017-05-19 18:22:12.075 [http-8107-438] [LOG_USER_VISIT] - url=/app/apporder/fastScanOrder|-|param=|-|userId=3885866|-|loginId=0d9fed1a-e23d-410c-968c-70d2caf75323|-|IP=192.168.90.229|-|storeId=292|-|platform=ANDROID|-|device=smartisan YQ601 KTU84P dev-keys|-|sysVersion=Android-4.4.4|-|version=3.5.0";
        int indexOfUrl = msg.indexOf("url");
        int indexOfFirstMark = msg.indexOf("|");
        String url = msg.substring(indexOfUrl + 4, indexOfFirstMark);
        System.out.println(url);
        System.out.println(msg.substring(indexOfFirstMark, msg.length()));
        String content = msg.substring(indexOfFirstMark, msg.length());
        String[] split = content.split("\\|-\\|");
        for (int i = 0; i < split.length; i++) {
            System.out.println(split[i]);
        }
    }
}
