package com.fieldright.fr.service.implementation;

import com.fieldright.fr.entity.CategoriaPostagem;
import com.fieldright.fr.entity.dto.CategoriaPostagemDTO;
import com.fieldright.fr.repository.CategoriaPostagemRepository;
import com.fieldright.fr.response.Response;
import com.fieldright.fr.service.interfaces.CategoriaPostagemService;
import com.fieldright.fr.util.enums.StatusCategoriPostagem;
import com.fieldright.fr.util.mapper.CategoriaPostagemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CategoriaPostagemServiceImpl implements CategoriaPostagemService {

    @Autowired
    private CategoriaPostagemRepository CategoriPostagemRepository;

    @Autowired
    private CategoriaPostagemMapper CategoriPostagemMapper;

    @Value("${src.images.url}")
    private String url;

    @Value("${src.images.past}")
    private String past;

    @Override
    public Response save(CategoriaPostagemDTO dto) {

        String message = "Operacao realizada com sucesso";
        HttpStatus httpStatus = HttpStatus.CREATED;
      /*  CategoriPostagem CategoriPostagem = CategoriPostagem.builder()
                                    .descricao(dto.getDescricao())
                                    .titulo(dto.getTitulo())
                                    .status(StatusCategoriPostagem.ACTIVADO)
                                    .data(LocalDateTime.now()).build();*/

        CategoriaPostagem CategoriPostagem = CategoriPostagemMapper.toCategoriPostagem(dto);
        CategoriPostagem.setData(LocalDateTime.now());
        CategoriPostagem.setStatus(StatusCategoriPostagem.ACTIVADO);

        if(CategoriPostagem != null)
        {
            CategoriPostagemRepository.save(CategoriPostagem);
        }
        else {

            message = "Nao foi possivel registar a CategoriPostagem.operacao cancelada";
            httpStatus = HttpStatus.NOT_IMPLEMENTED;
        }


        return new Response.Builder()
                .withStatus(httpStatus)
                .withData(message)
                .withErrors(null)
                .build();


    }

    @Override
    public Response<Page<CategoriaPostagemDTO>> findByStatus(Integer status, Pageable pageable) {


        Page<CategoriaPostagem> CategoriPostagemPage = null;
        String mensagem = "Status invalido";

        try {

            StatusCategoriPostagem statusCategoriPostagem = StatusCategoriPostagem.toEnum(status);

            if(statusCategoriPostagem != null)
            {
                CategoriPostagemPage = CategoriPostagemRepository.findByStatus(statusCategoriPostagem.getText(),pageable);

                Page<CategoriaPostagemDTO> dtos = CategoriPostagemPage.map(p -> {
                    return CategoriPostagemMapper.toCategoriPostagemDTO(p);

                });


                return new Response.Builder()
                        .withStatus(HttpStatus.OK)
                        .withData(dtos)
                        .withErrors(null)
                        .build();


            }
        }catch (Exception ex)
        {
            ex.printStackTrace();
            mensagem = ex.getMessage();
        }


        return new Response.Builder()
                .withStatus(HttpStatus.NOT_FOUND)
                .withData(mensagem)
                .withErrors(null)
                .build();



    }



    @Override
    public Response update(Long id, CategoriaPostagemDTO dto) {

      Optional<CategoriaPostagem> optional =  CategoriPostagemRepository.findById(id);

      if(optional.isPresent())
      {
          CategoriaPostagem modelo = optional.get();
          modelo.setName(dto.getName());

          modelo = CategoriPostagemRepository.save(modelo);


          return new Response.Builder()
                  .withStatus(HttpStatus.OK)
                  .withData(modelo)
                  .withErrors(null)
                  .build();
      }
        return new Response.Builder()
                .withStatus(HttpStatus.OK)
                .withData("ID nao invalido")
                .withErrors(null)
                .build();
    }

    @Override
    public Response delete(Long id) {

        Optional<CategoriaPostagem> optional =  CategoriPostagemRepository.findById(id);

        if(optional.isPresent())
        {
            CategoriaPostagem modelo = optional.get();

            CategoriPostagemRepository.delete(modelo);


            return new Response.Builder()
                    .withStatus(HttpStatus.OK)
                    .withData("CategoriPostagem eliminada")
                    .withErrors(null)
                    .build();
        }
        return new Response.Builder()
                .withStatus(HttpStatus.OK)
                .withData("ID nao invalido")
                .withErrors(null)
                .build();

    }

    @Override
    public Response updateStatus(Long id, Integer status) {

        Optional<CategoriaPostagem> optional =  CategoriPostagemRepository.findById(id);

        if(optional.isPresent())
        {
            CategoriaPostagem modelo = optional.get();
            modelo.setStatus(StatusCategoriPostagem.toEnum(status));

            modelo = CategoriPostagemRepository.save(modelo);

            return new Response.Builder()
                    .withStatus(HttpStatus.OK)
                    .withData("Operacao realizada com sucesso")
                    .withErrors(null)
                    .build();
        }
        return new Response.Builder()
                .withStatus(HttpStatus.OK)
                .withData("ID nao invalido")
                .withErrors(null)
                .build();
    }

    @Override
    public Response findAll(Pageable pageable) {



        Page<CategoriaPostagem> CategoriPostagemPage = null;
        String mensagem = "Status invalido";

        try {

                CategoriPostagemPage = CategoriPostagemRepository.findAll(pageable);

                Page<CategoriaPostagemDTO> dtos = CategoriPostagemPage.map(p -> {
                    return CategoriPostagemMapper.toCategoriPostagemDTO(p);

                });


                return new Response.Builder()
                        .withStatus(HttpStatus.OK)
                        .withData(dtos)
                        .withErrors(null)
                        .build();


        }catch (Exception ex)
        {
            ex.printStackTrace();
            mensagem = ex.getMessage();
        }


        return new Response.Builder()
                .withStatus(HttpStatus.NOT_FOUND)
                .withData(mensagem)
                .withErrors(null)
                .build();
    }
}
