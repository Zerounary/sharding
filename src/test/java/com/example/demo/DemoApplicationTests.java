package com.example.demo;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class)
public class DemoApplicationTests {

	@Autowired
	OrderDao orderDao;

	@Test
	public void testOrderDao() {
		for(int i = 0; i < 10; i++)
			orderDao.insertOrder(new BigDecimal(11), 1L,"success");
	}

}
