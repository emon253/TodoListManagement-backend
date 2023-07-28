package com.todolist.services;

import com.todolist.entity.User;

public interface UserService {

    User loadUserByUsername(String username);

}
