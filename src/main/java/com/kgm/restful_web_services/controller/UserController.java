package com.kgm.restful_web_services.controller;

import com.kgm.restful_web_services.exception.UserNotFoundException;
import com.kgm.restful_web_services.model.User;
import com.kgm.restful_web_services.repository.UserRepository;
import com.kgm.restful_web_services.service.UserServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/app")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserServiceImplementation userServiceImplementation;

    @Autowired
    private MessageSource messageSource;

    public UserController(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @GetMapping("/jpa/users")
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }


    // pagination

    @GetMapping("/jpa/all-users")
    public Page<User> getAllUsers(
            @RequestParam int pageNo,
            @RequestParam int pageSize){

        return userServiceImplementation.getUsers(pageNo,pageSize);
    }
/*
    @GetMapping("/user/{id}")
    public ResponseEntity<User> findOneUser(@PathVariable int id) {
        User user = userDaoService.findUserById(id);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } */

    //EntityModel
    //WebMvcLinkBuilder
    @GetMapping("/jpa/user/{id}")
    public EntityModel<User> findOneUser(@PathVariable Long id) {
        Optional<User> optionalUser = userRepository.findById(id);

        User user = optionalUser
                .orElseThrow(() -> new UserNotFoundException("id: " + id));

        EntityModel<User> entityModel = EntityModel.of(user);

        WebMvcLinkBuilder link = linkTo(methodOn(this.getClass()).findAllUsers());
        entityModel.add(link.withRel("all-users"));

        return entityModel;
    }

//    /*
//    @PostMapping("/create-user")
//    public ResponseEntity<User> createUser(@RequestBody User user){
//        if(user!=null){
//            userDaoService.save(user);
//            return new ResponseEntity<>(user,HttpStatus.CREATED);
//        }
//        throw new InputMismatchException("Invalid User Input");
//    } */
//
//    @PostMapping("/jpa/create-user")
//    public ResponseEntity<User> createUser(@Validated @RequestBody User user) {
//        User savedUser = userRepository.save(user);
//
//        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
//                .path("/{id}")
//                .buildAndExpand(savedUser.getId())
//                .toUri();
//        return ResponseEntity.created(location).build();
//
//    }

    @DeleteMapping("/jpa/delete-user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id){
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()){
            userRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

//    @DeleteMapping("/jpa/delete-user/{id}")
//    public ResponseEntity<?> deleteUser(@PathVariable int id){
//      if(userRepository.deleteByid(id)) {
//          return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//      }
//      throw new UserNotFoundException("id:"+id);
//    }

    /*
    @PutMapping("/jpa/update-user/{id}")
    public ResponseEntity<User> updateUser(@Validated @RequestBody User user,@PathVariable Long id){
        User oldUser= userRepository.findById(id).orElseThrow(()->new UserNotFoundException("id : "+id));
        oldUser.setName(user.getName());
        oldUser.setId(user.getId());
       // oldUser.setBirthDate(user.getBirthDate());
        User updatedUser= userRepository.save(oldUser);

        return new ResponseEntity<>(updatedUser,HttpStatus.OK);
    }
    */
/*
    @GetMapping("/hello-world-i18n")
    public String helloWorldi18n(){

        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage("good.morning.message",null,"Default Message",locale);

    }  */


}