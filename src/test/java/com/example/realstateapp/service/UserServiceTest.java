package com.example.realstateapp.service;

import com.example.realstateapp.model.User;
import com.example.realstateapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.verification.VerificationMode;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks( this );
    }

    @Test
    @DisplayName("유저 생성 테스트")
    void successCreateUser() {

        //given
        User user = new User();
        user.setName( "pppptttwwww" );
        user.setEmail( "qweqwe@qweqwe" );

        //when
        when( userRepository.save( user ) ).thenReturn( user );
        User createUser = userService.createUser( user );

        //then
        assertNotNull( createUser );
    }

    @Test
    @DisplayName("유저 삭제 테스트")
    void successDeleteUser() {
        User user = new User();
        user.setId( 1L );
        user.setName( "Taewoo" );

        when( userRepository.findById( 1L ) ).thenReturn( Optional.of( user ) );
        doNothing().when( userRepository ).delete( user );

        //when
        userService.delete( 1L );
        //then
        Mockito.verify(userRepository, times(1)).findById(1L);
        Mockito.verify(userRepository, times(1)).delete(user);
    }
}