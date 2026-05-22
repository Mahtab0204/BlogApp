package com.kgm.restful_web_services.controller;

import com.kgm.restful_web_services.dto.CategoryDTO;
import com.kgm.restful_web_services.dto.PostDTO;
import com.kgm.restful_web_services.dto.PostDTOV2;
import com.kgm.restful_web_services.exception.NoSuchElementException;
import com.kgm.restful_web_services.exception.UserNotFoundException;
import com.kgm.restful_web_services.model.Category;
import com.kgm.restful_web_services.model.Post;
import com.kgm.restful_web_services.model.User;
import com.kgm.restful_web_services.repository.CategoryRepository;
import com.kgm.restful_web_services.repository.PostRepository;
import com.kgm.restful_web_services.repository.UserRepository;
import com.kgm.restful_web_services.response.PostResponse;
import com.kgm.restful_web_services.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
@Tag(
        name = "CRUD REST APIs for Post Resource"
)
public class PostController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;


    @Operation(
            summary = "Get all Post  REST API",
            description = "Get all Post  REST API is used to get a particular users all post from the database "
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 OK"
    )
    @GetMapping("/v1/users/{userId}/posts")
    public ResponseEntity<List<PostDTO>> getAllPostForUser(@PathVariable Long userId) {

        List<PostDTO> posts = postService.getAllPostByUserId(userId);
        return new ResponseEntity<>(posts,HttpStatus.OK);
    }

    @Operation(
            summary = "Get all Post by pagination & sorting  REST API",
            description = "Get all Post with pagination & sorting  REST API is used to get a particular users all post with paging and sorting from the database "
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 OK"
    )
    @GetMapping("/v1/users/{id}/posts/paginated")
    public ResponseEntity<PostResponse> getAllPostForUserWithPaginationAndSorting(
            @PathVariable Long id,
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize",defaultValue = "5", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir

    ){
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                    : Sort.by(sortBy).descending();

        User user = userRepository.findById(id)
                .orElseThrow(()->new UserNotFoundException("User not found with Id: "+id));

        Pageable pageable = PageRequest.of(pageNo,pageSize, sort);

        Page<Post> posts = postRepository.findByUserId(id,pageable);


        List<Post> content= posts.getContent();


        PostResponse response = new PostResponse();
        response.setContent(content);
        response.setPageNo(posts.getNumber());
        response.setPageSize(posts.getSize());
        response.setTotalPages(posts.getTotalPages());
        response.setTotalElements(posts.getTotalElements());
        response.setLast(posts.isLast());


        if(posts.isEmpty()){
            throw new NoSuchElementException("No post found for userId : "+id);
        }

        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @Operation(
            summary = "Get Post by Id REST API",
            description = "Get Post by Id REST API is used to get single  post from the database "
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 OK"
    )

    // Versioning through content negotiation => @GetMapping("/users/{userId}/posts/{postId}, produces = "application/vnd.kgmgroup.v1+json")
    // Versioning through custom headers => @GetMapping("/users/{userId}/posts/{postId}, headers = "X-API-VERSION=1")
    // Versioning through query parameters => @GetMapping("/users/{userId}/posts/{postId}, params = "version=1")
    // Versioning through URI path
    @GetMapping("/v1/users/{userId}/posts/{postId}")
    public ResponseEntity<PostDTO> getOnePostForUserV1(
            @PathVariable Long userId,
            @PathVariable Long postId) {
        PostDTO posts = postService.getOnePostByUserId(userId,postId);
        if(posts==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(posts,HttpStatus.OK);
    }

    // Versioning through content negotiation => @GetMapping("/users/{userId}/posts/{postId}, produces = "application/vnd.kgmgroup.v2+json")
    // Versioning through custom headers => @GetMapping("/users/{userId}/posts/{postId}, headers = "X-API-VERSION=2")
    // Versioning through query parameters => @GetMapping("/users/{userId}/posts/{postId}, params = "version=2")
    // Versioning through URI path
    @GetMapping("/v2/users/{userId}/posts/{postId}")
    public ResponseEntity<PostDTOV2> getOnePostForUserV2(
            @PathVariable Long userId,
            @PathVariable Long postId) {
        PostDTO posts = postService.getOnePostByUserId(userId,postId);
        PostDTOV2 postDTOV2 = new PostDTOV2();
        postDTOV2.setId(postId);
        postDTOV2.setTitle(posts.getTitle());
        postDTOV2.setDescription(posts.getDescription());
        postDTOV2.setComments(posts.getComments());
        postDTOV2.setCategoryId(posts.getCategoryId());

        List<String> tags = new ArrayList<>();
        tags.add("Java");
        tags.add("Spring Boot");
        tags.add("C++");
        postDTOV2.setTags(tags);

        if(postDTOV2==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(postDTOV2,HttpStatus.OK);
    }




    @Operation(
            summary = "Create Post REST API",
            description = "Create Post REST API is used to save post into database "
    )
    @ApiResponse(
            responseCode = "201",
            description = "Http Status 201 CREATED"
    )
    @SecurityRequirement(
            name = "Bearer Authorization"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/v1/create-post/{id}")
    @Transactional
    public ResponseEntity<PostDTO> createPostForUser(@Validated @PathVariable Long id, @RequestBody PostDTO postDTO){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User Id: " + id));

        Category category = categoryRepository.findById(postDTO.getCategoryId())
                .orElseThrow(()->new ResourceNotFoundException("Category not found with id : "+postDTO.getCategoryId()));

        Post posts = modelMapper.map(postDTO,Post.class);
        posts.setUser(user);
        posts.setCategory(category);
        Post savedPost = postRepository.save(posts);

        return new ResponseEntity<>(modelMapper.map(savedPost,PostDTO.class),HttpStatus.CREATED);
    }


    @Operation(
            summary = "Delete all Post  REST API",
            description = "Delete all Post  REST API is used to delete a particular users all post  from the database "
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 OK"
    )
    @SecurityRequirement(
            name = "Bearer Authorization"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/v1/delete-post/{userId}")
    @Transactional
    public ResponseEntity<?>deleteALlPostForUser(@PathVariable Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User Id: " + userId));

        List<Post> post= user.getPosts();
        if(post.isEmpty()){
            throw new NoSuchElementException("UserId :"+userId);
        }
        postRepository.deleteAll(post);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(
            summary = "Delete Post  REST API",
            description = "Delete Post  REST API is used to delete a particular user post  from the database "
    )
    @ApiResponse(
            responseCode = "204",
            description = "Http Status 204 NO_CONTENT"
    )

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/v1/delete-post/{userId}/post/{postId}")
    @Transactional
    public ResponseEntity<?>deleteOnePostForUser(@PathVariable Long userId,@PathVariable Long postId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User Id: " + userId));
        Post post=postRepository.findById(postId)
                .orElseThrow(()-> new UserNotFoundException("No such element found for this postId: "+postId));
        if(!post.getUser().getId().equals(user.getId())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        postRepository.deleteById(postId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(
            summary = "Update Post  REST API",
            description = "Update Post  REST API is used to update a particular user post  from the database "
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 OK"
    )
    @SecurityRequirement(
            name = "Bearer Authorization"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/v1/update-post/{userId}/post/{postId}")
    public ResponseEntity<PostDTO>updateOnePostForUser(@Validated @PathVariable Long userId,
                                                          @PathVariable Long postId,
                                                          @RequestBody PostDTO newPost){

        PostDTO updatedPost = postService.updatePostByUserId(userId,postId,newPost);

        if(updatedPost==null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(updatedPost,HttpStatus.OK);
    }

    //Build get post by category REST API

    @Operation(
            summary = "Get Post by Category REST API",
            description = "Get Post by Category  REST API is used to get all post of a particular category from the database "
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 OK"
    )
    @GetMapping("/v1/get-all-posts/category/{categoryId}")
    public ResponseEntity<List<PostDTO>> getPostsByCategory(@PathVariable  Long categoryId){

        List<PostDTO> posts = postService.getPostByCategory(categoryId);
        return new ResponseEntity<>(posts,HttpStatus.OK);
    }
}
