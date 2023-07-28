package com.todolist.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.todolist.entity.Todo;
import com.todolist.entity.User;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {

    List<Todo> findByUser(User user);
}
