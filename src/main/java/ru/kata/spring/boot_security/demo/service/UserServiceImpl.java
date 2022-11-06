package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        addDefault();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).get();
    }

    @Override
    public List<Role> listRoles() {
        return roleRepository.findAll();
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public void saveUser(User user) {
        if (user.getPassword().isEmpty() || user.getPassword() == null) {
            user.setPassword(userRepository.findById(user.getId()).get().getPassword());
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void saveRole(Role role) {
        roleRepository.save(role);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User findByEmail(String email){
        return userRepository.findByEmail(email);
    }


    @Override
    @Transactional
    public void removeUserById(Long id) {
        userRepository.deleteById(id);
    }



    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = findByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException(String.format("User '%s' not found", email));
        }

        return user;
    }

    public boolean isExistEmail(User user) {
        User userFromDB = findByEmail(user.getEmail());
        if (userFromDB != null && userFromDB.getId()!=user.getId()) {
            return true;
        }
        return false;
    }
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
