package com.fieldright.fr.util.enums;

public enum StatusComentario {
	
	PENDING(1L), // Aguardando a confirmação do admin, visível apenas para o admin
	ACCEPTED(2L); // Comentário aceite

	private Long code;

	StatusComentario(Long code) {
		this.code = code;
	}


	public static StatusComentario toEnum(Long code)
	{

		for(StatusComentario s : StatusComentario.values())
		{

			if(s.getCode().equals(code))
			{
				return s;
			}
		}

		throw new RuntimeException("Status invalido");
	}

	public Long getCode() {
		return code;
	}
}
