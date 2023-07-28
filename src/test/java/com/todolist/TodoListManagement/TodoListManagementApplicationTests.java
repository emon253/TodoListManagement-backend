package com.todolist.TodoListManagement;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.todolist.dto.TodoDto;
import com.todolist.entity.Todo;
import com.todolist.repositories.TodoRepository;
import com.todolist.services.TodoServiceImpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest

class TodoListManagementApplicationTests {
	@InjectMocks
	private TodoServiceImpl todoService;

	@Mock
	private TodoRepository todoRepository;

	@Test
	public void testFindAll() {
		List<Todo> todoList = new ArrayList<>();

		when(todoRepository.findAll()).thenReturn(todoList);

		List<TodoDto> result = todoService.findAll();

		assertNotNull(result);
		assertEquals(todoList.size(), result.size());

	}

	


}
