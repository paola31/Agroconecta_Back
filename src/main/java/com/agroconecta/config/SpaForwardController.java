package com.agroconecta.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SpaForwardController {

    @GetMapping({
            "/",
            "/nosotros",
            "/proyectos",
            "/contacto",
            "/catalogo",
            "/login",
            "/register",
            "/carrito",
            "/admin",
            "/admin/**"
    })
    public String forwardToFrontend() {
        return "forward:/index.html";
    }
}
