package com.example.demo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
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

	@Autowired
	PlusMapper plusMapper;

	@Test
	public void testOrderDao() {
		for(int i = 0; i < 10; i++)
			orderDao.insertOrder(new BigDecimal(11), 1L,"success");
	}

	@Test
	public void plusTest(){
		List<Map> rst = plusMapper.getAll(Wrappers.query().eq("status", "ok"));
		System.out.println("rst" + JSONObject.toJSONString(rst));
	}

}
