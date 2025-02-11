package com.sb3chatgpt.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class BotController {

    @Autowired
    private RestTemplate restTemplate;

    // TODO:récupérer les valeurs des propriétés du fichier application.properties


    // TODO:créer une méthode chat qui prend en paramètre une chaîne de caractères prompt et qui retourne un objet de type BotResponse

}
