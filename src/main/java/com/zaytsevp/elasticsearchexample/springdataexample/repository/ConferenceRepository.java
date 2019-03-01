package com.zaytsevp.elasticsearchexample.springdataexample.repository;

import com.zaytsevp.elasticsearchexample.springdataexample.model.Conference;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author Pavel Zaytsev
 */
public interface ConferenceRepository extends ElasticsearchRepository<Conference, String> {
}
