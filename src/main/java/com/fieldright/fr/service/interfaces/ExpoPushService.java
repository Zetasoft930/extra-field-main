package com.fieldright.fr.service.interfaces;

import com.fieldright.fr.entity.dto.ExpoPushDTO;
import com.fieldright.fr.response.Response;

public interface ExpoPushService {


    public Response sendPushNotification(ExpoPushDTO expoPushDTO);
    public Response sendPushNotificationTokeDefault(ExpoPushDTO expoPushDTO);

}
