package com.avs.shop.service;

import com.avs.shop.dao.UserRepository;
import com.avs.shop.domain.User;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceImplTest {

    private UserServiceImpl userService;
    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;

    @BeforeAll
    static void beforeALL(){
        System.out.println("Before all tests");
    }

    @BeforeEach
    void setUP() {
        System.out.println("Before each tests");
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userRepository = Mockito.mock(UserRepository.class);
        userService = new UserServiceImpl(userRepository, passwordEncoder);
    }

    @AfterEach
    void afterEach(){
        System.out.println("After each tests");
    }

    @AfterAll
    static void afterAll(){
        System.out.println("After all tests");
    }

    @Test
    void checkFIndByName(){
        String name = "Tom";
        User expectedUser = User.builder().id(1l).name(name).build();
        Mockito.when(userRepository.findFirstByName(Mockito.any())).thenReturn(expectedUser);
        User actualUser = userService.findByName(name);
        Assertions.assertNotNull(actualUser);
        Assertions.assertEquals(expectedUser, actualUser);
    }

}
