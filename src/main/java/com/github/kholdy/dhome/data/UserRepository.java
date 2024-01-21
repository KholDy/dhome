package com.github.kholdy.dhome.data;

import com.github.kholdy.dhome.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long>{
	Optional<User> findByUsername(String username);
	boolean deleteByUsername(String username);
	boolean existsByUsername(String username);
}
