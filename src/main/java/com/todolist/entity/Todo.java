package com.todolist.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long todoID;

    private String title;

    private String description;

    private String status;

    @ManyToOne
    private User user;

    @Lob
    @Column(length = 1000000)
    private byte[] originalImage;
    @Lob
    @Column(length = 1000000)
    private byte[] resizedImage;
}
