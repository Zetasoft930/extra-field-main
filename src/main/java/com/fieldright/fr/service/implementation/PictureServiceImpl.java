package com.fieldright.fr.service.implementation;

import com.fieldright.fr.entity.Picture;
import com.fieldright.fr.repository.PictureRepository;
import com.fieldright.fr.service.interfaces.PictureService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PictureServiceImpl implements PictureService {

    private PictureRepository repository;

    @Override
    public byte[] getPicture(long imgId) {
        Optional<Picture> picture = repository.findById(imgId);

        if(picture.isPresent())
            return picture.get().getBytes();
        return new byte[0];
    }

    @Override
    public long updatePicture(MultipartFile picture, Long id) throws IOException {
        if(id==null){//Criar nova imagem
            Picture pict = new Picture();
            pict.setBytes(picture.getBytes());

            return repository.save(pict).getId();
        }else {//Atualizar uma imagem existente
            Optional<Picture> byId = repository.findById(id);
            if (byId.isPresent()) {
                byId.get().setBytes(picture.getBytes());
                repository.save(byId.get());

                return id;
            }
            return 0;
        }
    }

    @Override
    public void deletePicture(Long id) {
        repository.deleteById(id);
    }
}
