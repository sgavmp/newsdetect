package com.ikip.newsdetect.main.service;

import com.ikip.newsdetect.find.service.NewsIndexService;
import com.ikip.newsdetect.main.repository.LocationRepository;
import com.ikip.newsdetect.model.Location;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Set;

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
