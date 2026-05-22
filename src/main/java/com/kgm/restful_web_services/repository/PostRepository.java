package com.kgm.restful_web_services.repository;

import com.kgm.restful_web_services.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface PostRepository extends JpaRepository<Post,Long> {

    Page<Post> findByUserId(Long userId, Pageable pageable);

    List<Post> findAllPostByUserId(Long userId);

    List<Post> findByCategoryId(Long categoryId);
}
