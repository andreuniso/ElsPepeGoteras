package com.elspepegoteras.server.rest;

import com.elspepegoteras.server.database.RiskManagerJDBC;
import com.elspepegoteras.server.interfaces.IRiskManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiManager {
    private static IRiskManager gBD = new RiskManagerJDBC();

    @PostMapping("/login")
    public boolean login(@RequestBody User user) {
        // Aquí pots implementar la lògica de validació
        /*if ("admin".equals(user.getUsername()) && "admin123".equals(user.getPassword())) {
            return "Login exitoso!";
        }
        return "Credencials incorrectes!";*/
        return true;
    }
}
