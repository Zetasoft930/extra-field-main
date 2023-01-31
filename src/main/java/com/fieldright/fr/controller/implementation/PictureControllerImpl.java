package com.fieldright.fr.controller.implementation;

import com.fieldright.fr.controller.interfaces.PictureController;
import com.fieldright.fr.security.util.JwtUserUtil;
import com.fieldright.fr.service.interfaces.PictureService;
import com.fieldright.fr.service.interfaces.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.io.IOException;

@CrossOrigin
@RestController
@RequestMapping("/images")
@AllArgsConstructor
public class PictureControllerImpl implements PictureController {

    private PictureService pictureService;
    private UserService userService;

    @Override
    @GetMapping(
            value = "/{imgId}",
            produces = MediaType.IMAGE_PNG_VALUE
    )
    public byte[] getPicture(@PathVariable long imgId) {
        return pictureService.getPicture(imgId);
    }

    @Override
    @PutMapping
    public String updateAvatar(@RequestParam MultipartFile picture) throws IOException {
        return userService.updateAvatar(picture, JwtUserUtil.getUserAuthenticated());
    }
}
