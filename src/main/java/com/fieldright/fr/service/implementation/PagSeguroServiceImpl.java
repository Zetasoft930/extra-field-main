package com.fieldright.fr.service.implementation;

import com.fieldright.fr.response.XmlBody;
import com.fieldright.fr.service.interfaces.PagSeguroService;
import com.google.gson.Gson;
import lombok.Getter;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.Date;

@Service
public class PagSeguroServiceImpl implements PagSeguroService {

    private static final String URL_TRANFER = "https://ws.pagseguro.uol.com.br/transfer";
    private static final String URL_CHECKOUT = "https://ws.pagseguro.uol.com.br/v2/checkout";
    private static final String URL_CONSULTA_TRANSACAO = "https://ws.pagseguro.uol.com.br/v3/transactions/notifications/";
    private static final String pathBalance = "/balance";
    private static final String pathRequests = "/requests";
    private static final String pathAuthorize = "/authorize";
    private static final String CONTENT_TYPE_JSON = "application/vnd.pagseguro.com.br.v1+json";
    private static final String CONTENT_TYPE_XML = "application/xml; charset=UTF-8";
    @Value("${pagseguro.email}")
    private String email;
    @Value("${pagseguro.token}")
    private String token;
    private OkHttpClient client;

    public PagSeguroServiceImpl() {
        client = new OkHttpClient().newBuilder()
                .build();
    }

