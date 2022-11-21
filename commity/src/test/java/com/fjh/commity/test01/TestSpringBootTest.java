package com.fjh.commity.test01;

import com.fjh.commity.CommityApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@SpringBootTest(classes = CommityApplication.class)
@RunWith(SpringRunner.class)
//@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
public class TestSpringBootTest {
}
