package com.chu;

import com.chu.controller.IndexController;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.chu.model.User;
import com.chu.repository.UserDaoRepository;
import com.chu.util.base.Page;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Date;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class DemoApplicationTests {

	private MockMvc mvc;
	@Autowired
	private UserDaoRepository UserDao;
	@Test
	public void contextLoads() throws Exception  {
		mvc= MockMvcBuilders.standaloneSetup(new IndexController()).build();
		mvc.perform(MockMvcRequestBuilders.get("http://localhost:8080/getMapModel/223434/iiii").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.MapID").value("223434"));
	}


	@Test
	public void insert() {
		User user = new User();
		user.setName("测试");
		user.setDate(new Date());
		int result = UserDao.insert(user);
		System.out.println(result);
	}

	@Test
	public void delete() {
		int result = UserDao.deleteById(1);
		System.out.println(result);
	}

	@Test
	public void update() {
		User user = new User();
		user.setId(2);
		user.setName("测试2");
		user.setDate(new Date());
		int result = UserDao.updateById(user);
		System.out.println(result);
	}

	@Test
	public void select() {
		User result = UserDao.selectById(2);
		System.out.println(result);
	}


	@Test
	public void select2() {
		User result = UserDao.selectById(7);
		System.out.println(result);
	}

	// 分页测试
	@Test
	public void queryForPage(){
		Page<User> result = UserDao.queryForPage(1, 20, "测试");
		System.out.println(result.getList());
	}

}
