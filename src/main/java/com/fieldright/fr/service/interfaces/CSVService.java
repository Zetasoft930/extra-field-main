package com.fieldright.fr.service.interfaces;

import com.fieldright.fr.response.Response;
import org.springframework.web.multipart.MultipartFile;

public interface CSVService {

   Response  saveProduct(MultipartFile file);
}
