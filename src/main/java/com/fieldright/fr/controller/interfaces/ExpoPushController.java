package com.fieldright.fr.controller.interfaces;

import com.fieldright.fr.entity.dto.CategoriaDTO;
import com.fieldright.fr.entity.dto.ExpoPushDTO;
import com.fieldright.fr.response.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Api(
        tags = "Expo endpoints",
        description = "Realiza operações com categorias de produto"
)
public interface ExpoPushController {

    @ApiOperation(
            value = "Push Notification",
            notes = "Este endpoint Envia notificacao"
    )
    Response push(ExpoPushDTO dto);

    @ApiOperation(
            value = "PushTokenDefault Notification",
            notes = "Este endpoint Envia notificacao usando token default"
    )
    Response PushTokenDefault(ExpoPushDTO dto);
}
