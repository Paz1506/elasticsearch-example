package com.zaytsevp.elasticsearchexample.springdataexample.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * Публикация
 *
 * @author Pavel Zaytsev
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "zps", type = "posts")
public class Post {

    @Id
    private String id;

    @Field(type = FieldType.Text)
    private String title;

    @Field(type = FieldType.Text)
    private String category;

    @Field(type = FieldType.Text)
    private String publishedDate;

    @Field(type = FieldType.Text)
    private String author;
}
