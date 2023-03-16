package com.fieldright.fr.mail;

import com.fieldright.fr.entity.Product;
import com.fieldright.fr.entity.Usuario;
import com.fieldright.fr.entity.mail.BodyMailSender;
import com.fieldright.fr.service.interfaces.UserService;
import com.google.gson.GsonBuilder;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;

@Component
public class EMailSender {

    @Value("${mail.smtp.adress}")
    private String adress;
    @Value("${mail.smtp.identity}")
    private String identidade;
    @Value("${smtp.sendinblue.api.key}")
    private String api_key;
    private OkHttpClient client;
    private MediaType mediaType;

    @Autowired
    private UserService userService;

    public EMailSender() {
        client = new OkHttpClient().newBuilder()
                        .build();
        mediaType = MediaType.parse("application/json");
    }

    public void sendBoasVindas(Usuario usuario, String password) {
        BodyMailSender bodyMailSender = new BodyMailSender();
        bodyMailSender.setSender(new BodyMailSender.Contact(identidade, adress));
        bodyMailSender.setTo(Arrays.asList(new BodyMailSender.Contact(usuario.getFullName(), usuario.getEmail())));
        bodyMailSender.setSubject("Seja bem-vindo a Field Right, o seu app de confiança");
        bodyMailSender.setHtmlContent("<pre>Parab&eacute;ns! A sua conta foi criada com sucesso.<br /><br /></pre>" +
                        "<pre>Segue os dados de acesso:<br /><br />Email: <strong>" + usuario.getEmail() + "</strong><br />" +
                        "Senha: <strong>" + password + "<br /><br /></strong></pre>" +
                        "<pre>Ao fazer login, voc&ecirc; precisar&aacute; alterar sua senha, pois esta &eacute; provis&oacute;ria!<br /><br /></pre>" +
                        "<pre>Acesse e come&ccedil;a j&aacute; a usar!<br /><br /></pre>" +
                        "<pre>Oportunidades sempre ao seu alcance.<br /><br /></pre>" +
                        "<pre><em>Suporte da Field Right</em></pre>" +
                        "<pre><strong>&nbsp;</strong></pre>");

        send(bodyMailSender);
    }

    public void sendRedefinicaoDeSenha(Usuario usuario, String password) {
        BodyMailSender bodyMailSender = new BodyMailSender();
        bodyMailSender.setSender(new BodyMailSender.Contact(identidade, adress));
        bodyMailSender.setTo(Arrays.asList(new BodyMailSender.Contact(usuario.getFullName(), usuario.getEmail())));
        bodyMailSender.setSubject("Redefinição de senha");
        bodyMailSender.setHtmlContent("<pre>Prezado(a) " + usuario.getFullName() + ",<br /><br />" +
                        "Recentemente, voc&ecirc; solicitou a redefini&ccedil;&atilde;o de senha para sua conta. Segue abaixo a senha provis&oacute;ria para seu pr&oacute;ximo login.<br /><br />" +
                        "Ao fazer login, voc&ecirc; ser&aacute; redirecionado(a) a um formul&aacute;rio de cria&ccedil;&atilde;o de uma nova senha.<br /><br />" +
                        "Senha provis&oacute;ria: <strong>" + password + "<br /></strong><br />" +
                        "Caso n&atilde;o tenha feito esta solicita&ccedil;&atilde;o ou acredita que um usu&aacute;rio n&atilde;o autorizado tenha acessado sua conta, acesse <br /><a href=\"https://fieldright.com/\">https://fieldright.com/</a> para redefinir sua senha imediatamente. Em seguida, inicie sess&atilde;o na p&aacute;gina da conta da sua conta em <br /><a href=\"https://fieldright.com\">https://fieldright.com</a> para revisar e atualizar seus ajustes de seguran&ccedil;a.<br /><br /><br />" +
                        "Atenciosamente,<br /><br /><br />" +
                        "<em>Suporte da Field Right</em>" +
                        "</pre>");

        send(bodyMailSender);
    }

    public void sendCompraEfetuada(Usuario usuario) {
        BodyMailSender bodyMailSender = new BodyMailSender();
        bodyMailSender.setSender(new BodyMailSender.Contact(identidade, adress));
        bodyMailSender.setTo(Arrays.asList(new BodyMailSender.Contact(usuario.getFullName(), usuario.getEmail())));
        bodyMailSender.setSubject("Compra efetuada com sucesso!!");
        bodyMailSender.setHtmlContent("<pre>A sua compra foi feita com sucesso!<br /><br /></pre>" +
                        "<pre>Obrigado por escolher a Field Right e fazer parte dessa fam&iacute;lia! Volte sempre.<br /><br /><br /><br /><" +
                        "em>Suporte da Field Right</em></pre>");

        send(bodyMailSender);
    }

