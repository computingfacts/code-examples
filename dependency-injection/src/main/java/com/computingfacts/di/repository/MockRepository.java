package com.computingfacts.di.repository;

import com.google.common.collect.ImmutableMap;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author joseph
 */
@Configuration
public class MockRepository {

    private final static List<Map<String, String>> BLOG_POSTS = Arrays.asList(
            ImmutableMap.of("id", "book-1",
                    "title", "Code: The Hidden Language of Computer Hardware and Software",
                    "pageCount", "393",
                    "authorId", "Charles Petzold"),
            ImmutableMap.of("id", "book-2",
                    "title", "Clean Code",
                    "pageCount", "395",
                    "authorId", "Robert Cecil Martin"),
            ImmutableMap.of("id", "book-3",
                    "name", "The Mythical Man-Month",
                    "pageCount", "207",
                    "authorId", "Fred Brooks"),
            ImmutableMap.of("id", "book-4",
                    "name", "The Design of Design",
                    "pageCount", "881",
                    "authorId", "Fred Brooks"),
            ImmutableMap.of("id", "book-5",
                    "name", "Compilers: Principles, Techniques, and Tools",
                    "pageCount", "371",
                    "authorId", "Ravi")
    );
    private static final Map<String, Map<String, String>> COMMENT_POST_MAP
            = ImmutableMap.of("comment-1", BLOG_POSTS.get(2),
            "comment-2", BLOG_POSTS.get(1),
            "comment-3", BLOG_POSTS.get(2),
            "comment-4", BLOG_POSTS.get(0),
            "comment-5", BLOG_POSTS.get(4));

    @Bean
    public Stream<Map.Entry<String, Map<String, String>>> commentRepository() {
        return COMMENT_POST_MAP
                .entrySet()
                .stream();
    }

    @Bean
    public Stream<Map.Entry<String, String>> blogRepository() {
        //return Pattern.compile(", ").splitAsStream("Article 1, Article 2, Article 3");

        return BLOG_POSTS.stream()
                .map(Map::entrySet)
                .flatMap(Collection::stream)
                .distinct();

    }

}
