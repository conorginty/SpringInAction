package tacos;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

// A simple test class that ensures that the Spring application context loads successfully.

/* @SpringBootTest annotation tells JUnit to bootstrap the test with Spring Boot capabilities. Just like
@SpringBootApplication (in the main class), @SpringBootTest is a composite annotation, which is itself
annotated with @ExtendWith(SpringExtension.class), to add Spring testing capabilities to JUnit 5. */
@SpringBootTest
class TacoCloudApplicationTests {

	/* Even without any assertions or code of any kind, this empty test method will prompt the @SpringBootTest
	annotation to do its job and load the Spring application context. If there are any problems in doing so,
	the test fails. */
	@Test
	void contextLoads() {
	}

}
