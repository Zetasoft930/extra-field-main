package com.fieldright.fr.util;

import com.fieldright.fr.entity.Product;
import com.fieldright.fr.util.enums.StatusProduct;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class CSVHelper {

    public  String TYPE = "text/csv";
    static String[] HEADERs = { "altura","category","comprimento","country","description",
            "largura","metrica","name","peso","pesoCubado","price",
            "quantityAvailable","tpPreparacaoDias","tpPreparacaoHoras","tpPreparacaoMinutos","unidadeMedida",
            "vendedorId","vendedorName","min_stock" };

    public  boolean hasCSVFormat(MultipartFile file) {

        if (!TYPE.equals(file.getContentType())) {
            return false;
        }

        return true;
    }


    public  List<Product> csvToTutorials(InputStream is) {

        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));

             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {

            List<Product> products = new ArrayList<>();

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {

                Product product = this.getProduct(csvRecord);

                if(product != null)
                {
                    products.add(product);
                }

            }

            return products;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
        }
    }

    private Product getProduct(CSVRecord csvRecord){


        Product product = new Product();
        product.setAltura(Double.parseDouble(csvRecord.get("altura")));
        product.setCategory(csvRecord.get("category"));
        product.setLargura(Double.parseDouble(csvRecord.get("largura")));
        product.setComprimento(Double.parseDouble(csvRecord.get("comprimento")));
        product.setCountry(csvRecord.get("country"));
        product.setDescription(csvRecord.get("description"));
        product.setMetrica(Double.parseDouble(csvRecord.get("metrica")));
        product.setName(csvRecord.get("name"));
        product.setPeso(Double.parseDouble(csvRecord.get("peso")));
        product.setPesoCubado(Double.parseDouble(csvRecord.get("pesoCubado")));
        product.setPrice(BigDecimal.valueOf(Double.parseDouble(csvRecord.get("price"))));
        product.setQuantityAvailable(BigDecimal.valueOf(Double.parseDouble(csvRecord.get("quantityAvailable"))));
        product.setTpPreparacaoDias(Integer.parseInt(csvRecord.get("tpPreparacaoDias")));
        product.setTpPreparacaoHoras(Integer.parseInt(csvRecord.get("tpPreparacaoHoras")));
        product.setTpPreparacaoMinutos(Integer.parseInt(csvRecord.get("tpPreparacaoMinutos")));
        product.setUnidadeMedida(csvRecord.get("unidadeMedida"));
        product.setVendedorId(Long.parseLong(csvRecord.get("vendedorId")));
        product.setVendedorName(csvRecord.get("vendedorName"));
        product.setMin_stock(Integer.parseInt(csvRecord.get("min_stock")));

        product.setStatus(StatusProduct.PENDING);



        if(product.isValid())
        {
            return product;
        }

        throw new RuntimeException("Dados do produto invalido.por favor verifica");


    }

}
