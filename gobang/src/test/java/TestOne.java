import cn.hutool.crypto.digest.DigestUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestOne {
    public static void main(String[] args) {
        String s = DigestUtil.md5Hex("123");
        String s1 = DigestUtil.md5Hex("123");
        System.out.println(s);
        System.out.println(s1);
    }
}
