package com.dmall.blas.collect.factory;

import com.dmall.tool.nb.web.SpringAppContextAware;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;

/**
 * 用于创建ES的TransportClient对象
 *
 * @Author qiang.shao
 * @Email qiang.shao@dmall.com
 * @Date 2017/3/29
 */
public class ESTransportClientFactory {

    private static Logger LOGGER = LoggerFactory.getLogger(ESTransportClientFactory.class);

    /**
     * 获取ES的TransportClient对象
     *
     * @return
     */
    public static TransportClient getESClient() {
        return SpringAppContextAware.getSpringBean(TransportClient.class);
    }

    /**
     * 创建ES的TransportClient对象
     *
     * @param serverIp    es服务器IP
     * @param port        es服务器端口号
     * @param clusterName es服务器所在集群名称
     * @return
     */
    public static TransportClient createESClient(String serverIp, int port, String clusterName) {

        try {
            Settings settings = Settings.settingsBuilder().put("cluster.name", clusterName).build();

            TransportClient elasticSearchClient = TransportClient.builder()
                    .settings(settings)
                    .build()
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(serverIp), port));

            LOGGER.info("es client初始化成功，client={}", elasticSearchClient);
            return elasticSearchClient;
        } catch (Exception e) {
            LOGGER.error("初始化es client失败，e={}", e);
        }

        return null;
    }

}
