package com.fieldright.fr.entity.dto;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ComentarioDTO {
	
    private long id;
    private String comentario;
    private long usuarioId;
    private String status;
    private String tipoUsuario;
    private String avatar;
    private String firstName;
    private String lastName;
    private String createdAt;

}
