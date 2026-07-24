package com.filmtokio.Repositorys;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.filmtokio.Entities.User;

public interface UserRepository extends CrudRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.email = :emailOrUsername OR u.username = :emailOrUsername")
    public Optional<User> findByEmailOrUsername(String emailOrUsername);

    public boolean existsByUsername(String username);

    public boolean existsByEmail(String email);

}
