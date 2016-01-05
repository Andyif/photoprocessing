package com.amayd.uploadservice.controllers;

import com.amayd.uploadservice.modes.UploadedFile;
import com.amayd.uploadservice.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.stream.IntStream;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@Controller
@RequestMapping("/upload")
public class UploadController {

    @Autowired
    FileRepository fileRepository;

    @RequestMapping(method = POST)
    public String uploadfFile(HttpServletRequest httpServletRequest, Model model) throws IOException{
        int c;
        FileOutputStream outputStream = null;
        StandardMultipartHttpServletRequest standardMultipartHttpServletRequest;
        standardMultipartHttpServletRequest = new StandardMultipartHttpServletRequest(httpServletRequest);
        standardMultipartHttpServletRequest.getFileNames().forEachRemaining(System.out::println);
        try {
            InputStream intStream = standardMultipartHttpServletRequest.getFile("fileToUpload").getInputStream();
            outputStream = new FileOutputStream("C:/Users/Andy_/Desktop/123.jpg");
            while ((c = intStream.read()) != -1){
                outputStream.write(c);
            }
        }finally {
            if (outputStream != null){
                outputStream.close();
            }
        }



//        fileRepository.save(new UploadedFile(fileToUpload.get));
        model.addAttribute("info", fileRepository.count());
        return "upload";
    }

    @RequestMapping(method = GET)
    public String showPade(){
        return "upload";
    }

}
