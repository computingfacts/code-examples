package com.computingfacts.di;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class DependencyInjectionApplicationTests {

	@Autowired
	private  Stream<Map.Entry<String, String>> blogRepository;
	@Autowired
	private  Stream<Map.Entry<String, Map<String, String>>> commentRepository;
	@Test
	void contextLoads() {
		assertThat(blogRepository).isNotNull();
		assertThat(commentRepository).isNotNull();
	}

}
