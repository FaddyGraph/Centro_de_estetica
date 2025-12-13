package com.cefet.centro_de_estetica.mapper;


import org.springframework.stereotype.Component;

import com.cefet.centro_de_estetica.dto.UsuarioRequestDTO;
import com.cefet.centro_de_estetica.dto.UsuarioResponseDTO;
import com.cefet.centro_de_estetica.entity.Usuario;

@Component
public class UsuarioMapper {
	public UsuarioResponseDTO toResponseDTO(Usuario usuario) {
		if(usuario == null) return null;// talvez seja melhor lançar uma exceção
	
		return new UsuarioResponseDTO(usuario);
	}
	
	public Usuario toEntity(UsuarioRequestDTO dto) {
		if (dto == null) return null;
		
		Usuario usuario = new Usuario();
		usuario.setNome(dto.nome());
	    usuario.setTelefone(dto.telefone());
	    usuario.setEmail(dto.email());
	    usuario.setSenha(dto.senha()); 
	    usuario.setStatusUsuario(dto.statusUsuario());
	    usuario.setTipo(dto.tipo());
	    
	 // A lista de serviços (servicosIds) NÃO é mapeada aqui.
	 // isso é feito no Service usando o repository, pois precisamos buscar os objetos no banco.
	    
		return usuario;
	}
}
