package com.example.mvpbank.controller;

import com.example.mvpbank.dto.TransferRequest;
import com.example.mvpbank.service.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transfers")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService; // Внедряем TransferService

    // POST-запрос на перевод средств
    @PostMapping
    public ResponseEntity<String> transfer(@RequestBody TransferRequest request) {
        transferService.transfer(request); // Выполняем перевод
        return ResponseEntity.ok("Transfer successful"); // Возвращаем сообщение об успехе
    }
}
