package com.legadi.jdk9.features.module_tester.embedded.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.legadi.jdk9.features.module_tester.embedded.model.EmbeddedEntity;

@RestController
@RequestMapping("/entities")
public class EmbeddedCrudController {

    private final Map<UUID, EmbeddedEntity> entities = new HashMap<>();

    @PostMapping()
    public ResponseEntity<String> post(
            @RequestBody EmbeddedEntity entity) {
        UUID correlationId = UUID.randomUUID();

        entities.put(correlationId, entity);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Entity-ID", correlationId.toString());

        return ResponseEntity.status(HttpStatus.CREATED)
            .headers(headers)
            .body("Created");
    }
}
