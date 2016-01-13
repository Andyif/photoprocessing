package com.amayd.uploadservice.controllers;

import com.amayd.uploadservice.modes.UploadedFile;
import com.amayd.uploadservice.repository.FileRepository;
import com.amayd.uploadservice.service.ResizeImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@Controller
@RequestMapping("/upload")
public class UploadController {

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private ResizeImageService resizeImageService;

    private Map<String, Future<String>> receivedRequests = new HashMap<>();

    @RequestMapping(method = POST)
    public String uploadFile(final HttpServletRequest httpServletRequest, final Model model) throws IOException{

        final StandardMultipartHttpServletRequest httpRequest =
                new StandardMultipartHttpServletRequest(httpServletRequest);
        final InputStream inputStream = httpRequest.getFile("fileToUpload").getInputStream();

        Future<String> processingResult = resizeImageService.changeSize(inputStream, 1000, 1000, false);
        receivedRequests.put(String.valueOf(Instant.now().getEpochSecond()), processingResult);

        model.addAttribute("info", "processing");
        return "upload";
    }

    @RequestMapping(method = GET)
    public String showPage(){
        return "upload";
    }

    @RequestMapping(value = "/status", method = GET)
    public String getStatus(final Model model){

        receivedRequests.forEach((uid, result) -> {
            if (result.isDone()){
                model.addAttribute("info", "processed " + fileRepository.count());
            }else {
                model.addAttribute("info", "processing");

            }
        });

//        if (processingResult.isDone()){
//            model.addAttribute("info", "processed " + fileRepository.count());
//        }else {
//            model.addAttribute("info", "processing");
//        }

        return "upload";
    }

}
