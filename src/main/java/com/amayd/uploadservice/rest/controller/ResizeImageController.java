package com.amayd.uploadservice.rest.controller;

import com.amayd.uploadservice.rest.model.ImageEntity;
import com.amayd.uploadservice.rest.repository.ImageEntityRepository;
import com.amayd.uploadservice.rest.repository.ProcessingResultRepository;
import com.amayd.uploadservice.service.ResizeImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@Controller
@RequestMapping("rest/upload")
public class ResizeImageController {

    @Autowired
    ResizeImageService resizeImageService;

    @Autowired
    ImageEntityRepository imageEntityRepository;

    @Autowired
    ProcessingResultRepository processingResultRepository;

    @RequestMapping(method = POST)
    public HttpEntity uploadImage(@RequestBody ImageEntity imageEntity){
        imageEntityRepository.save(imageEntity);
        resizeImageService.resizeFromRest(imageEntity);
    }
}
