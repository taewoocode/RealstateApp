package com.example.realstateapp.service;

import com.example.realstateapp.model.User;
import com.example.realstateapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        return userRepository.save( user );
    }

    public User getUserById(Long id) {
        return userRepository.findById( id )
                .orElseThrow( () -> new IllegalArgumentException( "해당 사용자를 찾을 수 없다." ) );
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail( email )
                .orElseThrow( () -> new IllegalArgumentException( "해당 이메일을 찾을 수 없습니다." ) );
    }

    public User updateUser(Long id, User userDetails) {
        User user = getUserById( id );
        user.setName( userDetails.getName() );
        user.setEmail( userDetails.getEmail() );
        user.setPassword( userDetails.getPassword() );
        return userRepository.save( user );
    }

    public void delete(Long id) {
        User user = getUserById( id );
        userRepository.delete( user );
    }
}
