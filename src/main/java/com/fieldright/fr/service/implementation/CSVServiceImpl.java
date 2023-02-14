package com.fieldright.fr.service.implementation;

import com.fieldright.fr.entity.Product;
import com.fieldright.fr.response.Response;
import com.fieldright.fr.service.interfaces.CSVService;
import com.fieldright.fr.service.interfaces.ProductService;
import com.fieldright.fr.util.CSVHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class CSVServiceImpl implements CSVService {

    @Autowired
    private CSVHelper csvHelper;
    @Autowired
    private ProductService productService;

    @Override
    public Response<String> saveProduct(MultipartFile file) {

        String message = "Ficheiro importado com sucesso";
        HttpStatus status = HttpStatus.OK;
        try {
            List<Product> products = csvHelper.csvToTutorials(file.getInputStream());
            if(!products.isEmpty())
            {
                productService.internalSave(products);
            }

        } catch (IOException e) {
            message = "fail to store csv data: " + e.getMessage();
        }
        catch (Exception e){
            e.printStackTrace();
            message = e.getMessage();

        }


        return new Response.Builder()
                .withStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .withData(message)
                .withErrors(null)
                .build();
    }
}
