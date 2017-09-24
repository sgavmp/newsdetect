package com.ikip.newsdetect.topic.validator;

public class SourceTypeDoesNotExistsException extends ParseException {
 private String type;
 public SourceTypeDoesNotExistsException(String type){
   super();
   this.type = type;
 }
 public String getType() {return type;};
 public String toString() {
   return "El tipo de fuente "+type+" no existe (no está definido)";
 }    
}
