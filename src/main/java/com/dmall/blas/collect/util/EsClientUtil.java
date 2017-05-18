package com.dmall.blas.collect.util;

import java.net.InetAddress;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Administrator on 2016/4/1.
 */
public class EsClientUtil {
	private static final Logger logger = LoggerFactory.getLogger(EsClientUtil.class);
	private static TransportClient client = null;

	public static TransportClient getEsClient(String host, Integer port, String clusterName) {
		if (client == null) {
			synchronized (EsClientUtil.class) {
				if (client == null) {
					try {
						Settings settings = Settings.settingsBuilder().put("cluster.name", clusterName)
								.build();
						client = TransportClient.builder().settings(settings).build()
								.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host), port));
						logger.info("es client初始化成功，client={}", client);
					} catch (Exception e) {
						logger.error("初始化es client失败，e={}", e);
						return null;
					}
				}
			}
		}

		return client;
	}
}
