package com.wipro.authentication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wipro.authentication.model.User;
@Repository
public interface UserRepository extends JpaRepository<User, String> {

}
