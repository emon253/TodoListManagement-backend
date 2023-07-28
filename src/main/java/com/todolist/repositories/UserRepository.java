package com.todolist.repositories;

import com.todolist.entity.User;
import com.todolist.services.UserServiceImpl;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    public Optional<User> findByUserName(String userName);

}
