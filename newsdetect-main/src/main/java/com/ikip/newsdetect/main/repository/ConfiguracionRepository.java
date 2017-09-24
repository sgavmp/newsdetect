package com.ikip.newsdetect.main.repository;

import es.ucm.visavet.gbf.app.domain.Configuracion;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfiguracionRepository extends CrudRepository<Configuracion, String> {

}
