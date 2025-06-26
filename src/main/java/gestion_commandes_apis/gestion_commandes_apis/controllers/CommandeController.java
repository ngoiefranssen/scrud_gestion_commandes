package gestion_commandes_apis.gestion_commandes_apis.controllers;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import gestion_commandes_apis.gestion_commandes_apis.dtos.create.CreateCommandeRequestDTO;
import gestion_commandes_apis.gestion_commandes_apis.dtos.create.ProduitCommandeRequestDTO;
import gestion_commandes_apis.gestion_commandes_apis.models.CommandeEntity;
import gestion_commandes_apis.gestion_commandes_apis.repositories.CommandeRepository;
import gestion_commandes_apis.gestion_commandes_apis.repositories.DetailCommandeRepository;
import gestion_commandes_apis.gestion_commandes_apis.services.CommandeService;

@RestController
@RequestMapping("/api/commandes")
public class CommandeController {

    @Autowired
    private CommandeService commandeService;
    @Autowired
    private DetailCommandeRepository detailCommandeRepository;
    @Autowired
    private CommandeRepository commandeRepository;

    /**
     * Endpoint pour récupérer toutes les commandes
     * GET /api/commandes
     * Retourne une liste de toutes les commandes
     * 
     * @return
     */

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllCommandes(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long clientId,
            @RequestParam(required = false) Long statutId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage,
            @RequestParam(defaultValue = "dateCommande") String sortField,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Map<String, Object> response = new HashMap<>();

        try {

            // Gestion du tri
            Sort.Direction sortDirection = sortDir.equalsIgnoreCase("desc")
                    ? Sort.Direction.DESC
                    : Sort.Direction.ASC;

            Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(sortDirection, sortField));

            // Récupération paginée avec recherche optionnelle
            Page<CommandeEntity> pageCommande = commandeService.getAllCommandes(
                    clientId != null ? clientId.intValue() : null,
                    statutId != null ? statutId.byteValue() : null,
                    search,
                    pageable);

            List<CommandeEntity> commandes = pageCommande.getContent();

            // Préparer les métadonnées de pagination
            Map<String, Object> paginationData = new HashMap<>();
            paginationData.put("statusCode", 200);
            paginationData.put("next", pageCommande.hasNext() ? page + 1 : null);
            paginationData.put("total", pageCommande.getTotalElements());
            paginationData.put("perPage", perPage);
            paginationData.put("last", pageCommande.isLast());
            paginationData.put("previous", pageCommande.hasPrevious() ? page - 1 : null);
            paginationData.put("from", (page - 1) * perPage + 1);
            paginationData.put("to", ((page - 1) * perPage) + commandes.size());
            paginationData.put("page", page);
            paginationData.put("first", pageCommande.isFirst());
            paginationData.put("items", commandes); // On inclut les commandes ici

            // Construire la réponse finale
            response.put("success", true);
            response.put("message", "Éléments trouvés avec succès");
            response.put("data", paginationData);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Erreur interne du serveur");
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Endpoint pour récupérer une commande par son ID
     * GET /api/commandes/{id}
     * Retourne une commande spécifique par son ID
     * En cas d'utilisation de @RequestParam, l’URL soit :
     * /api/commandes?commandeId=1
     * 
     * @param commandeId
     * @return
     */

    @GetMapping("/{commandeId}")
    public ResponseEntity<Map<String, Object>> getCommandeById(@PathVariable Integer commandeId) {
        // Map pour stocker la réponse
        Map<String, Object> response = new HashMap<>();

        try {
            // Vérification de l'ID de la commande
            CommandeEntity commande = commandeService.getByIdCommande(commandeId);
            // Si la commande est trouvée, on la retourne
            if (commande != null) {
                response.put("success", true);
                response.put("message", "Commande trouvée avec succès");
                response.put("data", commande);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Commande non trouvée");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Erreur interne du serveur");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Endpoint pour ajouter une nouvelle commande
     * POST /api/commandes
     * Prend un objet CommandeEntity dans le corps de la requête et le sauvegarde
     * 
     * @param commande
     * @return
     */

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> enregistrerCommande(
            @RequestParam("clientId") Integer clientId,
            @RequestParam("statutId") Byte statutId,
            @RequestParam("produits") String produitsJson
    // @RequestParam("fichier") MultipartFile fichier
    ) {
        try {
            // Convertir le JSON en liste de produits
            ObjectMapper objectMapper = new ObjectMapper();
            List<ProduitCommandeRequestDTO> produits = objectMapper.readValue(
                    produitsJson, new TypeReference<>() {
                    });

            // Créer le DTO complet
            CreateCommandeRequestDTO dto = new CreateCommandeRequestDTO();
            dto.setClientId(clientId);
            dto.setStatutId(statutId);
            dto.setProduits(produits);

            String message = commandeService.saveCommande(dto);

            if (message.startsWith("Échec")) {
                return ResponseEntity.badRequest().body(Map.of("message", message));
            }

            return ResponseEntity.ok(Map.of("message", message));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Endpoint pour le mise à jour d'une commande
     * PUT /api/commandes/{id}
     * 
     * @param commandeId
     * @return
     */
    @PutMapping(value = "/{commandeId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> modifierCommande(
            @PathVariable Integer commandeId,
            @RequestParam("clientId") Integer clientId,
            @RequestParam("statutId") Byte statutId,
            @RequestParam("produits") String produitsJson) {
        try {
            // Vérifier l'existence de la commande
            commandeRepository.findByCommandeId(commandeId)
                    .orElseThrow(() -> new RuntimeException("Commande introuvable"));

            ObjectMapper objectMapper = new ObjectMapper();

            List<ProduitCommandeRequestDTO> produits = objectMapper.readValue(
                    produitsJson, new TypeReference<>() {
                    });

            CreateCommandeRequestDTO dto = new CreateCommandeRequestDTO();
            dto.setClientId(clientId);
            dto.setStatutId(statutId);
            dto.setProduits(produits);

            String message = commandeService.updateCommande(commandeId, dto);

            if (message.startsWith("Échec")) {
                return ResponseEntity.badRequest().body(Map.of("message", message));
            }

            return ResponseEntity.ok(Map.of("message", message));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Endpoint pour supprimer une commande par son ID
     * DELETE /api/commandes/{id}
     * Supprime une commande spécifique par son ID
     * 
     * @param commandeId
     * @return
     */
    @DeleteMapping("/{commandeId}")
    public ResponseEntity<Map<String, Object>> deleteCommande(@PathVariable Integer commandeId) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Vérifier l'existence de la commande
            commandeRepository.findByCommandeId(commandeId)
                    .orElseThrow(() -> new RuntimeException("Commande introuvable"));

            // Supprimer les détails de la commande
            detailCommandeRepository.deleteByCommandeId(commandeId);

            // Supprimer la commande elle-même
            commandeService.deleteCommande(commandeId);

            response.put("success", true);
            response.put("message", "Commande supprimée avec succès");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Erreur interne du serveur");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
