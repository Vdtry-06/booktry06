package vdtry06.springboot.authservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vdtry06.springboot.authservice.entity.Permission;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, String> {}
