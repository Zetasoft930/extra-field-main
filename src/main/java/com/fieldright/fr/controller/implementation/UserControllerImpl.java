package com.fieldright.fr.controller.implementation;

import com.fieldright.fr.controller.interfaces.UserController;
import com.fieldright.fr.entity.dto.ContaDTO;
import com.fieldright.fr.entity.dto.UserCompraDTO;
import com.fieldright.fr.response.Response;
import com.fieldright.fr.security.util.JwtUserUtil;
import com.fieldright.fr.service.interfaces.ContaService;
import com.fieldright.fr.service.interfaces.UserService;
import com.fieldright.fr.util.enums.StatusVenda;
import com.fieldright.fr.util.form.NewPasswordForm;
import com.fieldright.fr.util.form.SingUpForm;
import com.fieldright.fr.util.form.UpdateForm;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/user/v1")
@AllArgsConstructor
@CrossOrigin
public class UserControllerImpl implements UserController {

    private UserService userService;
    private ContaService contaService;

    @Override
    @PostMapping(
            value = "/singUp"
    )
    public Response<HttpStatus> singUp(@RequestBody @Valid SingUpForm form) {
        return userService.createUser(form);
    }

    @Override
    @PutMapping(
            value = "/alterPassword"
    )
    public Response<HttpStatus> alterPassword(@RequestBody @Valid NewPasswordForm form) {
        return userService.alterPassword(form, JwtUserUtil.getUserAuthenticated());
    }

    @Override
    @PostMapping(
            value = "/forgetPassword",
            params = "email"
    )
    public Response<HttpStatus> forgetPassword(String email) {
        return userService.forgetPassword(email);
    }

    @Override
    @PutMapping(
            value = "/update"
    )
    public Response<HttpStatus> update(@RequestBody @Valid UpdateForm form) {
        return userService.update(form, JwtUserUtil.getUserAuthenticated());
    }

    @Override
    @PutMapping(
            value = "/updateAccount"
    )
    public Response<ContaDTO> updateAccount(@RequestBody ContaDTO conta) {
        return contaService.updateAccount(conta, JwtUserUtil.getUserAuthenticated());
    }

    @Override
    @PutMapping(
            value = "/updateExponentPushToken",
            params = {"exponentPushToken", "userId"}
    )
    public Response<HttpStatus> updateExponentPushToken(String exponentPushToken, long userId) {
        return userService.updateExponentPushToken(JwtUserUtil.getUserAuthenticated(), userId, exponentPushToken);
    }

    @GetMapping(value = "/countUserCompra")
    @Override
    public ResponseEntity<Response<Page<UserCompraDTO>>> getCountUserCompraFinalizadaByFilters(@RequestParam(name = "userLoja",required = true) long usuarioId,
                                                                                               Pageable pageable) {

        Response<Page<UserCompraDTO>> response =  userService.findByFilter(usuarioId,
                StatusVenda.FINALIZADA,
                pageable);

      return   new ResponseEntity<>(response, response.getStatus());
    }
}
