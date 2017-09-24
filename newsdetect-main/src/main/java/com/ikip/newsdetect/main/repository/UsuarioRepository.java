package com.ikip.newsdetect.main.repository;

import es.ucm.visavet.gbf.app.domain.Usuario;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends CrudRepository<Usuario, String> {

	public Usuario findByUserName(String userName);
	
}
