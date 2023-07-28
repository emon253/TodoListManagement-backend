package com.todolist.controllers;

import com.todolist.dto.TodoDto;
import com.todolist.entity.Todo;
import com.todolist.repositories.TodoRepository;
import com.todolist.services.TodoService;
import com.todolist.services.TodoServiceImpl;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.validation.Valid;
import javax.xml.bind.DatatypeConverter;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowedHeaders = "*")

public class TodoController {

	static final Logger logger = Logger.getLogger(TodoServiceImpl.class.getName());

	@Autowired
	private TodoService todoService;

	@PostMapping("/v1/save-todo")
	public ResponseEntity<TodoDto> save(@Valid @RequestBody TodoDto todoDto) {
		logger.log(Level.INFO, "Starting todo save operation...");

		TodoDto savedTodo = this.todoService.save(todoDto);
		return new ResponseEntity<>(savedTodo, HttpStatus.CREATED);

	}

	@GetMapping("/v2/find-all-todo")
	public ResponseEntity<List<TodoDto>> findAll() {
		logger.log(Level.INFO, "calling service to retrieve todos    ...");

		List<TodoDto> allTodo = this.todoService.findAll();
		return ResponseEntity.ok(allTodo);
	}

	@PutMapping("/v2/update-todo")
	public ResponseEntity<TodoDto> update(@RequestBody Long todoID) {
		logger.log(Level.INFO, "calling service update todo status ...");

		TodoDto updatedTodo = this.todoService.update(todoID);
		return new ResponseEntity<>(updatedTodo, HttpStatus.CREATED);

	}

	@DeleteMapping("/v1/delete-todo/{userID}")
	public ResponseEntity<?> delete(@PathVariable("userID") Long todoID) {
		logger.log(Level.INFO, "calling service to delete todo status ...");

		this.todoService.delete(todoID);
		return ResponseEntity.ok(HttpStatus.OK);
	}

	@PutMapping("/v1/upload-image/{todoID}")
	public ResponseEntity<?> uploadImage(@PathVariable Long todoID, @RequestBody String image) {
		logger.log(Level.INFO, "calling service to upload image ...");

		boolean uploaded = this.todoService.uploadImage(todoID, image);
		return new ResponseEntity<>(uploaded, HttpStatus.OK);

	}

}
