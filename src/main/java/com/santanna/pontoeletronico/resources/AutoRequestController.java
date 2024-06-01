package com.santanna.pontoeletronico.resources;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/server")
@RestController
public class AutoRequestController {
    @GetMapping()
    public void getServer() {
    }
}
