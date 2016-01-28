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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Application.class})
@WebAppConfiguration
public class ControllersTest extends AbstractControllerTest {

    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void upload_get_validTest() throws Exception {
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
        Assert.assertTrue("empty body", content.length() > 0);
        Thread.sleep(10000);

        String currentUid = content.substring(24, 34);

        result = mockMvc.perform(get("/rest/status/" + currentUid)).andReturn();
        content = result.getResponse().getContentAsString();
    }

    @Test
    public void get_invalidTestTest() throws Exception {
        String uid = "123";
        String expectedErrorMsg = "No such request with UID = " + uid;

        MvcResult result = this.mockMvc.perform(get("/rest/status/" + uid)).andReturn();

        String content = result.getResponse().getContentAsString();
        int status = result.getResponse().getStatus();
        String actualErrorMsg = result.getResponse().getErrorMessage();

        Assert.assertEquals("Wrong status", 400, status);
        Assert.assertEquals(expectedErrorMsg, actualErrorMsg);
    }

}
