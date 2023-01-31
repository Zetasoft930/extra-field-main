package com.fieldright.fr.util.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class NewPasswordForm {

    private String newPassword;
    private String confirmation;

    public boolean matchingPasswords(){
        return newPassword.equals(confirmation);
    }
}
