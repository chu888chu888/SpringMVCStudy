package com.roncoo.education;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.roncoo.education.component.RoncooRedisComponent;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootDemo171ApplicationTests {

	@Autowired
	private RoncooRedisComponent roncooRedisComponent;

	@Test
	public void set() {
		roncooRedisComponent.set("roncoo", "hello world");
	}

	@Test
	public void get() {
		System.out.println(roncooRedisComponent.get("roncoo"));
	}
	
	@Test
	public void del() {
		roncooRedisComponent.del("roncoo");
	}

}
