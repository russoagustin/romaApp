package com.russo.roma.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.russo.roma.dto.PeticionRestablecerContraDto;
import com.russo.roma.dto.ResetContrasenaDto;
import com.russo.roma.dto.UsuarioDTO;
import com.russo.roma.model.usuarios.Usuario;
import com.russo.roma.services.interfaces.IUsuarioServices;

@RestController
@RequestMapping("/api/usuarios")
public class UsuariosController {

    private final IUsuarioServices usuarioService;

    public UsuariosController(IUsuarioServices usuarioService){
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<Void> registrarUsuario(@RequestBody Usuario usuario, UriComponentsBuilder ucb){
        Integer id = usuarioService.altaUsuario(usuario);
        URI uri = ucb.path("/api/usuarios/{id}").buildAndExpand(id).toUri();
        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/confirmar-email")
    public ResponseEntity<Void> validarEmail(@RequestParam("token") String token){
        usuarioService.confirmarCuenta(token);
        return ResponseEntity.noContent().build();
    }

    @GetMapping()
    public ResponseEntity<List<UsuarioDTO>> buscarPorNombre(@RequestParam("nombres") String nombres){
        List<UsuarioDTO> lista = usuarioService.buscarUsuarioPorNombre(nombres);
        return ResponseEntity.ok().body(lista);
    }

    @PutMapping("/{id}")
    @PreAuthorize(" isAuthenticated() && #id == principal.getId()")
    public ResponseEntity<Void> modificarUsuario(@RequestBody Usuario usuario, @PathVariable Integer id){
        usuarioService.modificarUsuario(id, usuario);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrarUsuario(@PathVariable Integer id){
        usuarioService.borrarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> buscarPorId(@PathVariable Integer id){
        UsuarioDTO usuarioDto = usuarioService.buscarUsuarioPorId(id);
        return ResponseEntity.ok().body(usuarioDto);
    }

    @PatchMapping("/{id}/mozo")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> hacerMozo(@PathVariable Integer id){
        usuarioService.hacerMozo(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> hacerAdmin(@PathVariable Integer id){
        usuarioService.hacerAdmin(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/sol-restablecer")
    public ResponseEntity<Void> solicitarRestablecerContraseña(@RequestBody PeticionRestablecerContraDto resetSol){
        usuarioService.crearTokenCambioContrasena(resetSol.email());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/restablecer")
    public ResponseEntity<String> resetContrasena(@RequestBody ResetContrasenaDto res){
        usuarioService.restablecerContrasena(res.token(), res.nuevaContrasena());
        return ResponseEntity.ok("Se cambió correctamente la contraseña");
    }

}
