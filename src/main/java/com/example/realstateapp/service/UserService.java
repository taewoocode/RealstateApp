package com.example.realstateapp.service;

import com.example.realstateapp.model.User;
import com.example.realstateapp.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void save(User user) {
        userRepository.save( user );
    }

    public void delete(User user) {
        userRepository.delete( user );
    }

    public void

}
