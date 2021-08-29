package com.denprod.task_management;


import com.denprod.task_management.entity.Task;
import com.denprod.task_management.entity.TaskComment;
import com.denprod.task_management.entity.TaskPriority;
import com.denprod.task_management.entity.TaskStatus;
import com.denprod.task_management.entity.security.Role;
import com.denprod.task_management.entity.security.User;
import com.denprod.task_management.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class TaskManagementRunner implements ApplicationRunner {
    @Autowired
    UserRepository userRepository;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    TaskPriorityRepository taskPriorityRepository;

    @Autowired
    TaskStatusRepository taskStatusRepository;

    @Autowired
    TaskCommentRepository taskCommentRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {
        //Basic data

        //Task priorities
        TaskPriority priority = new TaskPriority();
        priority.setName("No urgent");
        priority.setValue(9);
        taskPriorityRepository.save(priority);

        priority = new TaskPriority();
        priority.setName("Normal");
        priority.setValue(5);
        taskPriorityRepository.save(priority);

        priority = new TaskPriority();
        priority.setName("Urgent");
        priority.setValue(1);
        taskPriorityRepository.save(priority);

        //Task statuses
        TaskStatus status = new TaskStatus();
        status.setName("Draft");
        taskStatusRepository.save(status);

        status = new TaskStatus();
        status.setName("Assigned");
        taskStatusRepository.save(status);

        status = new TaskStatus();
        status.setName("In the process");
        taskStatusRepository.save(status);

        status = new TaskStatus();
        status.setName("In testing");
        taskStatusRepository.save(status);

        status = new TaskStatus();
        status.setName("Done");
        status.setCompleted(true);
        taskStatusRepository.save(status);

        //Users
        Set<Role> rolesAdmin = new HashSet<>();
        rolesAdmin.add(Role.ADMIN);

        Set<Role> rolesUser = new HashSet<>();
        rolesUser.add(Role.USER);

        String pass = passwordEncoder.encode("1");

        User user1 = new User();
        user1.setUsername("John Snow");
        user1.setEmail("aaa@aa.com");
        user1.setPassword(pass);
        user1.setRoles(rolesAdmin);
        userRepository.save(user1);

        User user2 = new User();
        user2.setUsername("user1");
        user2.setEmail("bbb@bb.com");
        user2.setPassword(pass);
        user2.setRoles(rolesUser);
        userRepository.save(user2);

        User user3 = new User();
        user3.setUsername("user2");
        user3.setEmail("ccc@cc.com");
        user3.setPassword(pass);
        user3.setRoles(rolesUser);
        userRepository.save(user3);

        //Tasks
        Task task = new Task();
        task.setName("Improving actions");
        task.setAuthor(user1);
        task.setDescription("We must do a lot of improving actions in this project");
        task.setPriority(taskPriorityRepository.getById(2));
        task.setStatus(taskStatusRepository.getById(1));
        taskRepository.save(task);

        for (int i = 0; i < 40; i++) {
            task = new Task();
            task.setName("Test task " + i);
            task.setAuthor(user1);
            task.setDescription("Test description " + i);
            task.setPriority(taskPriorityRepository.getById(2));
            task.setStatus(taskStatusRepository.getById(1));
            taskRepository.save(task);
        }

        task = new Task();
        task.setName("Developing front-end");
        task.setAuthor(user1);
        task.setDescription("Front-end in this application must be more comfortable");
        task.setPriority(taskPriorityRepository.getById(3));
        task.setStatus(taskStatusRepository.getById(2));
        task.setAssignee(user3);
        task.getUsers().add(user1);
        task.getUsers().add(user3);
        taskRepository.save(task);

        //Task comment
        TaskComment taskComment = new TaskComment();
        taskComment.setAuthor(user2);
        taskComment.setTask(task);
        taskComment.setDescription("Explain the description of the task. I don't understand what you did you mean.");
        taskCommentRepository.save(taskComment);
    }
}
