package com.access.zenchallenge.repository;


import com.access.zenchallenge.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
