package com.ikip.newsdetect.topic.util;
public class QueryUtil {
  public static String internize(String s) {
    StringBuffer sb = new StringBuffer();
	int i=0;
	while (i < s.length()) {
	  if (s.charAt(i) == '\\') i++;
	  sb.append(s.charAt(i++));
	}
	return sb.toString();
   }
}