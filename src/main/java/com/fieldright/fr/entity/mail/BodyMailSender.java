package com.fieldright.fr.entity.mail;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BodyMailSender {

    private Contact sender;
    private List<Contact> to;
    private String subject;
    private String htmlContent;


    @Getter
    @Setter
    @AllArgsConstructor
    public static class Contact {

        private String name;
        private String email;

    }
}