    public void sendNovaVendaEfetuada(Usuario usuario) {
        BodyMailSender bodyMailSender = new BodyMailSender();
        bodyMailSender.setSender(new BodyMailSender.Contact(identidade, adress));
        bodyMailSender.setTo(Arrays.asList(new BodyMailSender.Contact(usuario.getFullName(), usuario.getEmail())));
        bodyMailSender.setSubject("Você tem uma nova venda!!");
        bodyMailSender.setHtmlContent("<pre>Obrigado!<br /><br /></pre>" +
                        "<pre>A cada novo pedido fechado, aumenta nossa certeza de que estamos fornecendo os melhores produtos para seu neg&oacute;cio.<br /><br /><br />" +
                        "<em>Suporte da Field Right</em></pre>");

        send(bodyMailSender);
    }

    public void sendCadastroAtivado(Usuario usuario){
        BodyMailSender bodyMailSender = new BodyMailSender();
        bodyMailSender.setSender(new BodyMailSender.Contact(identidade, adress));
        bodyMailSender.setTo(Arrays.asList(new BodyMailSender.Contact(usuario.getFullName(), usuario.getEmail())));
        bodyMailSender.setSubject("Seu cadastro foi ativado!!");
        bodyMailSender.setHtmlContent("<p>Parab&eacute;ns! O seu cadastro j&aacute; foi ativado com sucesso.</p>" +
                        "<p>Seja bem-vindo &agrave; Field Right<br />Acesse e come&ccedil;a j&aacute; a usar o aplicativo.</p>" +
                        "<p>Oportunidades sempre ao seu alcance!</p>");

        send(bodyMailSender);
    }

    public void send(Usuario usuario,String titulo,String mensagem){
        BodyMailSender bodyMailSender = new BodyMailSender();
        bodyMailSender.setSender(new BodyMailSender.Contact(identidade, adress));
        bodyMailSender.setTo(Arrays.asList(new BodyMailSender.Contact(usuario.getFullName(), usuario.getEmail())));
        bodyMailSender.setSubject(titulo);
        bodyMailSender.setHtmlContent("<p>"+mensagem+"</p>");

        send(bodyMailSender);
    }


    private void send(BodyMailSender MailBody) {
        RequestBody body = RequestBody.create(mediaType, new GsonBuilder().create().toJson(MailBody));
        Request request = new Request.Builder()
                        .url("https://api.sendinblue.com/v3/smtp/email")
                        .method("POST", body)
                        .addHeader("Content-Type", "application/json")
                        .addHeader("api-key", api_key)
                        .build();
        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(Product product)
    {
        String titulo = "Stock em Baixo";
        String mensagem = "";


        Usuario usuario = userService.internalFindUserById(product.getVendedorId());
        if(usuario != null)
        {


         BigDecimal   stock_disponivel = product.qtdDisponivelParaCompra();
         Integer   min_stock = product.getMin_stock();
            if (stock_disponivel.doubleValue() <= min_stock && stock_disponivel.doubleValue() > 1 && min_stock > 1) {

                titulo = "Stock - produto atingiu o ponto de ruptura";
                mensagem = "<p>O produto : <b>" + product.getName() + "</b>.</p>" +
                        "<p>Estoque Minimo Definido:<b>" + product.getMin_stock() + "</b>.</p>" +
                        "<p>Esta com o estoque em baixo :<b>" + stock_disponivel.doubleValue() + "</b>.</p>";

            } else if (stock_disponivel.doubleValue() == 1) {

                titulo = "Stock - produto atingiu o ponto de ruptura";
                mensagem = "<p>O produto : <b>" + product.getName() + "</b>.</p>" +
                        "<p>Estoque Minimo Definido:<b>" + product.getMin_stock() + "</b>.</p>" +
                        "<p>Esta com o estoque igual a  :<b>" + stock_disponivel.doubleValue() + "</b>.</p>";

            } else if (stock_disponivel.doubleValue() == 0) {

                titulo = "Stock - produto atingiu o ponto de ruptura";
                mensagem = "<p>O produto : <b>" + product.getName() + "</b>.</p>" +
                        "<p>Estoque Minimo Definido:<b>" + product.getMin_stock() + "</b>.</p>" +
                        "<p>Esta com o estoque esgotado  :<b>" + stock_disponivel.doubleValue() + "</b>.</p>";
            }


            send(usuario,titulo,mensagem);
        }
    }
    public void send(Product product,String titulo,String mensagem)
    {


        Usuario usuario = userService.internalFindUserById(product.getVendedorId());
        if(usuario != null)
        {

            send(usuario,titulo,mensagem);
        }
    }

}
