package com.kgm.restful_web_services.service;

import com.kgm.restful_web_services.dto.CommentDTO;

import java.util.List;

public interface CommentService {
    CommentDTO createComment(Long postId,CommentDTO commentDTO) throws Exception;

    List<CommentDTO> getAllCommentsByPostId(Long postId)throws Exception;

    CommentDTO getOneCommentFromPost(Long postId,Long commentId) throws Exception;

    CommentDTO updateComment(Long postId,Long commentId,CommentDTO commentDTO) throws Exception;

    boolean deleteOneComment(Long postId,Long commentId) throws Exception;

    boolean deleteAllCommentsByPostId(Long postId) throws Exception;
}
