package com.todolist.services;

import com.todolist.entity.User;
import com.todolist.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService,UserDetailsService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public User loadUserByUsername(String username) {
        User user = this.userRepository.findByUserName(username).orElseThrow(()
                -> new UsernameNotFoundException("User information with userName " + username + " not found"));
        return user;
    }

}
