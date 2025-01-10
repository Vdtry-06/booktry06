package vdtry06.springboot.authservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vdtry06.springboot.authservice.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByUsername(String username);

    boolean existsByPassword(String password);

    boolean existsByEmail(String email);

    Optional<User> findByUsername(String username);
}
