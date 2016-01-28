package testRestApi.integration;

import com.amayd.uploadservice.rest.controller.ResizeImageController;
import com.amayd.uploadservice.rest.model.ImageEntity;
import com.amayd.uploadservice.rest.model.ProcessingResult;
import com.amayd.uploadservice.service.ResizeImageService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.Future;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

public class MockedControllerTest extends AbstractControllerTest{

    @Mock
    private ResizeImageService resizeImageService;

    @InjectMocks
    private ResizeImageController resizeImageController;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);

        setUp(resizeImageController);
    }

    @Test
    public void mockedPostTest() throws Exception {

        String url = "https://www.google.com.ua/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png";

        ImageEntity imageEntity = new ImageEntity();
        imageEntity.setName("integrationTest");
        imageEntity.setUrl(url);
        imageEntity.setHeight(100);
        imageEntity.setWidth(100);

//        InputStream inputStream = new URL(url).openStream();

        Future<String> imageResizingResult = new AsyncResult<>("done");

        ProcessingResult processingResult = new ProcessingResult();
        processingResult.setUid(1L);
        processingResult.setFinished(true);

        when(resizeImageService.resizeImage(imageEntity)).thenReturn(processingResult);

        String inputString = mapToJson(imageEntity);

        MvcResult result = mockMvc.perform(post("/rest/upload").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON).content(inputString)).andReturn();

        String content = result.getResponse().getContentAsString().trim();
        int status = result.getResponse().getStatus();

        Assert.assertEquals("Wrong status", 200, status);
        Assert.assertTrue("empty body",content.length() > 0);
    }

    @Test
    public void mockedGetTest() throws Exception {

        final Long uid = 123L;

        ProcessingResult processingResult = new ProcessingResult();
        processingResult.setUid(uid);
        processingResult.setFinished(true);

        when(resizeImageService.getResizeImageStatus(uid)).thenReturn(processingResult);

        MvcResult result = mockMvc.perform(get("/rest/status/" +uid)).andReturn();

        Assert.assertEquals("Wrong status", 200, result.getResponse().getStatus());
        Assert.assertEquals("empty body", result.getResponse().getContentAsString().length());
    }
}
