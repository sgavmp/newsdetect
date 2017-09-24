package com.ikip.newsdetect.main.service;

import es.ucm.visavet.gbf.app.domain.Location;
import es.ucm.visavet.gbf.app.repository.LocationRepository;
import es.ucm.visavet.gbf.app.service.NewsIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Set;

@Service
public class PlaceAlertServiceImpl {
	
	@Autowired
	private LocationRepository repository;
	
	@Autowired
	private NewsIndexService newsIndexService;
	
	public Set<Location> getAllLocations() {
		return repository.findAllByOrderByNameAsc();
	}
	
	public Location getOneById(Long id) {
		return repository.findOne(id);
	}
	
	public Location createLocation(Location place) throws IOException {
		place = repository.save(place);
		newsIndexService.resetLocation(place);
		return place;
	}
	
	public Location updateLocation(Location place) throws IOException {
		place = repository.save(place);
		newsIndexService.resetLocation(place);
		return place;
	}
	
	public void removeLocation(Location place) {
		repository.delete(place);
	}

}
