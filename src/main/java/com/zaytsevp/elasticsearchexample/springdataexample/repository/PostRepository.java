package com.zaytsevp.elasticsearchexample.springdataexample.repository;

import com.zaytsevp.elasticsearchexample.springdataexample.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * @author Pavel Zaytsev
 */
public interface PostRepository extends ElasticsearchRepository<Post, String> {

    Page<Post> findByAuthor(String author, Pageable pageable);

    List<Post> findByTitleContaining(String title);

}