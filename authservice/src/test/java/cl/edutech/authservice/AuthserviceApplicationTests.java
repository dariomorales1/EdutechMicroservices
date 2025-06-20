package cl.edutech.authservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class AuthserviceApplicationTests {

    @Test
    void testPlus() {
        int result = 2 + 3;
        assertEquals(5, result);
    }

}
