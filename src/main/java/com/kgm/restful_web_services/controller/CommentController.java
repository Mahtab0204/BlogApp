package com.kgm.restful_web_services.controller;

import com.kgm.restful_web_services.ResponseMessage.MessageResponse;
import com.kgm.restful_web_services.dto.CommentDTO;
import com.kgm.restful_web_services.repository.PostRepository;
import com.kgm.restful_web_services.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/app")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private PostRepository postRepository;

    @PostMapping("/jpa/comment/post/{postId}")
    public ResponseEntity<Object> createComment(
            @Validated
            @PathVariable(value = "postId") Long postId,
            @RequestBody CommentDTO commentDTO) throws Exception {

       CommentDTO savedCommentDTO = commentService.createComment(postId,commentDTO);
     //  return new ResponseEntity<>(savedCommentDTO, HttpStatus.CREATED);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedCommentDTO.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/jpa/all-comments/post/{postId}")
    public ResponseEntity<List<CommentDTO>> getCommentsForPost(
            @PathVariable Long postId) throws Exception {

        List<CommentDTO> commentDTOS = commentService.getAllCommentsByPostId(postId);
        return new ResponseEntity<>(commentDTOS,HttpStatus.OK);
    }

    @GetMapping("/jpa/get-comment/post/{postId}/comment/{commentId}")
    public ResponseEntity<CommentDTO> getOneComment(
            @PathVariable Long postId,
            @PathVariable Long commentId) throws Exception {

        CommentDTO commentDTO = commentService.getOneCommentFromPost(postId,commentId);
        if(commentDTO!=null){
            return new ResponseEntity<>(commentDTO,HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @PutMapping("/jpa/update-comment/post/{postId}/comment/{commentId}")
    public ResponseEntity<CommentDTO> updateComment(
            @Validated
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @RequestBody CommentDTO commentDTO) throws Exception {

        CommentDTO newCommentDTO = commentService.updateComment(postId,commentId,commentDTO);
        if(commentDTO!=null){
            return new ResponseEntity<>(commentDTO,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/jpa/delete-comment/post/{postId}/comment/{commentId}")
    public ResponseEntity<MessageResponse> deleteComment(
            @PathVariable Long postId,
            @PathVariable Long commentId) throws Exception {

        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setMessage("Comment deleted Successfully");
        if(commentService.deleteOneComment(postId,commentId)){
            return new ResponseEntity<>(messageResponse,HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/jpa/delete-all-comments/post/{postId}")
    public ResponseEntity<MessageResponse> deleteAllComments(
            @PathVariable Long postId ) throws Exception {

        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setMessage("All Comments deleted Successfully");

        if(commentService.deleteAllCommentsByPostId(postId)){
            return new ResponseEntity<>(messageResponse,HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }



}
