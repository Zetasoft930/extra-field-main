package com.fieldright.fr.service.interfaces;

import com.fieldright.fr.entity.*;
import com.fieldright.fr.entity.dto.Loja;
import com.fieldright.fr.entity.dto.UserCompraDTO;
import com.fieldright.fr.entity.dto.UserDTO;
import com.fieldright.fr.entity.security.UserAuthenticated;
import com.fieldright.fr.response.Response;
import com.fieldright.fr.security.dto.TokenDTO;
import com.fieldright.fr.util.enums.StatusVenda;
import com.fieldright.fr.util.enums.TipoVeiculo;
import com.fieldright.fr.util.form.NewPasswordForm;
import com.fieldright.fr.util.form.SingUpForm;
import com.fieldright.fr.util.form.UpdateForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService {

    UserDTO internalFindUserByEmail(String email);

    Usuario internalFindUserById(long id);

    Response<HttpStatus> createUser(SingUpForm form);

    Response<HttpStatus> alterPassword(NewPasswordForm form, UserAuthenticated authenticated);

    Response<HttpStatus> forgetPassword(String email);

    Response<HttpStatus> update(UpdateForm form, UserAuthenticated authenticated);

    String updateAvatar(MultipartFile avatar, UserAuthenticated authenticated) throws IOException;

    void internalUpdateUser(Usuario usuario);

    TokenDTO internalCompleteLogin(String token, long userId);

    void internalAdicioneComprasParaComprador(Long carrinhoId, long userId);

    Response<HttpStatus> updateExponentPushToken(UserAuthenticated authenticated, long userId, String exponentPushToken);

    List<Product> addNewProductInVendedor(Product product, Vendedor vendedor);

    List<Motorista> internalFindAllEntregadorByTiposVeiculoAndCidades(List<TipoVeiculo> veiculos, String... cidades);

    List<Motorista> internalFindAllEntregadorByTipoVeiculo(TipoVeiculo tipoVeiculo);

    Response<Page<Loja>> getLojas(String name, long category, Pageable pageable);


    Response<Page<UserCompraDTO>> findByFilter(long usuarioLojaId, StatusVenda status, Pageable pageable);
}
