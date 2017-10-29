package com.ikip.newsdetect.main.repository;

import com.ikip.newsdetect.model.Location;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Set;

@RepositoryRestResource
public interface LocationRepository extends CrudRepository<Location, Long> {

	public Set<Location> findAllByOrderByNameAsc();
	@Query(value="SELECT l.* FROM location l left join news_locations n on(n.location_id = l.id) " +
			"where n.alert_detect_id in (SELECT id from alert union select id from risk) " +
			"group by l.id;", nativeQuery=true)
	public List<Location> findAllAfect();
	@Query(value="SELECT l.* FROM location l left join news_locations n on(n.location_id = l.id) " +
			"where n.alert_detect_id in (SELECT id from alert) group by l.id;", nativeQuery=true)
	public List<Location> findAllAfectAlert();
	@Query(value="SELECT l.* FROM location l left join news_locations n on(n.location_id = l.id) " +
			"where n.alert_detect_id in (SELECT id from risk) group by l.id;", nativeQuery=true)
	public List<Location> findAllAfectRisk();
	
}
