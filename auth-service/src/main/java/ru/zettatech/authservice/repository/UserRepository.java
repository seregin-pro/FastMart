package ru.zettatech.authservice.repository;

import ru.zettatech.authservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

	User findUserByUsername(String username);



}
