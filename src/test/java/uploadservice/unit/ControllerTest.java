//package uploadservice.unit;
//
//import com.amayd.uploadservice.controllers.UploadController;
//import org.junit.Test;
//import uploadservice.configuration.RepositoryConfiguration;
//import org.junit.Before;
//import org.junit.runner.RunWith;
//import org.springframework.boot.test.SpringApplicationConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = {RepositoryConfiguration.class})
//public class ControllerTest {
//
//    final String BASE_URL = "http://localhost:8080/";
//    private MockMvc mockMvc;
//
//    @Before
//    public void setUp(){
//        this.mockMvc = MockMvcBuilders.standaloneSetup(new UploadController()).build();
//    }
//
//    @Test
//    public void testUploadFile() throws Exception {
//        this.mockMvc.perform(post("/upload")).andReturn();
//    }
//
//}
