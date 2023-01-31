package com.fieldright.fr.util.mapper;

import com.fieldright.fr.entity.Compra;
import com.fieldright.fr.entity.Comprador;
import com.fieldright.fr.entity.Endereco;
import com.fieldright.fr.response.XmlBody;
import com.fieldright.fr.util.StringUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class XmlCreator {

    public static XmlBody createXmlBody(Comprador comprador, List<Compra> compras, long carrinhoId, BigDecimal taxaEntrega) {
        Endereco enderecoEntrega = compras.get(0).getEnderecoEntrega();
        Integer areaCode = null;
        long number = 0L;
        getPhoneInformations(comprador, areaCode, number);

        return new XmlBody.Builder()
                .withSender(retornaNomeCompletoTratado(comprador), comprador.getEmail(), areaCode, number,
                        null)
                .withItens(getItems(compras))
                .withShipping(1,
                        taxaEntrega,
                        true,
                        enderecoEntrega.getRua(),
                        enderecoEntrega.getNumero(),
                        "",
                        enderecoEntrega.getBairro(),
                        enderecoEntrega.getCidade(),
                        enderecoEntrega.getEstado(),
                        enderecoEntrega.getPais(),
                        getCep(enderecoEntrega))
                .withReference(carrinhoId)
                .build();

    }

    private static String retornaNomeCompletoTratado(Comprador comprador) {
        String nomeTratado;
        String nome = comprador.getFirstName();
        String sobreNome = comprador.getLastName();

        if (nomeValido(nome) && nomeValido(sobreNome)) {
            nomeTratado = comprador.getFullName();
        } else {
            nomeTratado = !nomeValido(nome) ? sobreNome + " " + sobreNome : nome + " " + nome;
        }
        nomeTratado = StringUtils.trimLeadingWhitespace(nomeTratado);
        nomeTratado = StringUtils.trimTrailingWhitespace(nomeTratado);

        while (nomeTratado.contains("  ")) {
            nomeTratado = StringUtils.replace(nomeTratado, "  ", " ");
        }
        return nomeTratado;
    }

    private static boolean nomeValido(String nome) {
        return nome != null && !nome.isBlank();
    }

    private static long getCep(Endereco enderecoEntrega) {
        String cepEntrega = enderecoEntrega.getCep();
        long cep;

        if (cepEntrega.contains("-")) {
            String textBefore = StringUtil.getTextBeforeElement(cepEntrega, "-");
            String textAfter = StringUtil.getTextAfterElement(cepEntrega, "-");
            cep = Long.parseLong(textBefore + textAfter);
        } else {
            cep = Long.parseLong(cepEntrega);
        }
        return cep;
    }

    private static List<XmlBody.Item> getItems(List<Compra> compras) {
        List<XmlBody.Item> items = new ArrayList<>(1);

        for (Compra compra : compras) {
            items.add(new XmlBody.Item(
                    compra.getProductId(),
                    compra.getProductDescription(),
                    compra.getProductPrice(),
                    compra.getQtdComprada()));
        }
        return items;
    }

    private static void getPhoneInformations(Comprador comprador, Integer areaCode, long number) {
        String compradorPhone = comprador.getPhone();
        if (nomeValido(compradorPhone)) {
            areaCode = Integer.parseInt(String.valueOf(compradorPhone.charAt(0) + compradorPhone.charAt(1)));
            number = Long.parseLong(compradorPhone.substring(1));
        }
    }

}
