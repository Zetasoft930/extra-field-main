package com.fieldright.fr.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@XmlRootElement(name = "checkout")
public class XmlBody {
    private Sender sender;
    @XmlElement
    private String currency;
    private List<Item> items;
    @XmlElement
    private Shipping shipping;
    @XmlElement
    private Receiver receiver;
    @XmlElement
    private long reference;

    @XmlElementWrapper
    @XmlElement(name = "item")
    public List<Item> getItems() {
        return items;
    }

    @XmlElement(name = "sender")
    public Sender getSender() {
        return sender;
    }

    @XmlElementWrapper
    @XmlElement(name = "document")
    public List<Document> getDocuments() {
        return sender.getDocuments();
    }

    @Getter
    @Setter
    public static class Sender {
        private String name;
        private String email;
        private Phone phone;
        private List<Document> documents;
    }

    @Getter
    @Setter
    private static class Phone {
        private int areaCode;
        private long number;
    }

    @Getter
    @Setter
    private static class Document {
        private String type = "CPF"; //Por enquanto, o documento do comprador sempre será o cpf
        private String value;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Item {
        private long id;
        private String description;
        private BigDecimal amount;
        private int quantity;
    }

    @Getter
    @Setter
    private static class Shipping {
        private Address address;
        private int type;
        private BigDecimal cost;
        private boolean addressRequired;
    }

    @Getter
    @Setter
    private static class Address {
        private String street;
        private String number;
        private String complement;
        private String district;
        private String city;
        private String state;
        private String country;
        private long postalCode;
    }

    @Getter
    @Setter
    private static class Receiver {
        private String email;
    }

    public static class Builder {
        private Sender sender;
        private String currency;
        private List<Item> items;
        private Shipping shipping;
        private Receiver receiver;
        private long reference;

        public Builder withSender(String name, String email, Integer areaCode, long number, String cpf) {
            Sender sender = new Sender();
            sender.setName(name);
            sender.setEmail(email);

            if (cpf != null) {
                Document document = new Document();
                document.value = cpf;
                sender.setDocuments(Arrays.asList(document));
            }
            if (areaCode != null) {
                Phone phone = new Phone();
                phone.areaCode = areaCode;
                phone.number = number;
                sender.setPhone(phone);
            }

            this.sender = sender;
            return this;
        }

        public Builder withItens(List<Item> items) {
            this.items = items;
            return this;
        }

        public Builder withShipping(int type, BigDecimal cost, boolean addressRequired, String street, String number,
                                    String complement, String district, String city, String state, String country,
                                    long postalCode) {
            Address address = new Address();
            address.setStreet(street);
            address.setNumber(number);
            address.setComplement(complement);
            address.setDistrict(district);
            address.setCity(city);
            address.setState(state);
            address.setCountry(country);
            address.setPostalCode(postalCode);

            Shipping shipping = new Shipping();
            shipping.setAddress(address);
            shipping.setType(type);
            shipping.setCost(cost);
            shipping.setAddressRequired(addressRequired);
            this.shipping = shipping;
            return this;
        }

        public Builder withReference(long reference) {
            this.reference = reference;
            return this;
        }

        public XmlBody build() {
            XmlBody xmlBody = new XmlBody();
            this.receiver = new Receiver();
            this.currency = "BRL";

            xmlBody.sender = this.sender;
            xmlBody.currency = this.currency;
            xmlBody.items = this.items;
            xmlBody.shipping = this.shipping;
            xmlBody.receiver = this.receiver;
            xmlBody.reference = this.reference;

            return xmlBody;
        }
    }
}
