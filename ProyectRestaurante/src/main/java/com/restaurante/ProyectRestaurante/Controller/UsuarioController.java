package com.restaurante.ProyectRestaurante.Controller;

import com.restaurante.ProyectRestaurante.Model.Usuario;
import com.restaurante.ProyectRestaurante.Model.Rol;
import com.restaurante.ProyectRestaurante.Repository.UsuarioRepository;
import com.restaurante.ProyectRestaurante.Repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> listar() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        List<Map<String, Object>> response = new ArrayList<>();

        for (Usuario u : usuarios) {
            Map<String, Object> userData = new HashMap<>();
            userData.put("id_usuario", u.getIdUsuario());
            userData.put("nombre_usuario", u.getNombreUsuario());
            userData.put("telefono", u.getTelefono());
            userData.put("email", u.getEmail());
            userData.put("id_rol", u.getIdRol());
            // ELIMINAR ESTA LÍNEA (seguridad):
            // userData.put("clave", u.getClave());

            // JOIN con tabla rol
            Optional<Rol> rol = rolRepository.findById(u.getIdRol());
            userData.put("nombre_rol", rol.isPresent() ? rol.get().getNombre_rol() : null);

            response.add(userData);
        }

        return ResponseEntity.ok(response);
    }

    // Registrar nuevo usuario
    @PostMapping
    public ResponseEntity<?> registrar(@RequestBody Map<String, Object> usuarioData) {
        try {
            Usuario usuario = new Usuario();
            usuario.setNombreUsuario((String) usuarioData.get("nombre_usuario"));
            usuario.setTelefono((String) usuarioData.get("telefono"));
            usuario.setEmail((String) usuarioData.get("email"));

            String clave = (String) usuarioData.get("clave");
            usuario.setClave(passwordEncoder.encode(clave));


            // Convertir id_rol a Integer
            Object idRolObj = usuarioData.get("id_rol");
            Integer idRol;
            if (idRolObj instanceof String) {
                idRol = Integer.parseInt((String) idRolObj);
            } else {
                idRol = (Integer) idRolObj;
            }
            usuario.setIdRol(idRol);

            Usuario saved = usuarioRepository.save(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerPorId(@PathVariable Long id) {
        return usuarioRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizar(@PathVariable Long id, @RequestBody Usuario usuarioActualizado) {
        return usuarioRepository.findById(id).map(usuario -> {
            usuario.setNombreUsuario(usuarioActualizado.getNombreUsuario());
            usuario.setTelefono(usuarioActualizado.getTelefono());
            usuario.setEmail(usuarioActualizado.getEmail());

            // PROBLEMA: Esto encripta UNA CONTRASEÑA YA ENCRIPTADA
            // BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            // usuario.setClave(encoder.encode(usuario.getClave()));

            // SOLO encriptar si viene una contraseña nueva
            if (usuarioActualizado.getClave() != null && !usuarioActualizado.getClave().isEmpty()) {
                // Solo si el usuario envía una nueva contraseña
                if (!usuarioActualizado.getClave().startsWith("$2a$")) {
                    // No está encriptada, entonces encriptar
                    usuario.setClave(passwordEncoder.encode(usuarioActualizado.getClave()));
                }
                // Si ya empieza con $2a$ es porque ya está encriptada, no hacer nada
            }

            usuario.setIdRol(usuarioActualizado.getIdRol());
            return ResponseEntity.ok(usuarioRepository.save(usuario));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {

        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // ---------- LOGIN DE USUARIO ----------
    @PostMapping("/login")
    public ResponseEntity<?> login(
                                   @RequestBody Usuario loginRequest) {
//        String email = credentials.get("email").toLowerCase().trim();
//        String clave = credentials.get("clave").trim(); // nunca está de más

//        System.out.println(" Login attempt - Email: " + email);

        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(loginRequest.getEmail().toLowerCase().trim());
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(401).body("Usuario no encontrado");
        }

        Usuario usuario = usuarioOpt.get();

        if (usuario == null){
            return ResponseEntity.status(401).body("Usuario no encontrado");
        }

//        if (email == null || clave == null) {
//            return ResponseEntity.badRequest().body(Map.of("message", "Faltan campos"));
//        }

// Buscar usuario por email
//        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email.toLowerCase().trim());





//        if (usuarioOpt.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                    .body(Map.of("message", "Credenciales inválidas"));
//        }

//        Usuario usuario = usuarioOpt.get();
//        System.out.println(" Usuario encontrado: " + usuario.getEmail());
//        System.out.println(" Contraseña en BD: " + usuario.getClave().substring(0, 20) + "...");


// Verificar la contraseña usando PasswordEncoder


        System.out.println("Clave ingresada: " + loginRequest.getClave());
        System.out.println("Clave en DB: " + usuario.getClave());
        System.out.println("Coincide?: " + passwordEncoder.matches(loginRequest.getClave(), usuario.getClave()));

        if(!passwordEncoder.matches(loginRequest.getClave(), usuario.getClave())){
            return ResponseEntity.status(401).body("Credenciales invalidas");
        }

//        if (!passwordEncoder.matches(clave, usuario.getClave())) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                    .body(Map.of("success", false, "message","Credenciales inválidas"));
//        }

        System.out.println("✅ Login exitoso");




        // Obtener el rol sin clave
        Optional<Rol> rolOpt = rolRepository.findById(usuario.getIdRol());

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("token", "fake-jwt-token-123456");

        response.put("user", Map.of(
                "id_usuario", usuario.getIdUsuario(),
                "nombre_usuario", usuario.getNombreUsuario(),
                "telefono", usuario.getTelefono(),
                "email", usuario.getEmail(),
                    "id_rol", usuario.getIdRol(),
                "nombre_rol", rolOpt.map(Rol::getNombre_rol).orElse("Desconocido")
        ));

        return ResponseEntity.ok(response);
    }

}