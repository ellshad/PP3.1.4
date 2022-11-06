package ru.kata.spring.boot_security.demo.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


//   @Query("select u from User u JOIN FETCH u.roles where u.username = :username")
   User findByUsername(String username);

   @Query("select u from User u JOIN FETCH u.roles where u.email = :email")
   User findByEmail(String email);
}
//Интерфейс JpaRepository предоставляет набор стандартных методов (findBy, save, deleteById и др.) для работы с БД.