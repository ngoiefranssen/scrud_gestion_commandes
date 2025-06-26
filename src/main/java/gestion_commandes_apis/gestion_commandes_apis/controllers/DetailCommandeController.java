package gestion_commandes_apis.gestion_commandes_apis.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import gestion_commandes_apis.gestion_commandes_apis.dtos.gets.DetailCommandeWithTotalResponseDTO;
import gestion_commandes_apis.gestion_commandes_apis.services.DetailCommadeService;

@RestController
@RequestMapping("/api/detail-commandes")
public class DetailCommandeController {
    @Autowired
    private DetailCommadeService commandeService;

    @GetMapping("/{commandeId}/details")
    public ResponseEntity<?> getDetailsCommande(@PathVariable Integer commandeId) {
        try {
            DetailCommandeWithTotalResponseDTO response = commandeService.getDetailsByCommandeId(commandeId);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

}
