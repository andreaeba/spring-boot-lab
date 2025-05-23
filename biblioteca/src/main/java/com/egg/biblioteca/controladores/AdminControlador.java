package com.egg.biblioteca.controladores;

import com.egg.biblioteca.entidades.Usuario;
import com.egg.biblioteca.excepciones.MyException;
import com.egg.biblioteca.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
public class AdminControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @GetMapping("/dashboard")
    public String panelAdministrativo(){
        return "panel.html";
    }

    @GetMapping("/usuarios")
    public String listar(ModelMap modelo){
        List<Usuario> usuarios = usuarioServicio.listarUsuarios();
        modelo.addAttribute("usuarios", usuarios);

        return "usuario_lista.html";
    }

    @GetMapping("/modificarRol/{id}")
    public String cambiarRol(@PathVariable String id){
        usuarioServicio.cambiarRol(id);
        return "redirect:/admin/usuarios";
    }

    @GetMapping("/usuario/{id}")
    public String modificarUsuario(@PathVariable String id, ModelMap modelo){
        Usuario usuario = usuarioServicio.getOne(id);
        modelo.addAttribute("usuario", usuario);

        return "usuario_modificar.html";
    }


    @PostMapping("/modificar/{id}")
    public String modificar(MultipartFile archivo, @PathVariable String id, @RequestParam String nombre, @RequestParam String email,
                            @RequestParam String password, @RequestParam String password2, ModelMap modelo) {

        try{
            usuarioServicio.actualizar(archivo, id, nombre, email, password, password2);
            modelo.put("exito", "El usuario fue actualizado correctamente.");
            return "redirect:/admin/usuarios";
        } catch (MyException ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("email", email);
            return "usuario_modificar.html";
        }
    }


}
