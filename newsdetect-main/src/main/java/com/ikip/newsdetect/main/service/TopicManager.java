package com.ikip.newsdetect.main.service;

import com.ikip.newsdetect.main.repository.TopicRepository;
import com.ikip.newsdetect.model.Topic;
import com.ikip.newsdetect.topic.manager.ITopicsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Set;

@Service
public class TopicManager implements ITopicsManager {

	@Autowired
	private TopicRepository topicRepository;
	
	@Override
	public boolean existsTopic(String topic) {
		return topicRepository.exists(topic);
	}

	@Override
	public Set<String> getDependencies(String topic) {
		Topic item = topicRepository.findOne(topic);
		Set<String> dependencias = new HashSet<>();
		if (item!=null) {
			for(Topic aux : topicRepository.findAllByReference(item.getTitle()))
				dependencias.add(aux.getTitle());
		}
		return dependencias;
	}

	@Override
	public void addDependency(String topic, String ofTopic) {
		// TODO Auto-generated method stub
	}

	@Override
	public Reader getDefinition(String topic) throws UnsupportedEncodingException {
		Topic item = topicRepository.findOne(topic);
		if (item!=null)
			return new InputStreamReader(new ByteArrayInputStream(item.getQuery().getBytes()),"UTF-8");
		else
		{
			System.out.println("Este topic esta mal->"+topic);
		return null;
		
		}
	}

	@Override
	public boolean existsSourceType(String type) {
		return true;
	}

	@Override
	public boolean existsSourceLocation(String location) {
		return true;
	}

	@Override
	public int getSourceType(String type) {
		return 0;
	}

	@Override
	public int getSourceLocation(String location) {
		return 0;
	}

	@Override
	public Topic getTopic(String topic) {
		return topicRepository.findOne(topic);
	}
	
	public Iterable<Topic> getAllTopic() {
		return topicRepository.findAll();
	}


}
