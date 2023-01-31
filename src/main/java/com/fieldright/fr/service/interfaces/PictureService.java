package com.fieldright.fr.service.interfaces;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface PictureService {

    byte[] getPicture(long imgId);

    /**
     * @author Pacifique C. Mukuna
     * @implNote Este método serve tanto para salvar uma nova imagem, como para atualizar uma imagem existente.
     * Caso for criar uma nova imagem, basta informar "null" no parâmetro "id". caso contrário, informar o id da imagem que precisa ser atualizada
     * @param picture A foto que precisa salvar
     * @param id O id da foto existente que precisa ser atualizada
     * @return retorna o id da imagem salva
     * @throws IOException
     */
    long updatePicture(MultipartFile picture, Long id) throws IOException;
    void deletePicture(Long id);
}
