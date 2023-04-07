package com.fieldright.fr.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Component
public class FileUtil {

    @Value("spring.servlet.multipart.location")
    private String path;

    public String  transfere(MultipartFile file){

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String newFileName = "imagem_" + System.currentTimeMillis() + "_" + fileName;

        File convertedFile = new File(path + newFileName);

        try {
            file.transferTo(convertedFile);
            return "https://fieldrightapi.herokuapp.com/imagens/"+newFileName;
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;

    }


}
