package com.todolist.services;

import com.todolist.constants.Const;
import com.todolist.dto.TodoDto;
import com.todolist.entity.Todo;
import com.todolist.entity.User;
import com.todolist.exceptions.ResourceNotFoundException;
import com.todolist.repositories.TodoRepository;
import com.todolist.repositories.UserRepository;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.xml.bind.DatatypeConverter;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TodoServiceImpl implements TodoService {

	static final Logger logger = Logger.getLogger(TodoServiceImpl.class.getName());

	@Autowired
	private TodoRepository todoRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	@Transactional
	@Override
	public TodoDto save(TodoDto todoDto) {
		logger.log(Level.SEVERE, "Finding user for todo...");
		User createdBy = this.userRepository.findByUserName(todoDto.getCreatedBy()).orElseThrow(
				() -> new ResourceNotFoundException("Requested user ", "UserName " + todoDto.getCreatedBy()));

		Todo todo = this.converToTodo(todoDto);
		todo.setUser(createdBy);

		todo.setStatus(Const.TODO_STATUS_OPEN);

		Todo savedTodo = this.todoRepository.save(todo);
		logger.log(Level.SEVERE, "Todo successfully saved...");

		return this.converToDto(savedTodo);

	}

	private byte[] resizeImage(byte[] originalImageBytes) {
		try {

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			Thumbnails.of(new ByteArrayInputStream(originalImageBytes))
					.size(Const.IMAGE_WIDTH_200, Const.IMAGE_HEIGHT_200).toOutputStream(outputStream);

			byte[] resizedImageBytes = outputStream.toByteArray();
			outputStream.close();

			return resizedImageBytes;
		} catch (IOException ex) {
			logger.log(Level.SEVERE, null, ex);
		}
		return null;
	}

	@Override
	public List<TodoDto> findAll() {

		List<TodoDto> collect = this.todoRepository.findAll().stream().map(this::converToDto)
				.sorted(Comparator
						.comparing((TodoDto todo) -> Const.TODO_STATUS_OPEN.equalsIgnoreCase(todo.getStatus()) ? 0 : 1)
						.thenComparing(Comparator.comparing(TodoDto::getStatus)))
				.collect(Collectors.toList());

		return collect;
	}
	
	@Transactional
	@Override
	public TodoDto update(Long todoID) {

		Todo todo = this.todoRepository.findById(todoID)
				.orElseThrow(() -> new ResourceNotFoundException("Requested todo ", "TodoID " + todoID));

		todo.setStatus(
				(todo.getStatus().equals(Const.TODO_STATUS_CLOSE)) ? Const.TODO_STATUS_OPEN : Const.TODO_STATUS_CLOSE);

		Todo updatedTodo = this.todoRepository.save(todo);
		logger.log(Level.INFO, "updated todo status open or close ...");

		return this.converToDto(updatedTodo);
	}

	@Override
	public void delete(Long todoID) {
		Todo todo = this.todoRepository.findById(todoID)
				.orElseThrow(() -> new ResourceNotFoundException("Requested todo", "TodoID", todoID));

		this.todoRepository.delete(todo);
		logger.log(Level.INFO, "todo information deleted...");

	}

//      -------------- dto to entity and entity to dto converter methods --------------------------------
	private Todo converToTodo(TodoDto todoDto) {
		Todo todo = new Todo();

		todo.setTitle(todoDto.getTitle());
		todo.setDescription(todoDto.getDescription());
		todo.setTodoID(todoDto.getTodoID());
		todo.setStatus(todoDto.getStatus());
		return todo;
	}

	private TodoDto converToDto(Todo todo) {
		TodoDto todoDto = new TodoDto();

		todoDto.setTitle(todo.getTitle());
		todoDto.setDescription(todo.getDescription());
		todoDto.setTodoID(todo.getTodoID());
		todoDto.setCreatedBy(todo.getUser().getUsername());
		todoDto.setStatus(todo.getStatus());
		if (todo.getResizedImage() != null)
			todoDto.setImage(DatatypeConverter.printBase64Binary(todo.getResizedImage()));
		return todoDto;

	}
	
	@Transactional
	@Override
	public boolean uploadImage(Long todoID, String image) {

		Todo todo = this.todoRepository.findById(todoID)
				.orElseThrow(() -> new ResourceNotFoundException("Requested todo ", "TodoID " + todoID));

		byte[] originalImage = DatatypeConverter.parseBase64Binary(image);

		logger.log(Level.INFO, "Resizing image file...");
		byte[] resizedImage = resizeImage(originalImage);

		todo.setResizedImage(resizedImage);
		todo.setOriginalImage(originalImage);
		
		todo = this.todoRepository.save(todo);
		logger.log(Level.INFO, "image upload and reziing completed...");

		logger.log(Level.INFO, "notiying client using web socket...");
		messagingTemplate.convertAndSend("/topic/imageUpload", "Image upload and resizing completed successfully!");

		return todo != null;
	}
}
