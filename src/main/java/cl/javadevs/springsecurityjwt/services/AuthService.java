package cl.javadevs.springsecurityjwt.services;

import cl.javadevs.springsecurityjwt.dtos.auth.request.DtoLogin;
import cl.javadevs.springsecurityjwt.dtos.auth.request.DtoRefreshToken;
import cl.javadevs.springsecurityjwt.dtos.auth.request.DtoRegistro;
import cl.javadevs.springsecurityjwt.dtos.auth.response.DtoLoginResponse;
import cl.javadevs.springsecurityjwt.dtos.auth.request.DtoResetPassword;
import cl.javadevs.springsecurityjwt.exceptions.*;
import cl.javadevs.springsecurityjwt.mappers.UsuarioMapper;
import cl.javadevs.springsecurityjwt.models.Rol;
import cl.javadevs.springsecurityjwt.models.Usuario;
import cl.javadevs.springsecurityjwt.repositories.IRolesRepository;
import cl.javadevs.springsecurityjwt.repositories.IUsuariosRepository;
import cl.javadevs.springsecurityjwt.security.jwt.JwtGenerador;
import cl.javadevs.springsecurityjwt.security.util.ConstantesSeguridad;
import cl.javadevs.springsecurityjwt.util.MensajeError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.UUID;

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

    public String generarRefreshToken(DtoLogin dtoLogin) {
        logger.info("Generando el refresh token");
        String refreshToken = UUID.randomUUID().toString();
        LocalDateTime refreshTokenExpiryDate = Instant.now()
                .plusMillis(ConstantesSeguridad
                        .JWT_REFRESH_EXPIRATION).atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        logger.info("Guardando el refresh token en la bd");
        Usuario usuario = usuariosRepository.findByUsername(dtoLogin.getUsername())
                .orElseThrow(()-> new CredencialesInvalidasException(MensajeError.CREDENCIALES_INVALIDAS));
        usuario.setRefreshToken(hashToken(refreshToken)); //hasheamos el refresh token
        usuario.setRefreshTokenExpiryDate(refreshTokenExpiryDate);
        usuariosRepository.save(usuario);
        logger.info("Retornando el refresh token.");
        return refreshToken;
    }

    public DtoLoginResponse login(DtoLogin dtoLogin){
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dtoLogin.getUsername(), dtoLogin.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            logger.info("Generando el token.");
            String token = jwtGenerador.generarToken(authentication);

            logger.info("Generando el refresh token.");
            logger.info("Guardando el refresh token en la bd.");
            String refreshToken = generarRefreshToken(dtoLogin);

            logger.info("Retornando la respuesta con el token, login exitoso.");
            return new DtoLoginResponse(token,refreshToken);
        } catch (Exception e) {
            throw new CredencialesInvalidasException(MensajeError.CREDENCIALES_INVALIDAS);
        }
    }

    public void resetPassword(DtoResetPassword dtoResetPassword){
        //validamos que el token no sea nulo
        if (dtoResetPassword.getTokenPassword() == null || dtoResetPassword.getTokenPassword().isEmpty()){
            throw new TokenInvalidoOExpiradoException(MensajeError.TOKEN_VACIO);
        }

        //validamos coincidencia de campos enviados
        if (!dtoResetPassword.getNewPassword().equals(dtoResetPassword.getConfirmPassword()))
            throw new CredencialesInvalidasException(MensajeError.PASSWORDS_NO_COINCIDEN);

        //Buscamos al usuario por el método creado, o sea por token
        Usuario usuario = usuariosRepository.findByTokenPassword(dtoResetPassword.getTokenPassword())
                .orElseThrow(() -> new TokenInvalidoOExpiradoException(MensajeError.TOKEN_INVALIDO));

        //Validar si el token ha expirado
        if (usuario.getLastTokenRequest() != null && usuario.getLastTokenRequest().isBefore(LocalDateTime.now().minusHours(1))) {
            throw new TokenInvalidoOExpiradoException(MensajeError.TOKEN_EXPIRADO);
        }

        //Actualizamos la contraseña
        usuario.setPassword(passwordEncoder.encode(dtoResetPassword.getNewPassword()));
        usuario.setTokenPassword(null);
        usuario.setLastTokenRequest(null);
        usuariosRepository.save(usuario);
        logger.info("La contraseña fue restablecida exitosamente.");
    }

    public String hashToken(String refreshToken) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodeHash = digest.digest(refreshToken.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : encodeHash){
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw  new RuntimeException("Error al hashear el token.",e);
        }
    }

    /*public String renovarToken(DtoRefreshToken dtoRefreshToken) {
        logger.info("Extraemos el refresh token del request.");
        String refreshToken = dtoRefreshToken.getRefreshToken();

        Usuario usuario = usuariosRepository.findByRefreshToken(hashToken(refreshToken))
                .orElseThrow(() -> new TokenInvalidoOExpiradoException(MensajeError.TOKEN_INVALIDO));
        logger.info("Hemos guardado el usuario en un objeto buscandolo por refreshToken");
        if (usuario.getRefreshTokenExpiryDate().isBefore(LocalDateTime.now())) {
            logger.info("Verificando la fecha de expiración.");
            throw new TokenInvalidoOExpiradoException(MensajeError.TOKEN_EXPIRADO);
        }
        logger.info("Se verificó que el token sigue vigente");
        try {
            logger.info("Empezamos el try:");
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(usuario.getUsername(), usuario.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            logger.info("Generando el token.");
            String token = jwtGenerador.generarToken(authentication);
            logger.info("El token fue generado exitosamente.");
            return token;
        } catch (Exception e) {
            logger.info("Se generó un error: ");
            throw new CredencialesInvalidasException(MensajeError.CREDENCIALES_INVALIDAS);
        }
    }*/

   /*public String renovarToken(DtoRefreshToken dtoRefreshToken) {
        logger.info("Extraemos el refresh token del request.");
        String refreshToken = dtoRefreshToken.getRefreshToken();

        // Buscar al usuario por el refresh token
        Usuario usuario = usuariosRepository.findByRefreshToken(hashToken(refreshToken))
                .orElseThrow(() -> new TokenInvalidoOExpiradoException(MensajeError.TOKEN_INVALIDO));
        logger.info("Hemos guardado el usuario en un objeto buscandolo por refreshToken");

        // Verificar si el refresh token ha expirado
        if (usuario.getRefreshTokenExpiryDate().isBefore(LocalDateTime.now())) {
            logger.info("Verificando la fecha de expiración.");
            throw new TokenInvalidoOExpiradoException(MensajeError.TOKEN_EXPIRADO);
        }
        logger.info("Se verificó que el token sigue vigente");

        try {
            logger.info("Empezamos el try:");

            // Crear manualmente un objeto de autenticación
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    usuario.getUsername(),
                    null, // No necesitas la contraseña aquí
                    usuario.getRoles().stream().map(rol -> new SimpleGrantedAuthority(rol.getName())).toList()
            );

            // Configurar el contexto de seguridad
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            logger.info("Contexto de seguridad configurado.");

            // Generar el nuevo token JWT
            logger.info("Generando el token.");
            String token = jwtGenerador.generarToken(authenticationToken);
            logger.info("El token fue generado exitosamente.");
            return token;
        } catch (Exception e) {
            logger.error("Se generó un error al renovar el token: ", e);
            throw new CredencialesInvalidasException(MensajeError.CREDENCIALES_INVALIDAS);
        }
    }*/

    public String renovarToken(DtoRefreshToken dtoRefreshToken) {
        logger.info("Extraemos el refresh token del request.");
        String refreshToken = dtoRefreshToken.getRefreshToken();

        // Buscar al usuario por el refresh token
        Usuario usuario = usuariosRepository.findByRefreshToken(hashToken(refreshToken))
                .orElseThrow(
                        () -> new TokenInvalidoOExpiradoException(MensajeError.TOKEN_INVALIDO));
        logger.info("Hemos guardado el usuario en un objeto buscandolo por refreshToken");

        // Verificar si el refresh token ha expirado
        if (usuario.getRefreshTokenExpiryDate().isBefore(LocalDateTime.now())) {
            logger.info("Verificando la fecha de expiración.");
            throw new TokenInvalidoOExpiradoException(MensajeError.TOKEN_EXPIRADO);
        }
        logger.info("Se verificó que el token sigue vigente");

        try {
            logger.info("Empezamos el try:");

            // Crear manualmente un objeto de autenticación
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    usuario.getUsername(),
                    null, // No necesitas la contraseña aquí
                    usuario.getRoles().stream().map(rol -> new SimpleGrantedAuthority(rol.getName())).toList()
            );

            // Generar el nuevo token JWT
            logger.info("Generando el token.");
            String token = jwtGenerador.generarToken(authenticationToken);
            logger.info("El token fue generado exitosamente.");
            return token;
        } catch (Exception e) {
            logger.error("Se generó un error al renovar el token: ", e);
            throw new CredencialesInvalidasException(MensajeError.CREDENCIALES_INVALIDAS);
        }
    }

    public String renovarRefreshToken(String refreshToken) {

        Usuario usuario = usuariosRepository.findByRefreshToken(hashToken(refreshToken))
                .orElseThrow(() -> new TokenInvalidoOExpiradoException(MensajeError.TOKEN_INVALIDO));

        if (usuario.getRefreshTokenExpiryDate().isBefore(LocalDateTime.now())) {
            throw new TokenInvalidoOExpiradoException(MensajeError.TOKEN_EXPIRADO);
        }
        String newRefreshToken = UUID.randomUUID().toString();
        usuario.setRefreshToken(hashToken(newRefreshToken));
        usuario.setRefreshTokenExpiryDate(Instant.now()
                .plusMillis(ConstantesSeguridad
                        .JWT_REFRESH_EXPIRATION).atZone(ZoneId.systemDefault())
                .toLocalDateTime());
        usuariosRepository.save(usuario);
        return newRefreshToken;
    }

    public void logout(String username) {
        Usuario usuario = usuariosRepository.findByUsername(username)
                .orElseThrow(() -> new CredencialesInvalidasException(MensajeError.CREDENCIALES_INVALIDAS));

        // Invalidar el refresh token
        usuario.setRefreshToken(null);
        usuario.setRefreshTokenExpiryDate(null);
        usuariosRepository.save(usuario);

        logger.info("Logout exitoso para el usuario: {}", username);
    }
}
