package com.amayd.uploadservice.controllers;

import com.amayd.uploadservice.modes.UploadedFile;
import com.amayd.uploadservice.repository.FileRepository;
import com.amayd.uploadservice.service.ResizeImageService;
import com.amayd.uploadservice.tools.ThreadExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@Controller
@RequestMapping("/upload")
public class UploadController {

    @Autowired
    FileRepository fileRepository;

    @Autowired
    ResizeImageService resizeImageService;

    Future<List<String>> resultResizedFiles;
    boolean processResultAfterResize = false;

    @RequestMapping(method = POST)
    public String uploadfFile(HttpServletRequest httpServletRequest, Model model) throws IOException{

        StandardMultipartHttpServletRequest standardMultipartHttpServletRequest =
                new StandardMultipartHttpServletRequest(httpServletRequest);
//        standardMultipartHttpServletRequest.getFileNames().forEachRemaining(System.out::println);
        InputStream inputStream = standardMultipartHttpServletRequest.getFile("fileToUpload").getInputStream();

        resultResizedFiles = resizeImageService.changeSize(inputStream, 1000, 1000, false);
        processResultAfterResize = true;

        model.addAttribute("info", "processing");
        return "upload";
    }

    @RequestMapping(method = GET)
    public String showPade(){
        return "upload";
    }

    @RequestMapping(value = "/status", method = GET)
    public String getStatus(Model model){

        if(processResultAfterResize){
            try {
                resultResizedFiles.get().stream().forEach(file -> fileRepository.save(new UploadedFile(file)));
            } catch (InterruptedException|ExecutionException e) {
                e.printStackTrace();
            }finally {
                processResultAfterResize = false;
            }
        }

        model.addAttribute("info", fileRepository.count());
        return "upload";
    }

}
