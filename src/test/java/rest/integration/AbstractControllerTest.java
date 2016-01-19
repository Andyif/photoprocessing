package rest.integration;

import com.amayd.uploadservice.rest.controller.BaseController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;

public abstract class AbstractControllerTest {

    protected static final Logger logger = LoggerFactory.getLogger(AbstractControllerTest.class);

    protected MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    protected String mapToJson(Object o) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(o);
    }

    protected <T> T mapFromJson(String json, Class<T> clazz) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, clazz);
    }

    protected void setUp(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    protected void setUp(BaseController controller){
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }
}
