package com.fieldright.fr.controller.implementation;

import com.fieldright.fr.controller.interfaces.ExpoPushController;
import com.fieldright.fr.entity.dto.ExpoPushDTO;
import com.fieldright.fr.response.Response;
import com.fieldright.fr.service.interfaces.ExpoPushService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins= {"*"}, maxAge = 4800, allowCredentials = "true" )
@RestController
@RequestMapping("/api/expo/v1")
public class ExpoPushControllerImpl implements ExpoPushController {


    @Autowired
    private ExpoPushService expoPushService;

    @PostMapping(value = "/push")
    @Override
    public Response push(@RequestBody @Valid ExpoPushDTO dto) {
        return expoPushService.sendPushNotification(dto);
    }

    @PostMapping(value = "/pushTokenDefault")
    @Override
    public Response PushTokenDefault(@RequestBody @Valid ExpoPushDTO dto) {
        return expoPushService.sendPushNotificationTokeDefault(dto);
    }
}
