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
import java.util.ArrayList;
import java.util.List;

@Service
public class CSVServiceImpl implements CSVService {

    @Autowired
    private CSVHelper csvHelper;
    @Autowired
    private ProductService productService;

    @Override
    public Response<String> saveProduct(MultipartFile file) {

        String message = "";
        HttpStatus status = HttpStatus.OK;
        List<String> messageError = new ArrayList<>();
        try {
            List<Product> products = csvHelper.csvToTutorials(file.getInputStream());
            if(!products.isEmpty())
            {
                productService.internalSave(products);
                message = "Ficheiro importado com sucesso";
            }

        } catch (IOException e) {
            messageError.add("fail to store csv data: " + e.getMessage());

        }
        catch (Exception e){
            e.printStackTrace();
            messageError.add( e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;

        }

        return new Response.Builder()
                .withStatus(status)
                .withData(message)
                .withErrors(messageError)
                .build();
    }
}
