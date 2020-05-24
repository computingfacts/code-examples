package com.computingfacts.di.service;

import java.util.List;
import java.util.Map;

/**
 * @author joseph
 */
public interface CommentService {

    List<Map.Entry<String, Map<String, String>>> commentsByUserId(String username);
}
