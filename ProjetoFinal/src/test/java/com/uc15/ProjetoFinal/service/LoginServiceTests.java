package com.uc15.ProjetoFinal.service; 

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.Test;

class LoginServiceTests {
    private LoginService service;
    @BeforeEach
    void setUp(){
        service = new LoginService();
    }

    @Test
    void senhaCerta() {
        assertTrue(service.authenticate("adm", "123"));
    }

    @Test
    void senhaErrada() {
        assertFalse(service.authenticate("adm", "999"));
    }

    @Test
    void loginErrado() {
        assertFalse(service.authenticate("ademir", "123"));
    }
    
    @AfterEach
    void tearDown() {    
        service = null;
    }
}
