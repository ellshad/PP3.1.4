package ru.kata.spring.boot_security.demo;

import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

@Component
public class DBInit {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    public DBInit(RoleRepository roleRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void addDefault(){
        Role roleUser = new Role("ROLE_USER");
        Role roleAdmin = new Role("ROLE_ADMIN");

        Set<Role> userRoles = new HashSet<Role>();
        Set<Role> adminRoles = new HashSet<Role>();
        Set<Role> superRoles = new HashSet<Role>();


        userRoles.add(roleUser);
        adminRoles.add(roleAdmin);
        superRoles.add(roleUser);
        superRoles.add(roleAdmin);

        roleRepository.save(roleUser);
        roleRepository.save(roleAdmin);

        User admin = new User("Вася", "Васильев", 22, "admin@mail.ru",
                "$2a$12$R7UwBqhVMUHlvoyQrwnT9upAry2qvrDLdRkN6YFd0TEdyOWqCUdya");
        User user = new User("Петя", "Петров", 22, "user@mail.ru",
                "$2a$12$jl2mZEZR2p3uVnyWGgz/s.BGm7nhqzPC.Y5CqZsEoNpqLHBFkhs9O");
        User superuser = new User("Ваня", "Иванов", 22, "super@mail.ru",
                "$2a$12$R7UwBqhVMUHlvoyQrwnT9upAry2qvrDLdRkN6YFd0TEdyOWqCUdya");

        user.setRoles(userRoles);
        admin.setRoles(adminRoles);
        superuser.setRoles(superRoles);

        userRepository.save(user);
        userRepository.save(admin);
        userRepository.save(superuser);
    }
}
