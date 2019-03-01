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
package com.zaytsevp.elasticsearchexample.springdataexample.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import java.util.List;

import static org.springframework.data.elasticsearch.annotations.FieldType.Date;

/**
 * Конференция
 *
 * @author Pavel Zaytsev
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "conference-index", type = "geo-class-point-type", shards = 1, replicas = 0,
          refreshInterval = "-1")
public class Conference {

    @Id
    private String id;

    private String name;

    @Field(type = Date)
    private String date;

    private GeoPoint location;

    private List<String> keywords;
}
