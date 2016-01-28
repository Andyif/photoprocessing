package com.amayd.uploadservice.rest.controller;

import com.amayd.uploadservice.rest.model.ImageEntity;
import com.amayd.uploadservice.rest.model.ProcessingResult;
import com.amayd.uploadservice.rest.resource.ProcessingResultResource;
import com.amayd.uploadservice.rest.resourceAssemblers.ProcessingResultAssembler;
import com.amayd.uploadservice.service.ResizeImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.View;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Enumeration;

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
        ProcessingResult processingResult = resizeImageService.getResizeImageStatus(uid);
        ProcessingResultResource processingResultResource = new ProcessingResultAssembler().toResource(processingResult);
        return new HttpEntity<>(processingResultResource);
    }

    @ExceptionHandler({IllegalArgumentException.class, NullPointerException.class})
    void handlerEx(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String requestUri = request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE).toString();
        String uid = requestUri.substring(requestUri.indexOf("=")+1, requestUri.indexOf("}"));

        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No such request with UID = " + uid);
    }
}
