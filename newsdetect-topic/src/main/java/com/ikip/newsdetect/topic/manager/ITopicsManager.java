package com.ikip.newsdetect.topic.manager;

import com.ikip.newsdetect.model.Topic;

import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.Set;


public interface ITopicsManager {
   boolean existsTopic(String topic);
   boolean existsSourceType(String type);
   boolean existsSourceLocation(String location);
   int getSourceType(String type);
   int getSourceLocation(String type);
   Set<String> getDependencies(String topic); 
   void addDependency(String topic, String ofTopic); 
   Reader getDefinition(String topic) throws UnsupportedEncodingException; 
   Topic getTopic(String topic);
}
