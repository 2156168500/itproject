import com.fjh.examonline.domain.Teacher;
import com.fjh.examonline.service.TeacherService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring.xml")
public class Test1 {
    @Autowired
    private TeacherService teacherService;
    @Test
    public void insert(){
        Teacher teacher = new Teacher();
        teacher.setTname("张三");
        teacher.setPass("123");
        teacherService.save(teacher);
    }
}
