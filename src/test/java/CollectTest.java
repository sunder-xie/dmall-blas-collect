import com.dmall.blas.collect.core.DefaultHandler;
import com.dmall.blas.collect.core.EsSubmitQueue;
import com.dmall.blas.collect.core.GroovyExecutor;
import com.dmall.blas.collect.dao.BlasSysInfoMapper;
import com.dmall.blas.collect.service.CollectDataService;
import com.dmall.blas.collect.service.GroovyService;
import com.dmall.blas.collect.util.ESUtils;
import com.dmall.blas.collect.util.GatherToolkit;
import com.dmall.tool.basic.Message;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by lrkin on 2017/5/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/spring/spring-config-*.xml")
public class CollectTest {

    @Resource
    private CollectDataService service;

    @Resource
    private EsSubmitQueue esSubmitQueue;

    @Resource
    private BlasSysInfoMapper mapper;

    @Test
    public void test() throws UnknownHostException {
        String msg = "[INFO ] 2017-05-19 18:22:12.075 [http-8107-438] [LOG_USER_VISIT] - url=/app/apporder/fastScanOrder|-|param=|-|userId=3885866|-|loginId=0d9fed1a-e23d-410c-968c-70d2caf75323|-|IP=192.168.90.229|-|storeId=292|-|platform=ANDROID|-|device=smartisan YQ601 KTU84P dev-keys|-|sysVersion=Android-4.4.4|-|version=3.5.0";
        int indexOfUrl = msg.indexOf("url");
        int indexOfFirstMark = msg.indexOf("|");
        String url = msg.substring(indexOfUrl + 4, indexOfFirstMark);
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
//        Client client = TransportClient.builder().build()
//                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
//        IndexResponse response = client.prepareIndex("dmall-blas-collect", "log").setSource(map).get();
//        if (response.isCreated()) {
//            System.out.println("创建成功!");
//        }
        int i = 0;
        while (true) {
            map.put("id", 12 + i);
            esSubmitQueue.append(map);
            i++;
        }
    }

    @Test
    public void testGuava() throws ExecutionException, InterruptedException {
        while (true) {
            Thread.sleep(100);
            String scriptStr = GatherToolkit.dataCacher().getGroovy("aaa.com");
            System.out.println(scriptStr);
        }
    }

    @Test
    public void testRedis() {
        Jedis jedis = new Jedis("localhost");
        System.out.println(jedis.get("ccc.com"));
    }

    @Test
    public void testGroovy() throws Exception {
        GroovyExecutor.invoke("/app/passport/smsLogin", "parseData", "");
    }

    @Test
    public void testQueue() throws Exception {
        int i = 0;
        while (true) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("id", 12 + i);
            map.put("title", "title");
            map.put("posttime", "2016-09-22");
            map.put("content", "content");
            map.put("updatetime", "2017-09-12");
            esSubmitQueue.append(map);
            i++;
        }
    }

    @Test
    public void testQueryGroovy() throws Exception {
        System.out.println(mapper.selectGroovyByUrl("/app/passport/smsLogin"));
    }

    @Test
    public void testMQ() throws Exception {
        while (true) {
            Thread.sleep(1000);
            System.out.println("测试MQ");
        }
    }
}
