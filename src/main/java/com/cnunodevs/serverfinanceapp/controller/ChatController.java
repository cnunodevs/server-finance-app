package com.cnunodevs.serverfinanceapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.flashvayne.chatgpt.service.ChatgptService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/chat")
@RequiredArgsConstructor
public class ChatController {
    
    private final ChatgptService chatgptService;

    @GetMapping
    public ResponseEntity<String> chatWith(@RequestParam String message) {
        chatgptService.sendMessage("Simula ser el asistente financiero de una plataforma para el monitoreo de finanzas personales. Responderás mis dudas de manera de didáctica. Proveerás al menos un ejemplo del concepto o duda. Cuando realice una pregunta que no esté relacionada con temas financieros, responderás: Lo siento, solo estoy habilitado para responderte dudas sobre temáticas de tipo financiero. Después de este mensaje, responderás: Hola, soy tu asistente financiero virtual. ¿Tienes alguna duda?");
        System.out.println(chatgptService.sendMessage(message));
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("answer");
    }

}
