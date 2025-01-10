package vdtry06.springboot.authservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vdtry06.springboot.authservice.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {}
