package com.chu;

import com.chu.mapper.UserMapper;
import com.chu.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootProjectApplicationTests {

	@Test
	public void contextLoads() {
	}
	@Autowired
	private UserMapper mapper;

	@Test
	public void insert() {
		User user = new User();
		user.setName("测试");
		user.setDate(new Date());
		int result = mapper.insert(user);
		System.out.println(result);
	}
}
