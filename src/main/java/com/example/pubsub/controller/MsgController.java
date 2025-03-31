package com.example.pubsub.controller;

import com.example.pubsub.domain.Msg;
import com.example.pubsub.service.MsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/msgs")
public class MsgController {

    private final MsgService msgService;

    @Autowired
    public MsgController(MsgService msgService) {
        this.msgService = msgService;
    }

    @GetMapping
    public ResponseEntity<List<Msg>> getAllMessages() {
        List<Msg> messages = msgService.findAll();
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<Msg> getMessageById(@PathVariable String uuid) {
        return msgService.findById(uuid)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/client/{externalId}")  // Mudança no mapeamento para clarificar
    public ResponseEntity<List<Msg>> getMessagesByClientId(@PathVariable Long externalId) {
        List<Msg> messages = msgService.findByClientId(externalId);
        return !messages.isEmpty() ?
                ResponseEntity.ok(messages) :
                ResponseEntity.noContent().build();
    }

    @GetMapping("/transaction/{transactionId}")
    public ResponseEntity<List<Msg>> getMessagesByTransactionId(@PathVariable String transactionId) {
        List<Msg> messages = msgService.findByTransactionId(transactionId);
        return ResponseEntity.ok(messages);
    }

    @PostMapping
    public ResponseEntity<Msg> createMessage(@RequestBody Msg msg) {
        Msg createdMsg = msgService.save(msg);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMsg);
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<Msg> updateMessage(@PathVariable String uuid, @RequestBody Msg msg) {
        if (!msgService.findById(uuid).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        msg.setUuid(uuid);
        Msg updatedMsg = msgService.update(msg);
        return ResponseEntity.ok(updatedMsg);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deleteMessage(@PathVariable String uuid) {
        if (!msgService.findById(uuid).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        msgService.deleteById(uuid);
        return ResponseEntity.noContent().build();
    }
}