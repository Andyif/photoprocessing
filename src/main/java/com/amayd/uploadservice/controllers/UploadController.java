package com.amayd.uploadservice.controllers;

import com.amayd.uploadservice.modes.UploadedFile;
import com.amayd.uploadservice.repository.FileRepository;
import com.amayd.uploadservice.service.ResizeImageService;
import com.amayd.uploadservice.tools.ThreadExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@Controller
@RequestMapping("/upload")
public class UploadController {

    @Autowired
    FileRepository fileRepository;

    @RequestMapping(method = POST)
    public String uploadfFile(HttpServletRequest httpServletRequest, Model model) throws IOException{

        StandardMultipartHttpServletRequest standardMultipartHttpServletRequest;
        List<String> resizedFiles;
        standardMultipartHttpServletRequest = new StandardMultipartHttpServletRequest(httpServletRequest);
//        standardMultipartHttpServletRequest.getFileNames().forEachRemaining(System.out::println);
        InputStream intStream = standardMultipartHttpServletRequest.getFile("fileToUpload").getInputStream();


        resizedFiles = ResizeImageService.changeSize(intStream, 1000, 1000, false);

        resizedFiles.stream().forEach(file -> fileRepository.save(new UploadedFile(file)));

        model.addAttribute("info", fileRepository.count());
        return "upload";
    }

    @RequestMapping(method = GET)
    public String showPade(){
        return "upload";
    }

}
