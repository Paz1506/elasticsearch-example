package com.zaytsevp.elasticsearchexample.springdataexample;


import com.zaytsevp.elasticsearchexample.springdataexample.model.Conference;
import com.zaytsevp.elasticsearchexample.springdataexample.repository.ConferenceRepository;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

/**
 * Класс конфигурации для ES
 *
 * @author Pavel Zaytsev
 */
@Configuration
@EnableElasticsearchRepositories
class Config {

    @Autowired
    private ElasticsearchOperations operations;

    @Autowired
    private ConferenceRepository conferenceRepository;

    private String clusterName = "elasticsearch";

    private int port = 9300;

    private String hostname = "127.0.0.1";

    /** For embedded ES instance */
    /*@Bean
    public NodeClientFactoryBean client() {

		NodeClientFactoryBean bean = new NodeClientFactoryBean(true);
		bean.setClusterName(UUID.randomUUID().toString());
		bean.setEnableHttp(false);
		bean.setPathData("target/elasticsearchTestData");
		bean.setPathHome("src/test/resources/test-home-dir");

		return bean;
	}*/

    /** For real ES instance */
    @Bean
    public Client client() throws UnknownHostException {
        Settings elasticsearchSettings = Settings.builder()
                                                 .put("client.transport.sniff", true)
                                                 .put("client.transport.ignore_cluster_name", false)
                                                 .put("cluster.name", clusterName)
                                                 .build();

        TransportClient client = new PreBuiltTransportClient(elasticsearchSettings);
        client.addTransportAddress(new TransportAddress(InetAddress.getByName(hostname), port));

        return client;
    }

    @Bean
    public ElasticsearchTemplate elasticsearchTemplate(Client client) throws Exception {
        return new ElasticsearchTemplate(client);
    }

    @PreDestroy
    public void deleteIndex() {
        operations.deleteIndex(Conference.class);
    }

    @PostConstruct
    public void insertDataSample() {

        // Remove all documents
        conferenceRepository.deleteAll();
        operations.refresh(Conference.class);

        // Save data sample
        conferenceRepository.save(Conference.builder()
                                            .date("2014-11-06")
                                            .name("Spring eXchange 2014 - London")
                                            .keywords(Arrays.asList("java", "spring"))
                                            .location(new GeoPoint(51.500152D, -0.126236D))
                                            .build());

        conferenceRepository.save(Conference.builder().date("2014-12-07")
                                            .name("Scala eXchange 2014 - London")
                                            .keywords(Arrays.asList("scala", "play", "java"))
                                            .location(new GeoPoint(51.500152D, -0.126236D))
                                            .build());

        conferenceRepository.save(Conference.builder().date("2014-11-20")
                                            .name("Elasticsearch 2014 - Berlin")
                                            .keywords(Arrays.asList("java", "elasticsearch", "kibana"))
                                            .location(new GeoPoint(52.5234051D, 13.4113999))
                                            .build());

        conferenceRepository.save(Conference.builder().date("2014-11-12")
                                            .name("AWS London 2014")
                                            .keywords(Arrays.asList("cloud", "aws"))
                                            .location(new GeoPoint(51.500152D, -0.126236D))
                                            .build());

        conferenceRepository.save(Conference.builder().date("2014-10-04")
                                            .name("JDD14 - Cracow")
                                            .keywords(Arrays.asList("java", "spring"))
                                            .location(new GeoPoint(50.0646501D, 19.9449799))
                                            .build());
    }
}
