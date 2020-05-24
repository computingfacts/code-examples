package com.computingfacts.di.service;

import org.springframework.stereotype.Service;

import java.beans.ConstructorProperties;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author joseph
 */
@Service
class CommentServiceImpl implements CommentService {

    private final Stream<Map.Entry<String, Map<String, String>>> commentRepository;

    @ConstructorProperties({"commentRepository"})
    public CommentServiceImpl(Stream<Map.Entry<String, Map<String, String>>> commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public List<Entry<String, Map<String, String>>> commentsByUserId(String username) {
        return commentRepository
                .filter(u -> u.getValue().containsValue(username))
                .collect(Collectors.toList());
    }

}
