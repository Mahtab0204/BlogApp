package com.kgm.restful_web_services.repository;

import com.kgm.restful_web_services.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {


    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    void deleteById(Long id);

    Page<User> findAll(Pageable pageable);

}
