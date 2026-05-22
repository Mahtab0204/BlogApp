package com.kgm.restful_web_services.service;

import com.kgm.restful_web_services.dto.PostDTO;
import com.kgm.restful_web_services.exception.NoSuchElementException;
import com.kgm.restful_web_services.exception.UserNotFoundException;
import com.kgm.restful_web_services.model.Category;
import com.kgm.restful_web_services.model.Post;
import com.kgm.restful_web_services.model.User;
import com.kgm.restful_web_services.repository.CategoryRepository;
import com.kgm.restful_web_services.repository.PostRepository;
import com.kgm.restful_web_services.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PostServiceImplementation implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<PostDTO> getAllPostByUserId(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User Id: " + userId));

        List<Post> posts = postRepository.findAllPostByUserId(userId);

        if(posts.isEmpty()){
            throw new NoSuchElementException("Post not found for userID: "+userId);
        }

        return posts.stream().map(this::mapToPostDTO).collect(Collectors.toList());
    }

    @Override
    public PostDTO getOnePostByUserId(Long userId, Long postId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User Id: " + userId));

        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new NoSuchElementException("Post not found for this postId :"+postId));

        if(!user.getId().equals(post.getUser().getId())){
           throw new NoSuchElementException("Post not found with postId :"+postId +"  for userID :"+userId);
        }
        return mapToPostDTO(post);
    }

    @Override
    public PostDTO updatePostByUserId(Long userId, Long postId, PostDTO postDTO) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User Id: " + userId));
        Post posts=postRepository.findById(postId)
                .orElseThrow(()-> new UserNotFoundException("No such element found for this postId: "+postId));

        Category category = categoryRepository.findById(postDTO.getCategoryId())
                .orElseThrow(()->new ResourceNotFoundException("Category not found with id : "+postDTO.getCategoryId()));

        if(!posts.getUser().getId().equals(user.getId())){
            throw new NoSuchElementException("Post not found with postId :"+postId +"  for userID :"+userId);
        }

        Post newPost = mapToPost(postDTO);
        posts.setTitle(newPost.getTitle());
        posts.setDescription(newPost.getDescription());
        posts.setCategory(category);
        Post updatedPost=postRepository.save(posts);

        return mapToPostDTO(updatedPost);
    }

    @Override
    public List<PostDTO> getPostByCategory(Long categoryId) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()->new ResourceNotFoundException("Category not found with id : "+categoryId));

        List<Post> posts = postRepository.findByCategoryId(categoryId);

        return posts.stream().map(post->mapToPostDTO(post))
                .collect(Collectors.toList());

    }


    private PostDTO mapToPostDTO(Post post){
        return modelMapper.map(post,PostDTO.class);
    }

    private Post mapToPost(PostDTO postDTO){
        return modelMapper.map(postDTO,Post.class);
    }

}
