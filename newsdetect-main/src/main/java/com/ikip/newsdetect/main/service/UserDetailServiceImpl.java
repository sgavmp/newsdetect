package com.ikip.newsdetect.main.service;

import com.ikip.newsdetect.main.repository.UsuarioRepository;
import com.ikip.newsdetect.main.security.UserDetailsImpl;
import com.ikip.newsdetect.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailServiceImpl implements UserDetailsService {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		if (username==null||username.isEmpty())
			throw new UsernameNotFoundException("Not found");
		
		User usuario = usuarioRepository.findByUserName(username);
		UserDetailsImpl userDetails = new UserDetailsImpl(username, usuario.getPassword(), usuario.getEmail(), usuario.isAdmin());
		return userDetails;
	}

}
