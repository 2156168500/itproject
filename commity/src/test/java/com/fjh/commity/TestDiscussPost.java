package com.fjh.commity;
import com.fjh.commity.entity.DiscussPost;
import com.fjh.commity.service.DiscussPostService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.List;

@SpringBootTest(classes = CommityApplication.class)
@RunWith(SpringRunner.class)
public class TestDiscussPost {
    @Autowired
    private DiscussPostService discussPostService;
    @Test
    public void testSelect(){
        List<DiscussPost> discussPosts = discussPostService.selectAllDiscussPost(0, 0, 10);
        for(DiscussPost discussPost : discussPosts){
            System.out.println(discussPost);
        }
        int count = discussPostService.selectCount(0);
        System.out.println(count);
    }
}
