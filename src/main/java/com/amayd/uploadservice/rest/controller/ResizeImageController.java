package com.amayd.uploadservice.rest.controller;

import com.amayd.uploadservice.rest.model.ImageEntity;
import com.amayd.uploadservice.rest.model.ProcessingResult;
import com.amayd.uploadservice.rest.resource.ProcessingResultResource;
import com.amayd.uploadservice.rest.resourceAssemblers.ProcessingResultAssembler;
import com.amayd.uploadservice.service.ResizeImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.InputStream;
import java.util.concurrent.Future;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@Controller
@RequestMapping("rest")
public class ResizeImageController extends BaseController{

    @Autowired
    ResizeImageService resizeImageService;

    @RequestMapping(value = "upload", method = POST)
    public HttpEntity<ProcessingResultResource> uploadImage(@RequestBody ImageEntity imageEntity){
        logger.debug("Upload Image Controller");
        ProcessingResult processingResult = resizeImageService.resizeImage(imageEntity);
        ProcessingResultResource processingResultResource = new ProcessingResultAssembler().toResource(processingResult);
        return new HttpEntity<>(processingResultResource);
    }

    @RequestMapping(value = "status/{uid}", method = GET)
    public HttpEntity<ProcessingResultResource> getStatus(@PathVariable Long uid){
        ProcessingResult processingResult = resizeImageService.getProcessingStatus(uid);
        ProcessingResultResource processingResultResource = new ProcessingResultAssembler().toResource(processingResult);
        return new HttpEntity<>(processingResultResource);
    }
}
