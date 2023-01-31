package com.fieldright.fr.util.mapper;

import com.fieldright.fr.entity.Compra;
import com.fieldright.fr.entity.Comprador;
import com.fieldright.fr.entity.Endereco;
import com.fieldright.fr.response.XmlBody;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class XmlCreatorTest {

    public static final int CARRINHO_ID = 1;
    public static final BigDecimal TAXA_ENTREGA = new BigDecimal(5);

    /**
     * Descobri que a api da PagSeguro retorna erro de "senderName invalid value" quando no nome do usuário tiver dois espaços seguidos: "  ".
     * Por isso, a existencia desse teste
     */
    @Test
    public void deveRemoverEspacosAMaisNoNomeDoUsuario() {
        Comprador comprador1 = geraComprador(" Pacifique ", " Mukuna ");
        Comprador comprador2 = geraComprador(" Pacifique Cishiku ", " Mukuna ");
        Comprador comprador3 = geraComprador(" Pacifique  Cishiku ", " Mukuna ");
        List<Compra> compras = geraCompras();

        XmlBody xmlBody1 = XmlCreator.createXmlBody(comprador1, compras, CARRINHO_ID, TAXA_ENTREGA);
        XmlBody xmlBody2 = XmlCreator.createXmlBody(comprador2, compras, CARRINHO_ID, TAXA_ENTREGA);
        XmlBody xmlBody3 = XmlCreator.createXmlBody(comprador3, compras, CARRINHO_ID, TAXA_ENTREGA);

        assertEquals("Pacifique Mukuna", xmlBody1.getSender().getName());
        assertEquals("Pacifique Cishiku Mukuna", xmlBody2.getSender().getName());
        assertEquals("Pacifique Cishiku Mukuna", xmlBody3.getSender().getName());
    }

    /**
     * Descobri que a api da PagSeguro retorna erro de "senderName invalid value" quando o nome do usuário for único: (Pacifique).
     * A api está esperando um nome e sobre nome.
     * Por isso, a existencia desse teste
     */
    @Test
    public void deveDuplicarNomeDeUsuarioCasoNaoTiverSobreNome() {
        Comprador comprador1 = geraComprador("Pacifique", null);
        Comprador comprador2 = geraComprador("Pacifique", "  ");
        Comprador comprador3 = geraComprador(null, "Mukuna");
        Comprador comprador4 = geraComprador("  ", "Mukuna");
        List<Compra> compras = geraCompras();

        XmlBody xmlBody1 = XmlCreator.createXmlBody(comprador1, compras, CARRINHO_ID, TAXA_ENTREGA);
        XmlBody xmlBody2 = XmlCreator.createXmlBody(comprador2, compras, CARRINHO_ID, TAXA_ENTREGA);
        XmlBody xmlBody3 = XmlCreator.createXmlBody(comprador3, compras, CARRINHO_ID, TAXA_ENTREGA);
        XmlBody xmlBody4 = XmlCreator.createXmlBody(comprador4, compras, CARRINHO_ID, TAXA_ENTREGA);

        assertEquals("Pacifique Pacifique", xmlBody1.getSender().getName());
        assertEquals("Pacifique Pacifique", xmlBody2.getSender().getName());
        assertEquals("Mukuna Mukuna", xmlBody3.getSender().getName());
        assertEquals("Mukuna Mukuna", xmlBody4.getSender().getName());
    }

    private List<Compra> geraCompras() {
        Compra compra = new Compra.Builder().withEnderecoEntrega(geraEndereco()).build();
        return singletonList(compra);
    }

    private Endereco geraEndereco() {
        Endereco endereco = new Endereco();
        endereco.setCep("12123-123");
        return endereco;
    }

    private Comprador geraComprador(String nome, String sobreNome) {
        Comprador comprador = new Comprador();
        comprador.setFirstName(nome);
        comprador.setLastName(sobreNome);
        return comprador;
    }
}
