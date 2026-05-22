package com.kgm.restful_web_services.service;

import com.kgm.restful_web_services.model.User;
import com.kgm.restful_web_services.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImplementation {

    @Autowired
    private UserRepository userRepository;

    public Page<User> getUsers(int page, int size) {

        PageRequest pageable = PageRequest.of(page, size);

        return userRepository.findAll(pageable);
    }
}
