package com.fieldright.fr.notification;

import com.fieldright.fr.entity.Motorista;
import com.fieldright.fr.entity.Usuario;
import com.fieldright.fr.entity.notification.Notification;
import com.fieldright.fr.service.interfaces.UserService;
import com.fieldright.fr.util.enums.TipoVeiculo;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Component
public class PushSender {

    private static final String URL = "https://exp.host/--/api/v2/push/send";
    @Autowired
    private UserService userService;

    public void avisaCompraConfirmada(long compradorId) {
        Usuario comprador = userService.internalFindUserById(compradorId);
        if (comprador.getExponentPushToken() == null) {
            System.out.println("Não foi possível enviar notificação para o comprador com id " + compradorId +
                    "pois ele não possui o ExponentPushToken");
            return;
        }
        JsonObject body = getBody(comprador.getExponentPushToken(),
                "A sua compra foi confirmada!",
                "A sua compra foi confirmada! Em breve estará a caminho.");

        send(body);
    }

    private JsonObject getBody(String to, String title, String body) {
        return new Notification()
                .to(to)
                .setTitle(title)
                .setBody(body)
                .build();
    }

    private JsonObject getBody(List<String> to, String title, String body) {
        return new Notification()
                .toMore(to)
                .setTitle(title)
                .setBody(body)
                .build();
    }

    private void send(JsonObject body) {
        HttpRequest request = getRequest(body.toString());
        try {
            HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private HttpRequest getRequest(String requestBody) {
        return HttpRequest.newBuilder()
                .uri(URI.create(URL))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .setHeader("Content-Type", "application/json")
                .build();
    }

    public void avisaCompraCancelada(long compradorId) {
        Usuario comprador = userService.internalFindUserById(compradorId);
        if (comprador.getExponentPushToken() == null) {
            System.out.println("Não foi possível enviar notificação para o comprador com id " + compradorId +
                    "pois ele não possui o ExponentPushToken");
            return;
        }
        JsonObject body = getBody(comprador.getExponentPushToken(),
                "A sua compra foi cancelada",
                "Sinto muito, a sua compra foi cancelada pelo vendedor! \n" +
                        "Em breve terá seu dinheiro de volta e poderá comprar de um outro vendedor.");

        send(body);
    }

    public void avisaNovaVendaEfetuada(long vendedorId) {
        Usuario vendedor = userService.internalFindUserById(vendedorId);
        if (vendedor.getExponentPushToken() == null) {
            System.out.println("Não foi possível enviar notificação para o vendedor com id " + vendedorId +
                    "pois ele não possui o ExponentPushToken");
            return;
        }
        JsonObject body = getBody(vendedor.getExponentPushToken(),
                "Tem uma nova venda!",
                "Notícia boa! \nTem uma nova venda de seus produtos");

        send(body);
    }

    public void avisaEntregaDisponivel(List<TipoVeiculo> veiculos, String... cidades) {
        List<Motorista> entregadores;
        List<String> toMore = new ArrayList<>();

        if (cidades == null) {
            entregadores = userService.internalFindAllEntregadorByTipoVeiculo(TipoVeiculo.EMPRESA);
        } else {
            entregadores = userService.internalFindAllEntregadorByTiposVeiculoAndCidades(veiculos, cidades);
        }

        if (entregadores.isEmpty()) {
            System.out.println("Não há nenhum entregador para receber essa notificação");
            return;
        }

        for (Motorista motorista : entregadores) {
            toMore.add(motorista.getExponentPushToken());
        }
        JsonObject body = getBody(toMore,
                "Nova entrega!",
                "Tem uma nova entrega para você!");

        send(body);
    }

    public void avisaComprasACaminho(long compradorId, String vendedorName) {
        Usuario comprador = userService.internalFindUserById(compradorId);
        if (comprador.getExponentPushToken() == null) {
            System.out.println("Não foi possível enviar notificação para o comprador com id " + compradorId +
                    "pois ele não possui o ExponentPushToken");
            return;
        }
        JsonObject body = getBody(comprador.getExponentPushToken(),
                "ua compra está à caminho!",
                "Notícia boa! \nSua compra da loja " + vendedorName + " está à caminho.");

        send(body);
    }

//    public void avisaPagamentoRealizado(long usuarioId) {
//        Optional<UserPushNotification> optional = userPushNotificationRepository.findByUserId(usuarioId);
//        if (optional.isEmpty()) {
//            return;
//        }
//        JsonObject body = getBody(Arrays.asList(optional.get()),
//                "Hello! \n" +
//                        "Your Field Right balance was transferred to your bank. Soon it will be available.",
//                "Olá! \n" +
//                        "Foi realizada a transferência do seu saldo Field Right para seu banco. \n" +
//                        "Em breve estará disponível.");
//
//        send(body);
//    }
    
	public void avisaEstoque(long vendedorId, String mensagem) {
		Usuario vendedor = userService.internalFindUserById(vendedorId);
		if (vendedor.getExponentPushToken() == null) {
			System.out.println("Não foi possível enviar notificação para o vendedor com id " + vendedorId
					+ "pois ele não possui o ExponentPushToken");
			return;
		}
		JsonObject body = getBody(vendedor.getExponentPushToken(), "Tem uma nova Notificação!", mensagem);

		send(body);
	}
}
