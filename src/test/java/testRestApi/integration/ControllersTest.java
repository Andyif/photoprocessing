package testRestApi.integration;

import com.amayd.uploadservice.Application;
import com.amayd.uploadservice.rest.model.ImageEntity;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Application.class})
@WebAppConfiguration
public class ControllersTest extends AbstractControllerTest{

    @Before
    public void setUp(){
        super.setUp();
    }

    @Test
    public void uploadTest() throws Exception {
        ImageEntity imageEntity = new ImageEntity();
        imageEntity.setName("integrationTest");
        imageEntity.setUrl("https://www.google.com.ua/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png");
        imageEntity.setHeight(100);
        imageEntity.setWidth(100);

        String inputString = mapToJson(imageEntity);

        MvcResult result = mockMvc.perform(post("/rest/upload").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON).content(inputString)).andReturn();

        String content = result.getResponse().getContentAsString().trim();
        int status = result.getResponse().getStatus();

        Assert.assertEquals("Wrong status", 200, status);
        Assert.assertTrue("empty body",content.length() > 0);
        System.out.println("CONTENT - " + content);
        Thread.sleep(60000);

        String currentUid = content.substring(24,34);
        System.out.println(currentUid);

        result = mockMvc.perform(get("/rest/status/" + currentUid)).andReturn();
        content = result.getResponse().getContentAsString();
        System.out.println("CONTENT - " + content);
    }

    @Test
    public void getTest() throws Exception {
        MvcResult result = this.mockMvc.perform(get("/rest/status/123")).andReturn();

        String content = result.getResponse().getContentAsString();
        int status = result.getResponse().getStatus();

        Assert.assertEquals("Wrong status", 200, status);

    }

}
