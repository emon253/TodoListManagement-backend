package com.todolist.services;

import com.todolist.dto.TodoDto;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface TodoService {

	TodoDto save(TodoDto todoDto);

	List<TodoDto> findAll();

	TodoDto update(Long todoID);

	void delete(Long todoID);

	public boolean uploadImage(Long todoId, String image);
}
