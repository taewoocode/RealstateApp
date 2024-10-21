package com.example.realstateapp.service;

import com.example.realstateapp.model.User;
import com.example.realstateapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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

    //회원가입 로직추가
    public User createUser(User user) {
        Optional<User> findByEmail = userRepository.findByEmail( user.getEmail() );
        if (findByEmail.isPresent()) {
            throw new IllegalArgumentException( "이미 등록된 이메일입니다." );
        }
        return userRepository.save( user );
    }
}
