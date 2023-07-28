package com.todolist.dto;

import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TodoDto {

    private Long todoID;

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    private String status;

    private String createdBy;

    private String image;

}
