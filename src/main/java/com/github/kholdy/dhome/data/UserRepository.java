package com.github.kholdy.dhome.data;

import com.github.kholdy.dhome.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long>{
	User findByUsername(String username);
}
