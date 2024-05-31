package tacos;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/* @WebMvcTest is a special Spring Boot test annotation that arranges for the test to run in the context of a
Spring MVC application. More specifically, in this case, it arranges for HomeController to be registered in
Spring MVC so that you can send requests to it. @WebMvcTest also sets up Spring support for testing Spring MVC.
Although it could be made to start a server, mocking the mechanics of Spring MVC is sufficient for our purposes.
The test class is injected with a MockMvc object for the test to drive the mockup. */
@WebMvcTest(WebConfig.class) // Web test for HomeController
public class WebConfigTest {

    @Autowired
    private MockMvc mockMvc; // performs the request for our below test

    @Test
    public void testHomePage() throws Exception {
        mockMvc.perform(get("/"))
            .andExpect(status().isOk()) // expects HTTP 200
            .andExpect(view().name("home"))
            .andExpect(content().string(containsString("Welcome to...")));
    }
}