package cl.javadevs.springsecurityjwt.services;

import cl.javadevs.springsecurityjwt.dtos.auth.DtoLogin;
import cl.javadevs.springsecurityjwt.dtos.auth.DtoRegistro;
import cl.javadevs.springsecurityjwt.dtos.auth.LoginResponseDto;
import cl.javadevs.springsecurityjwt.dtos.auth.ResetPasswordDto;
import cl.javadevs.springsecurityjwt.exceptions.*;
import cl.javadevs.springsecurityjwt.mappers.UsuarioMapper;
import cl.javadevs.springsecurityjwt.models.Rol;
import cl.javadevs.springsecurityjwt.models.Usuario;
import cl.javadevs.springsecurityjwt.repositories.IRolesRepository;
import cl.javadevs.springsecurityjwt.repositories.IUsuariosRepository;
import cl.javadevs.springsecurityjwt.security.jwt.JwtGenerador;
import cl.javadevs.springsecurityjwt.util.MensajeError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    private PasswordEncoder passwordEncoder;
    private IRolesRepository rolesRepository;
    private IUsuariosRepository usuariosRepository;
    private AuthenticationManager authenticationManager;
    private JwtGenerador jwtGenerador;

    @Autowired
    public AuthService(IUsuariosRepository usuariosRepository,IRolesRepository rolesRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtGenerador jwtGenerador){
        this.usuariosRepository = usuariosRepository;
        this.rolesRepository = rolesRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtGenerador = jwtGenerador;
    }

    private Usuario prepararUsuario(DtoRegistro dtoRegistro, String rolNombre){
        // Verificar si el usuario ya existe
        if (usuariosRepository.existsByUsername(dtoRegistro.getUsername())){
            throw new UsuarioExistenteException(MensajeError.USUARIO_EXISTENTE);
        }
        if (usuariosRepository.existsByEmail(dtoRegistro.getEmail()))
            throw new UsuarioExistenteException(MensajeError.CORREO_EXISTENTE);

        // Convertir el DTO en una entidad Usuario usando el mapper
        Usuario usuario = UsuarioMapper.toEntity(dtoRegistro);
        usuario.setPassword(passwordEncoder.encode(dtoRegistro.getPassword()));

        //Generalizamos el rol como tal
        Rol rol = rolesRepository.findByName(rolNombre).orElseThrow(() ->
                new RolNoEncontradoException(String.format(MensajeError.ROL_NO_ENCONTRADO,rolNombre)));

        usuario.setRoles(Collections.singletonList(rol));

        return usuario;
    }
    public void registrarUsuario(DtoRegistro dtoRegistro){
        logger.info("Intentado registrar usuario: {}", dtoRegistro.getUsername());
        Usuario usuario = prepararUsuario(dtoRegistro,"USER");
        //Guardamos el usuario
        usuariosRepository.save(usuario);
        logger.info("Usuario registrado exitosamente: {}",dtoRegistro.getUsername());
    }

    public void registrarAdministrador(DtoRegistro dtoRegistro){
        logger.info("Intentado registrar usuario: {}", dtoRegistro.getUsername());
        Usuario usuario = prepararUsuario(dtoRegistro, "ADMIN");
        //Guardamos el usuario
        usuariosRepository.save(usuario);
        logger.info("Usuario registrado exitosamente: {}",dtoRegistro.getUsername());
    }

    public LoginResponseDto login(DtoLogin dtoLogin){
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dtoLogin.getUsername(), dtoLogin.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            logger.info("Generando el token");
            String token = jwtGenerador.generarToken(authentication);

            logger.info("Obteniendo el rol del usuario autenticado");
            String rol = authentication.getAuthorities().stream()
                    .findFirst()
                    .map(grantedAuthority -> grantedAuthority.getAuthority())
                    .orElse("UNKNOW");

            logger.info("Retornando la respuesta con el token");
            return new LoginResponseDto(token, rol);
        } catch (Exception e) {
            throw new CredencialesInvalidasException(MensajeError.CREDENCIALES_INVALIDAS);
        }
    }

    public void resetPassword(ResetPasswordDto resetPasswordDto){
        //validamos que el token no sea nulo
        if (resetPasswordDto.getTokenPassword() == null || resetPasswordDto.getTokenPassword().isEmpty()){
            throw new TokenInvalidoOExpiradoException(MensajeError.TOKEN_VACIO);
        }

        //validamos coincidencia de campos enviados
        if (!resetPasswordDto.getNewPassword().equals(resetPasswordDto.getConfirmPassword()))
            throw new CredencialesInvalidasException(MensajeError.PASSWORDS_NO_COINCIDEN);

        //Buscamos al usuario por el método creado, o sea por token
        Usuario usuario = usuariosRepository.findByTokenPassword(resetPasswordDto.getTokenPassword())
                .orElseThrow(() -> new TokenInvalidoOExpiradoException(MensajeError.TOKEN_INVALIDO));

        //Validar si el token ha expirado
        if (usuario.getLastTokenRequest() != null && usuario.getLastTokenRequest().isBefore(LocalDateTime.now().minusHours(1))) {
            throw new TokenInvalidoOExpiradoException(MensajeError.TOKEN_EXPIRADO);
        }

        //Actualizamos la contraseña
        usuario.setPassword(passwordEncoder.encode(resetPasswordDto.getNewPassword()));
        usuario.setTokenPassword(null);
        usuario.setLastTokenRequest(null);
        usuariosRepository.save(usuario);
    }

}
