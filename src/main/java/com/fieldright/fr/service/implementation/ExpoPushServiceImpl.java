package com.fieldright.fr.service.implementation;

import com.fieldright.fr.entity.dto.ExpoPushDTO;
import com.fieldright.fr.response.Response;
import com.fieldright.fr.service.interfaces.ExpoPushService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class ExpoPushServiceImpl implements ExpoPushService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${expo.push.url}")
    private String expoUrl;

    @Value("${expo.push.token}")
    private String expoToken;

    @Override
    public Response sendPushNotification(ExpoPushDTO expoPushDTO) {

        return  this.sendPushNotification(expoPushDTO.getBody(),expoPushDTO.getTitle(),expoPushDTO.getTo());

    }

    @Override
    public Response sendPushNotificationTokeDefault(ExpoPushDTO expoPushDTO) {

        return  this.sendPushNotification(expoPushDTO.getBody(),expoPushDTO.getTitle(),expoToken);

    }

    private Response sendPushNotification(String message,String title,String token)
    {
        String responseMessage = "Error sending push notification";
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        try
        {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> body = new HashMap<>();
            body.put("to", token);
            body.put("title", title);
            body.put("body", message);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);


            ResponseEntity<String> response = restTemplate.postForEntity(expoUrl, request, String.class);

            httpStatus = response.getStatusCode();

            if (response.getStatusCode().is2xxSuccessful()) {
                responseMessage ="Push notification sent successfully";
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }


        return new Response.Builder()
                .withStatus(httpStatus)
                .withData(responseMessage)
                .withErrors(null)
                .build();
    }
}
