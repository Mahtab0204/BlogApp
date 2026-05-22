package com.kgm.restful_web_services.service;

import com.kgm.restful_web_services.dto.PostDTO;

import java.util.List;

public interface PostService {

    List<PostDTO> getAllPostByUserId(Long userId);

    PostDTO getOnePostByUserId(Long userId,Long postId);

    PostDTO updatePostByUserId(Long userId,Long postId,PostDTO postDTO);

    List<PostDTO> getPostByCategory(Long categoryId);
}
