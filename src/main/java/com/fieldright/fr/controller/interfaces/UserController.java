package com.fieldright.fr.controller.interfaces;

import com.fieldright.fr.entity.dto.ContaDTO;
import com.fieldright.fr.entity.dto.DepoimentoDTO;
import com.fieldright.fr.entity.dto.UserCompraDTO;
import com.fieldright.fr.response.Response;
import com.fieldright.fr.util.form.NewPasswordForm;
import com.fieldright.fr.util.form.SingUpForm;
import com.fieldright.fr.util.form.UpdateForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDate;

@Api(
        tags = "User endpoints",
        description = "Realiza operações com usuários"
)
public interface UserController {

    @ApiOperation(
            value = "SingUp",
            notes = "Este endpoint deve ser chamado para realizar um novo cadastro de usuário. \n" +
                    "Deve ser passado no body o formulário **SingUpForm** com os campos necessários para cada tipo de usuários. \n" +
                    "Os campos obrigatórios variam de acordo com o tipo conta (Vendedor/comprador/motorista), mas os campos obrigatórios " +
                    "para qualquer tipo de cadastro são os seguintes : \n" +
                    "   * ***email***\n" +
                    "   * ***firstName***\n" +
                    "   * ***lastName***\n" +
                    "   * ***perfil***: *motorista/vendedor/comprador*\n\n" +
                    "**Obs :** *Para cada tipo de perfil, terá alguns campos do formulário que se tornarão obrigatórios.*\n\n" +
                    "Ao realizar o cadastro com sucesso, o usuário receberá um e-mail de boas vindas contendo as credenciais provisórias que são: " +
                    "o email cadastrado e uma senha aleatória que precisa ser alterada no primeiro acesso (*chamando o endpoint AlterPassword*)"
    )
    @ResponseStatus(HttpStatus.CREATED)
    Response<HttpStatus> singUp(SingUpForm form);

    @ApiOperation(
            value = "New password",
            notes = "Este endpoint deve ser usado apenas para alterar a senha de um usuário.\n" +
                    "Basta passar no body o formulário que possui dois campos que devem ter valores idênticos:\n" +
                    "   * **newPassword**\n" +
                    "   * **confirmation**"
    )
    @ResponseStatus(HttpStatus.ACCEPTED)
    Response<HttpStatus> alterPassword(NewPasswordForm form);

    @ApiOperation(
            value = "Forget password",
            notes = "Este endpoint deve ser usado apenas para solicitar uma nova senha para o usuário. (Esqueci minha senha)\n" +
                    "Basta passar no path o e-mail cadastrado do usuário, que receberá um e-mail com uma nova senha aleatória" +
                    " que precisará ser alterada no próximo acesso (*chamando o endpoint AlterPassword*):"
    )
    @ResponseStatus(HttpStatus.ACCEPTED)
    Response<HttpStatus> forgetPassword(String email);

    @ApiOperation(
            value = "Update",
            notes = "Este endpoint deve ser usado apenas para atualizar o cadastro de algum usuário já existente.\n" +
                    "basta passar no body o formulário **UpdateForm** com as informações(campos) que precisam ser atualizadas"
    )
    @ResponseStatus(HttpStatus.ACCEPTED)
    Response<HttpStatus> update(UpdateForm form);

    @ApiOperation(
            value = "Update Account",
            notes = "Este endpoint deve ser usado apenas para atualizar as informações bancárias do vendedor.\n" +
                    "basta passar no body o **ContaDTO** com as informações(campos) que precisam ser atualizadas"
    )
    @ResponseStatus(HttpStatus.OK)
    Response<ContaDTO> updateAccount(ContaDTO conta);

    @ApiOperation(
            value = "Update updateExponentPushToken Infos",
            notes = "Este endpoint deve ser usado apenas para atualizar o ExponentPushToken de um usuário específico"
    )
    @ResponseStatus(HttpStatus.OK)
    Response<HttpStatus> updateExponentPushToken(String exponentPushToken, long userId);

    @ApiOperation(value = "Get countUserCompra by user loja", notes = "Este endpoint deve ser mostrar os clientes que mas compram nunhum certa loja")
    ResponseEntity<Response<Page<UserCompraDTO>>> getCountUserCompraFinalizadaByFilters(long usuarioId, Pageable pageable);
}