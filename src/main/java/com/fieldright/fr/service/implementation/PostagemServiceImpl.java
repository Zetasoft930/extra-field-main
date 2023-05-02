package com.fieldright.fr.service.implementation;

import com.fieldright.fr.entity.Comentario;
import com.fieldright.fr.entity.Postagem;
import com.fieldright.fr.entity.Usuario;
import com.fieldright.fr.entity.dto.ComentarioDTO;
import com.fieldright.fr.entity.dto.PostagemDTO;
import com.fieldright.fr.entity.dto.ProductDTO;
import com.fieldright.fr.repository.PostagemRepository;
import com.fieldright.fr.response.Response;
import com.fieldright.fr.service.interfaces.PostagemService;
import com.fieldright.fr.service.interfaces.UserService;
import com.fieldright.fr.util.FileUtil;
import com.fieldright.fr.util.enums.StatusComentario;
import com.fieldright.fr.util.enums.StatusPostagem;
import com.fieldright.fr.util.mapper.ComentarioMapper;
import com.fieldright.fr.util.mapper.PostagemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;

@Service
public class PostagemServiceImpl implements PostagemService {

    @Autowired
    private PostagemRepository postagemRepository;

    @Autowired
    private PostagemMapper postagemMapper;

    @Value("${src.images.url}")
    private String url;

    @Value("${src.images.past}")
    private String past;

    @Autowired
    private FileUtil fileUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private ComentarioMapper comentarioMapper;

    @Override
    public Response save(PostagemDTO dto,MultipartFile file) {

        String message = "Operacao realizada com sucesso";
        HttpStatus httpStatus = HttpStatus.CREATED;

        String nameFile =   fileUtil.transfere(file);

        if(nameFile != null) {

            Postagem postagem = postagemMapper.toPostagem(dto);
            postagem.setImagem(nameFile);
            postagem.setData(LocalDateTime.now());
            postagem.setStatus(StatusPostagem.ACTIVADO);

            if (postagem != null) {
                postagemRepository.save(postagem);
            } else {

                message = "Nao foi possivel registar a postagem.operacao cancelada";
                httpStatus = HttpStatus.NOT_IMPLEMENTED;
            }
        }else {

            message = "Nao foi possivel Salvar a imagem da postagem";
            httpStatus = HttpStatus.NOT_IMPLEMENTED;
        }


        return new Response.Builder()
                .withStatus(httpStatus)
                .withData(message)
                .withErrors(null)
                .build();


    }

    @Override
    public Response<Page<PostagemDTO>> findByStatus(Integer status, Pageable pageable) {


        Page<Postagem> postagemPage = null;
        String mensagem = "Status invalido";

        try {

            StatusPostagem statusPostagem = StatusPostagem.toEnum(status);

            if(statusPostagem != null)
            {
                postagemPage = postagemRepository.findByStatus(statusPostagem,pageable);

                Page<PostagemDTO> dtos = postagemPage.map(p -> {
                    PostagemDTO postagemDTO =  postagemMapper.toPostagemDTO(p);
                    postagemDTO.setComentarios(new HashSet<>());

                    for(Comentario comentario : p.getComentarios())
                    {

                        if(comentario.getStatus() == StatusComentario.ACCEPTED) {

                            Usuario usuario = userService.internalFindUserById(comentario.getUsuarioId());

                            ComentarioDTO comentarioDTO = comentarioMapper.toComentarioDTO(comentario);
                            comentarioDTO.setAvatar(usuario.getAvatar());
                            comentarioDTO.setLastName(usuario.getLastName());
                            comentarioDTO.setFirstName(usuario.getFirstName());
                            comentarioDTO.setTipoUsuario(usuario.getPerfil());
                            postagemDTO.getComentarios().add(comentarioDTO);
                        }
                    }

                    return  postagemDTO;

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
    public Response update(Long id, PostagemDTO dto) {

      Optional<Postagem> optional =  postagemRepository.findById(id);

      if(optional.isPresent())
      {
          Postagem modelo = optional.get();
          modelo.setTitulo(dto.getTitulo());
          modelo.setDescricao(dto.getDescricao());

          modelo = postagemRepository.save(modelo);


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

        Optional<Postagem> optional =  postagemRepository.findById(id);

        if(optional.isPresent())
        {
            Postagem modelo = optional.get();

            postagemRepository.delete(modelo);


            return new Response.Builder()
                    .withStatus(HttpStatus.OK)
                    .withData("postagem eliminada")
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

        Optional<Postagem> optional =  postagemRepository.findById(id);

        if(optional.isPresent())
        {
            Postagem modelo = optional.get();
            modelo.setStatus(StatusPostagem.toEnum(status));

            modelo = postagemRepository.save(modelo);

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



        Page<Postagem> postagemPage = null;
        String mensagem = "Status invalido";

        try {

                postagemPage = postagemRepository.findAll(pageable);

                Page<PostagemDTO> dtos = postagemPage.map(p -> {
                    PostagemDTO postagemDTO = postagemMapper.toPostagemDTO(p);


                    for(Comentario comentario : p.getComentarios())
                    {

                        if(comentario.getStatus() == StatusComentario.ACCEPTED) {

                            Usuario usuario = userService.internalFindUserById(comentario.getUsuarioId());

                            ComentarioDTO comentarioDTO = comentarioMapper.toComentarioDTO(comentario);
                            comentarioDTO.setAvatar(usuario.getAvatar());
                            comentarioDTO.setLastName(usuario.getLastName());
                            comentarioDTO.setFirstName(usuario.getFirstName());
                            comentarioDTO.setTipoUsuario(usuario.getPerfil());
                            postagemDTO.getComentarios().add(comentarioDTO);
                        }
                    }


                    return postagemDTO;

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
