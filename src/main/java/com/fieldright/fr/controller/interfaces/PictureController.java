package com.fieldright.fr.controller.interfaces;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Api(
        tags = "Picture endpoints",
        description = "Endpoints para manipulação de imagens"
)
public interface PictureController {

    @ApiOperation(
            value = "getPicture",
            notes = "Este endpoint só será chamado pelo ***CSS*** para recuperar as fotos dos produtos e as fotos de perfil de usuários"
    )
    byte[] getPicture(long id);

    @ApiOperation(
            value = "Salvar/Atualizar a foto de perfil dos usuários",
            notes = "Este endpoint deve ser chamado quando for atualizar a foto de perfil (avatar) do usuário. \n" +
                    "Deve ser passado em parâmetro uma foto com configuração ***form-data*** e com o nome de ***picture*** \n" +
                    "Será retornado apenas um String que conterá o link da imagem salva, que pode ser usado no css."
    )
    String updateAvatar(MultipartFile picture) throws IOException;
}
