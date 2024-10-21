package com.example.realstateapp.controller;

import com.example.realstateapp.model.User;
import com.example.realstateapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    //update
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createUser = userService.createUser( user );
        return new ResponseEntity<>( createUser, HttpStatus.CREATED );
    }

    //Get User by Id
    @GetMapping
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User userById = userService.getUserById( id );
        return ResponseEntity.ok( userById );
    }

    //Get User By Email
    @GetMapping
    public ResponseEntity<User> getUserByEmail(@RequestParam String email) {
        User userByEmail = userService.getUserByEmail( email );
        return ResponseEntity.ok( userByEmail );
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        User user = userService.updateUser( id, userDetails );
        return ResponseEntity.ok( user );
    }

    // Delete a User by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();  // 204 No Content
    }
}
