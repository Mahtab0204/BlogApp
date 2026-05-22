package com.kgm.restful_web_services.service;

import com.kgm.restful_web_services.dto.CommentDTO;
import com.kgm.restful_web_services.exception.NoSuchElementException;
import com.kgm.restful_web_services.model.Comment;
import com.kgm.restful_web_services.model.Post;
import com.kgm.restful_web_services.repository.CommentRepository;
import com.kgm.restful_web_services.repository.PostRepository;
import com.kgm.restful_web_services.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentServiceImplementation implements CommentService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    @Transactional
    public CommentDTO createComment(Long postId, CommentDTO commentDTO) throws Exception {

        Post post = postRepository.findById(postId)
                .orElseThrow(()->new ResourceNotFoundException("Post not found with id: "+postId));

        Comment comment = mapToEntity(commentDTO);
        comment.setPost(post);
        post.getComments().add(comment);
        Comment newComment = commentRepository.save(comment);

        return mapToDTO(newComment);
    }

    @Override
    @Transactional
    public List<CommentDTO> getAllCommentsByPostId(Long postId) throws Exception {

        Post post = postRepository.findById(postId)
                .orElseThrow(()->new ResourceNotFoundException("Post not found with id: "+postId));

       // post.getComments().size();
        List<Comment> comments= commentRepository.findByPostId(post.getId());
        return comments.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentDTO getOneCommentFromPost(Long postId, Long commentId) throws Exception {

        Post post = postRepository.findById(postId)
                .orElseThrow(()->new ResourceNotFoundException("Post not found with id: "+postId));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()->new ResourceNotFoundException("Comment not found with id : "+commentId));

        if(!post.getId().equals(comment.getPost().getId())){

            throw new NoSuchElementException("Comment not found for this Post");
        }

        return mapToDTO(comment);
    }


    @Override
    @Transactional
    public CommentDTO updateComment(Long postId, Long commentId, CommentDTO commentDTO) throws Exception {

        CommentDTO oldCommentDTO = getOneCommentFromPost(postId,commentId);

        Comment comment = mapToEntity(oldCommentDTO);

        Post post = postRepository.findById(postId)
                .orElseThrow(()->new ResourceNotFoundException("Post not found with id: "+postId));

        //comment.setId(commentId);
        comment.setName(commentDTO.getName());
        comment.setEmail(commentDTO.getEmail());
        comment.setBody(commentDTO.getBody());
        comment.setPost(post);
        commentRepository.save(comment);

        return mapToDTO(comment);
    }

    @Override
    public boolean deleteOneComment(Long postId, Long commentId) throws Exception {

        CommentDTO commentDTO = getOneCommentFromPost(postId,commentId);

        if(commentDTO==null){
            throw new NoSuchElementException("No comment found");
        }

        commentRepository.deleteById(commentId);
        return true;
    }

    @Override
    @Transactional
    public boolean deleteAllCommentsByPostId(Long postId) throws Exception {


        Post post = postRepository.findById(postId)
                .orElseThrow(()->new ResourceNotFoundException("Post not found with id: "+postId));

        commentRepository.deleteAllCommentsByPostId(postId);
        return true;
    }


    private CommentDTO mapToDTO(Comment comment){

        return modelMapper.map(comment,CommentDTO.class);

//        CommentDTO commentDTO = new CommentDTO();
//        commentDTO.setId(comment.getId());
//        commentDTO.setName(comment.getName());
//        commentDTO.setEmail(comment.getEmail());
//        commentDTO.setBody(comment.getBody());

    }

    private Comment mapToEntity(CommentDTO commentDTO){

        return modelMapper.map(commentDTO,Comment.class);

//        Comment comment = new Comment();
//        comment.setId(commentDTO.getId());
//        comment.setName(commentDTO.getName());
//        comment.setEmail(commentDTO.getEmail());
//        comment.setBody(commentDTO.getBody());
    }
}
