package com.todolist;

import com.todolist.constants.Const;
import com.todolist.entity.Role;
import com.todolist.entity.User;
import com.todolist.repositories.RoleRepository;
import com.todolist.repositories.UserRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class TodoRuntimeInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        // Check if roles are already initialized
        if (roleRepository.count() == 0) {
            this.initializeUsersWithRole();
        }
    }

    private void initializeUsersWithRole() {
//----------- saving roles in the db -----------------------
        this.roleRepository.save(new Role(Const.ROLE_ADMINISTRATIVE_USER));
        this.roleRepository.save(new Role(Const.ROLE_REGULAR_USER));

//------------- setting admin user with role-----------------------------
        Set<Role> rolesSetForAdmin = new HashSet<>();
        User user1 = new User();
        user1.setUserName("Yasin");
        user1.setPassword(this.passwordEncoder.encode("1234"));
        rolesSetForAdmin.add(this.roleRepository.findByRoleName(Const.ROLE_ADMINISTRATIVE_USER));
        user1.setRoles(rolesSetForAdmin);

//------------- setting regular user with role-----------------------------
        Set<Role> rolesSetForRegular = new HashSet<>();
        User user2 = new User();
        user2.setUserName("Emon");
        user2.setPassword(this.passwordEncoder.encode("1234"));
        rolesSetForRegular.add(this.roleRepository.findByRoleName(Const.ROLE_REGULAR_USER));
        user2.setRoles(rolesSetForRegular);

//-------------- saving users with role ----------------------------------
        this.userRepository.save(user1);
        this.userRepository.save(user2);
    }

}
