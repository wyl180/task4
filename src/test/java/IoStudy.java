import com.jnshu.util.OSSClientUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @ClassName IoStudy
 * @Description io流学习
 * @Author 韦延伦
 * @Date 2020/9/5 15:14
 * @Version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/applicationContext.xml")
public class IoStudy {
    @Autowired
   OSSClientUtil ossClientUtil;

}
