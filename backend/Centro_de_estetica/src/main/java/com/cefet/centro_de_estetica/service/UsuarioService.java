package com.cefet.centro_de_estetica.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.cefet.centro_de_estetica.dto.UsuarioRequestDTO;
import com.cefet.centro_de_estetica.dto.UsuarioResponseDTO;
import com.cefet.centro_de_estetica.entity.HorarioUsuario;
import com.cefet.centro_de_estetica.entity.Servico;
import com.cefet.centro_de_estetica.entity.Usuario;
import com.cefet.centro_de_estetica.enums.StatusUsuario;
import com.cefet.centro_de_estetica.enums.TipoUsuario;
import com.cefet.centro_de_estetica.mapper.UsuarioMapper;
import com.cefet.centro_de_estetica.repository.ServicoRepository;
import com.cefet.centro_de_estetica.repository.UsuarioRepository;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    private final ServicoRepository servicoRepository;
    private final UsuarioMapper mapper;

    // Construtor
    UsuarioService(UsuarioRepository repository, UsuarioMapper mapper, ServicoRepository servicoRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.servicoRepository = servicoRepository;
    }

    // --- MÉTODOS EXISTENTES ---

    public UsuarioResponseDTO salvar(UsuarioRequestDTO requestDTO) {
        if (repository.existsByEmail(requestDTO.email())) {
            throw new RuntimeException("Email já cadastrado!");
        }
        
        Usuario usuario = mapper.toEntity(requestDTO);
        
        // Define padrões se vier nulo
        if (usuario.getStatusUsuario() == null) {
            usuario.setStatusUsuario(StatusUsuario.ATIVO); 
        }
        if (usuario.getTipo() == null) {
            usuario.setTipo(TipoUsuario.CLIENTE);
        }
        
        // Se for funcionário, vincula serviços
        if (requestDTO.servicosIDs() != null && !requestDTO.servicosIDs().isEmpty()) {
            List<Servico> servicosEncontrados = servicoRepository.findAllById(requestDTO.servicosIDs());
            usuario.setServicos(servicosEncontrados);
        }
        
        Usuario usuarioSalvo = repository.save(usuario);
        return new UsuarioResponseDTO(usuarioSalvo);
    }

    public List<UsuarioResponseDTO> listarTodos() {
        return repository.findAll().stream().map(mapper::toResponseDTO).collect(Collectors.toList());
    }
    
    public List<UsuarioResponseDTO> listarProfissionaisPorServico(Long idServico) {
        List<Usuario> usuarios = repository.findByServicos_Id(idServico); 
        return usuarios.stream().map(mapper::toResponseDTO).collect(Collectors.toList()); 
    }

    public Optional<UsuarioResponseDTO> buscarPorId(Long id) {
        return repository.findById(id).map(mapper::toResponseDTO);
    }
    
    public List<UsuarioResponseDTO> buscarProfissionaisPorParteDoNome(String nome) {
        return repository.findByNomeContainingIgnoreCaseAndTipo(nome, TipoUsuario.FUNCIONARIO)
                .stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<UsuarioResponseDTO> buscarPorEmail(String email) {
        return repository.findByEmail(email).map(mapper::toResponseDTO);
    }
    
    public boolean verificarSeEmailExiste(String email) {
        return repository.existsByEmail(email);
    }

    public UsuarioResponseDTO atualizar(Long id, UsuarioRequestDTO dto) {
        Usuario usuarioEncontrado = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com id: " + id));

        usuarioEncontrado.setNome(dto.nome());
        usuarioEncontrado.setTelefone(dto.telefone());
        usuarioEncontrado.setEmail(dto.email());
        
        // Obs: Idealmente a senha só deve ser alterada se o campo vier preenchido
        if (dto.senha() != null && !dto.senha().isEmpty()) {
             usuarioEncontrado.setSenha(dto.senha()); 
        }
        
        usuarioEncontrado.setStatusUsuario(dto.statusUsuario());
        usuarioEncontrado.setTipo(dto.tipo());

        // Atualiza horários se enviado
        if (dto.horarios() != null) {
            usuarioEncontrado.getHorarios().clear();
            List<HorarioUsuario> novosHorarios = dto.horarios().stream().map(horario -> {
                HorarioUsuario novo = new HorarioUsuario();
                novo.setDiaSemana(horario.getDiaSemana());
                novo.setHorarioInicio(horario.getHorarioInicio());
                novo.setHorarioFim(horario.getHorarioFim());
                novo.setFuncionario(usuarioEncontrado);
                return novo;
            }).collect(Collectors.toList());
            usuarioEncontrado.getHorarios().addAll(novosHorarios);
        }
        
        // Atualiza serviços se enviado
        if (dto.servicosIDs() != null) {
            List<Servico> novosServicos = servicoRepository.findAllById(dto.servicosIDs());
            usuarioEncontrado.setServicos(novosServicos);
        }

        Usuario usuarioAtualizada = repository.save(usuarioEncontrado);
        return mapper.toResponseDTO(usuarioAtualizada);
    }

    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Não foi possível deletar. Usuario com id " + id + " não encontrado.");
        }
        repository.deleteById(id);
    }

    // --- NOVOS MÉTODOS PARA A TELA DE CLIENTES ---

    // 1. Listar apenas quem é CLIENTE
    public List<UsuarioResponseDTO> listarClientes() {
        return repository.findByTipo(TipoUsuario.CLIENTE)
                .stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    // 2. Alternar Status (Ativar/Desativar)
    public UsuarioResponseDTO alterarStatus(Long id) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Lógica de inversão
        if (usuario.getStatusUsuario() == StatusUsuario.ATIVO) {
            usuario.setStatusUsuario(StatusUsuario.DESATIVADO);
        } else {
            usuario.setStatusUsuario(StatusUsuario.ATIVO);
        }

        Usuario salvo = repository.save(usuario);
        return mapper.toResponseDTO(salvo);
    }
}