    @Override
    public double consultaSaldo() {
        Request request = new Request.Builder()
                .url(URL_TRANFER + pathBalance + "?email=" + email + "&token=" + token)
                .method("GET", null)
                .addHeader("Content-Type", CONTENT_TYPE_JSON)
                .build();
        try {
            Response response = client.newCall(request).execute();
            Gson gson = new Gson();
            Saldo saldo = gson.fromJson(response.body().string(), Saldo.class);

            return saldo.getValue();
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public String solicitaAutorizacao(String email, BigDecimal valor) {
        MediaType mediaType = MediaType.parse(CONTENT_TYPE_JSON);
        RequestBody body = RequestBody.create(mediaType, "{" +
                "\r\n\t\"receiverEmail\": \"" + email +
                "\",\r\n\t\"amount\": \"" + valor +
                "\",\r\n\t\"description\": \"Pagamento de serviços FieldRight\"\r\n}");
        Request request = new Request.Builder()
                .url(URL_TRANFER + pathRequests + "?email=" + email + "&token=" + token)
                .method("POST", body)
                .addHeader("Content-Type", CONTENT_TYPE_JSON)
                .build();
        try {
            Response response = client.newCall(request).execute();
            Gson gson = new Gson();
            JsonResponse jsonResponse = gson.fromJson(response.body().string(), JsonResponse.class);

            return jsonResponse.getTransferCode();
        } catch (IOException e) {
            throw new RuntimeException("Não foi possível realizar o pagamento. \n Erro: " + e.getMessage());
        }
    }

    @Override
    public JsonResponse autorizaPagamento(String authorizationCode) {
        MediaType mediaType = MediaType.parse(CONTENT_TYPE_JSON);
        RequestBody body = RequestBody.create(mediaType, "{\r\n\t\"authorizationCode\": \"" + authorizationCode + "\"\r\n}");
        Request request = new Request.Builder()
                .url(URL_TRANFER + pathAuthorize + "?email=" + email + "&token=" + token)
                .method("POST", body)
                .addHeader("Content-Type", CONTENT_TYPE_JSON)
                .build();
        try {
            Response response = client.newCall(request).execute();
            Gson gson = new Gson();
            JsonResponse jsonResponse = gson.fromJson(response.body().string(), JsonResponse.class);

            return jsonResponse;
        } catch (IOException e) {
            throw new RuntimeException("Não foi possível realizar o pagamento. \n Erro: " + e.getMessage());
        }
    }

    @Override
    public String efetuaPagamentoDeProdutos(XmlBody xmlBody) {
        try {
            StringWriter writer = getStringWriter(xmlBody);
            MediaType mediaType = MediaType.parse(CONTENT_TYPE_XML);
            RequestBody body = RequestBody.create(mediaType, writer.toString());
            Request request = new Request.Builder()
                    .url(URL_CHECKOUT + "?email=" + email + "&token=" + token)
                    .method("POST", body)
                    .addHeader("Content-Type", CONTENT_TYPE_XML)
                    .build();
            Response response = client.newCall(request).execute();
            validaResposta(response);
            XmlCheckoutResponse xmlCheckoutResponse = getXmlCheckoutResponse(response);

            return xmlCheckoutResponse.getCode();
        } catch (IOException | JAXBException e) {
            throw new RuntimeException("Não foi possível realizar o pagamento. \n Erro: " + e.getMessage());
        }
    }

    private void validaResposta(Response response) throws IOException {
        if (response.code() != 200){
            throw new RuntimeException(response.body().string());
        }
    }

    @Override
    public XmlTransactionResponse consultaPagamento(String notificationCode) {
        Request request = new Request.Builder()
                .url(URL_CONSULTA_TRANSACAO + notificationCode + "?email=" + email + "&token=" + token)
                .method("GET", null)
                .addHeader("Content-Type", CONTENT_TYPE_XML)
                .build();
        try {
            Response response = client.newCall(request).execute();
            return getXmlTransactionResponse(response);
        } catch (IOException | JAXBException e) {
            throw new RuntimeException("Não foi possível realizar o pagamento. \n Erro: " + e.getMessage());
        }
    }

    private XmlCheckoutResponse getXmlCheckoutResponse(Response response) throws JAXBException, IOException {
        Unmarshaller unmarshaller = JAXBContext.newInstance(XmlCheckoutResponse.class).createUnmarshaller();
        StringReader reader = new StringReader(response.body().string());
        XmlCheckoutResponse xmlCheckoutResponse = (XmlCheckoutResponse) unmarshaller.unmarshal(reader);
        return xmlCheckoutResponse;
    }

    private XmlTransactionResponse getXmlTransactionResponse(Response response) throws JAXBException, IOException {
        Unmarshaller unmarshaller = JAXBContext.newInstance(XmlTransactionResponse.class).createUnmarshaller();
        StringReader reader = new StringReader(response.body().string());
        XmlTransactionResponse xmlTransactionResponse = (XmlTransactionResponse) unmarshaller.unmarshal(reader);
        return xmlTransactionResponse;
    }

    @NotNull
    private StringWriter getStringWriter(XmlBody xmlBody) throws JAXBException {
        Marshaller marshaller = JAXBContext.newInstance(XmlBody.class).createMarshaller();
        StringWriter writer = new StringWriter();
        marshaller.marshal(xmlBody, writer);
        return writer;
    }

    @Getter
    private class Saldo {
        private double value;
    }

    @Getter
    public class JsonResponse {
        private String transferCode;
        private String transactionCode;
        private String date;
        private boolean error;
        private Errors errors;
    }

    @Getter
    private class Errors {
        private String AUTHORIZATION_ALREADY_GRANTED;
        private String TRANSFER_USER_CAN_NOT_USE_BALANCE;
        private String AMOUNT_LESS_THAN_MINIMUM;
        private String INVALID_AUTHORIZATION_CODE;
        private String INVALID_RECEIVERS_EMAIL;
    }

    @Getter
    @XmlRootElement(name = "checkout")
    private static class XmlCheckoutResponse {
        @XmlElement(name = "code")
        private String code;
        @XmlElement(name = "date")
        private Date date;
    }

    @Getter
    @XmlRootElement(name = "transaction")
    public static class XmlTransactionResponse {
        @XmlElement
        private int status;
        @XmlElement
        private long reference;
    }
}
