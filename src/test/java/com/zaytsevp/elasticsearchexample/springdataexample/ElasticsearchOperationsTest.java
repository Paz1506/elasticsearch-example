/*
 * Copyright 2014-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zaytsevp.elasticsearchexample.springdataexample;

import com.zaytsevp.elasticsearchexample.springdataexample.model.Conference;
import com.zaytsevp.elasticsearchexample.springdataexample.model.Post;
import com.zaytsevp.elasticsearchexample.springdataexample.repository.ConferenceRepository;
import com.zaytsevp.elasticsearchexample.springdataexample.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Пример работы с ES-репозиториями и
 * интерфейсом ElasticsearchOperations
 *
 * @author Pavel Zaytsev
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Config.class)
public class ElasticsearchOperationsTest {

    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    ElasticsearchOperations operations;

    @Autowired
    ConferenceRepository conferenceRepository;

    @Autowired
    PostRepository postRepository;

    /** поиск конференций по параметрам и ключевым словам */
    @Test
    public void textConferenceSearch() throws ParseException {
        // Prepare
        String expectedDate = "2014-10-29";
        String expectedWord = "java";
        CriteriaQuery query = new CriteriaQuery(
                new Criteria("keywords").contains(expectedWord).and(new Criteria("date").greaterThanEqual(expectedDate)));

        // Actual
        List<Conference> result = operations.queryForList(query, Conference.class);

        // Assertion
        assertThat(result, hasSize(3));

        for (Conference conference : result) {
            assertThat(conference.getKeywords(), hasItem(expectedWord));
            assertThat(format.parse(conference.getDate()), greaterThan(format.parse(expectedDate)));
        }
    }

    /** поиск конференций в диаазоне от заданной координаты */
    @Test
    public void geoSpatialConferenceSearch() {
        // Prepare
        GeoPoint startLocation = new GeoPoint(50.0646501D, 19.9449799D);
        String range = "330mi"; // or 530km
        CriteriaQuery query = new CriteriaQuery(new Criteria("location").within(startLocation, range));

        // Actual
        List<Conference> result = operations.queryForList(query, Conference.class);

        // Assertion
        assertThat(result, hasSize(2));
    }

    /** количество конференций */
    @Test
    public void testConferenceRepositoryCount() {
        // Assertion
        assertEquals(5L, conferenceRepository.count());
    }

    /** нечёткий поиск по наименованию */
    @Test
    public void testFuzzySearchConferenceQuery() {
        // Prepare
        SearchQuery query = new NativeSearchQueryBuilder()
                .withFilter(QueryBuilders.fuzzyQuery("name", "echange"))
                .build();

        // Act
        List<String> strings = operations.queryForIds(query);

        log.info("Fuzzy search results:");
        strings.forEach(c -> {
            Conference byId = conferenceRepository.findById(c).orElse(null);
            log.info("Fuzzy search result: {}", byId);
        });

        // Assertion
        assertEquals(2L, strings.size());
    }

    /** запрос к индексу zps (нечёткий поиск по заголовку статьи) */
    @Test
    public void testFuzzySearchPostQuery() {
        // Prepare
        SearchQuery query = new NativeSearchQueryBuilder()
                .withFilter(QueryBuilders.fuzzyQuery("title", "spring"))
                .build();

        // Actual
        List<String> strings = operations.queryForIds(query);

        // Assertion
        log.info("Fuzzy search results (zps index):");
        strings.forEach(c -> {
            Post byId = postRepository.findById(c).orElse(null);
            assertEquals(byId.getAuthor(), "Rambabu Posa");
            log.info("Fuzzy search result (zps index): {}", byId);
        });

        assertEquals(3L, strings.size());
    }

    /** количество записей в индексе zps */
    @Test
    public void testCountPosts() {
        // Assertion
        assertEquals(4L, postRepository.count());
    }

    // Тесты PostRepository

    /** поиск по автору */
    @Test
    public void testFindByAuthor() {
        // Actual
        Page<Post> actualResult = postRepository.findByAuthor("Rambabu Posa", PageRequest.of(0, 1));

        // Assertion
        assertEquals(actualResult.getTotalElements(), 4L);
    }

    /** поиск по заголовку */
    @Test
    public void testFindByTitle() {
        // Actual
        List<Post> actualResult = postRepository.findByTitleContaining("Data");

        // Assertion
        assertEquals(actualResult.size(), 2L);
    }
}
