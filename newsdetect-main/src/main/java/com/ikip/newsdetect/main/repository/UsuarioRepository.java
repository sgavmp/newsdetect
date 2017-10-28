package com.ikip.newsdetect.main.repository;

import com.ikip.newsdetect.model.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends PagingAndSortingRepository<User, String> {

	User findByUserName(String userName);
	
}
