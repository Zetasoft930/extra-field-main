package com.fieldright.fr.service.implementation;

import com.fieldright.fr.entity.dto.CarrinhoDTO;
import com.fieldright.fr.service.interfaces.EmisService;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;

@Service
public class EmisServiceImpl implements EmisService {
    @Value("${gpo.gateway.url}")
    private String url;
    @Value("${gpo.gateway.token}")
    private String token;
    @Value("${gpo.gateway.callbackUrl}")
    private String callbackUrl;
    private static final String APPLICATION_JSON_VALUE = "application/json";

    @Override
    public String efetuaPagamentoDeProdutos(CarrinhoDTO carrinho) {
        return getTokenCompra(carrinho);
    }

    private String getTokenCompra(CarrinhoDTO carrinho) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse(APPLICATION_JSON_VALUE);
        RequestBody body = RequestBody.create(mediaType, getBody(carrinho));
        Request request = new Request.Builder()
                .url(url)
                .method("POST", body)
                .addHeader("Content-Type", APPLICATION_JSON_VALUE)
                .build();
        try {
            Response response = client.newCall(request).execute();
            return getTokenCompra(response);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getTokenCompra(Response response) throws IOException {
        Gson gson = new Gson();
        TokenCompra tokenCompra = gson.fromJson(response.body().string(), TokenCompra.class);
        return tokenCompra.getId();
    }

    private String getBody(CarrinhoDTO carrinho) {
        Body body = new Body(String.valueOf(carrinho.getId()), carrinho.getVlTotal(), token, callbackUrl);
        return body.json();
    }

    @AllArgsConstructor
    private class Body {
        private String reference;
        private BigDecimal amount;
        private String token;
        private String callbackUrl;

        public String json() {
            Gson gson = new Gson();
            return gson.toJson(this);
        }
    }

    @Getter
    @Setter
    private class TokenCompra {
        private String id;
        private long timeToLive;
    }
}
