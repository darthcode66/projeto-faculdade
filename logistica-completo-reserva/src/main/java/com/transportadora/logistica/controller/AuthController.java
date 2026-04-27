package com.transportadora.logistica.controller;

import com.transportadora.logistica.model.Usuario;
import com.transportadora.logistica.repository.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String senha = credentials.get("senha");

        return usuarioRepository.findByEmail(email)
                .filter(u -> passwordEncoder.matches(senha, u.getSenha()))
                .map(u -> {
                    Map<String, Object> response = Map.of(
                            "id", u.getId(),
                            "nome", u.getNome(),
                            "email", u.getEmail(),
                            "perfil", u.getPerfil().name()
                    );
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.status(401)
                        .body(Map.of("erro", "Email ou senha invalidos")));
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@RequestBody Map<String, String> dados) {
        if (usuarioRepository.existsByEmail(dados.get("email"))) {
            return ResponseEntity.badRequest().body(Map.of("erro", "Email ja cadastrado"));
        }

        Usuario usuario = new Usuario();
        usuario.setNome(dados.get("nome"));
        usuario.setEmail(dados.get("email"));
        usuario.setSenha(passwordEncoder.encode(dados.get("senha")));
        usuario.setPerfil(Usuario.Perfil.OPERADOR);

        usuarioRepository.save(usuario);
        return ResponseEntity.ok(Map.of("mensagem", "Usuario registrado com sucesso"));
    }
}
