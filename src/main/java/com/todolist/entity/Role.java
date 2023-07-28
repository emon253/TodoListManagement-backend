package com.todolist.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long roleID;

    private String roleName;

    public Role() {
    }

    
    public Role(String roleName) {
        this.roleName = roleName;
    }
    
    

}
