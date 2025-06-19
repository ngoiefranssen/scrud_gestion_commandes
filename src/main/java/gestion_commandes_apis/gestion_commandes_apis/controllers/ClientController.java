package gestion_commandes_apis.gestion_commandes_apis.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import gestion_commandes_apis.gestion_commandes_apis.dtos.UpdateClientDTO;
import gestion_commandes_apis.gestion_commandes_apis.models.ClientEntity;
import gestion_commandes_apis.gestion_commandes_apis.services.ClientService;

@RestController
@RequestMapping("/api/clients")

public class ClientController {

    @Autowired
    private ClientService clientService;

    /**
     * Endpoint pour récupérer tous les clients
     * GET /api/clients
     * Retourne une liste de tous les clients
     * 
     * @return
     */

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllClients(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage,
            @RequestParam(defaultValue = "clientId,asc") String[] sort) {

        Map<String, Object> response = new HashMap<>();

        try {
            // Gestion du tri
            String sortField = sort[0];
            Sort.Direction sortDirection = sort.length > 1 && sort[1].equalsIgnoreCase("desc")
                    ? Sort.Direction.DESC
                    : Sort.Direction.ASC;

            Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(sortDirection, sortField));

            // Récupération paginée avec recherche optionnelle
            Page<ClientEntity> pageClients = clientService.getAllClientEntities(search, pageable);
            List<ClientEntity> clients = pageClients.getContent();

            // Préparer les métadonnées de pagination
            Map<String, Object> paginationData = new HashMap<>();
            paginationData.put("statusCode", 200);
            paginationData.put("next", pageClients.hasNext() ? page + 1 : null);
            paginationData.put("total", pageClients.getTotalElements());
            paginationData.put("perPage", perPage);
            paginationData.put("last", pageClients.isLast());
            paginationData.put("previous", pageClients.hasPrevious() ? page - 1 : null);
            paginationData.put("from", (page - 1) * perPage + 1);
            paginationData.put("to", ((page - 1) * perPage) + clients.size());
            paginationData.put("page", page);
            paginationData.put("first", pageClients.isFirst());
            paginationData.put("items", clients); // On inclut les clients ici

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
     * Endpoint pour récupérer un client par son ID
     * GET /api/clients/{clientId}
     * Retourne le client correspondant à l'ID fourni
     * 
     * @param clientId
     * @return
     */

    @GetMapping("/{clientId}")
    public ResponseEntity<ClientEntity> getClientById(@PathVariable Integer clientId) {
        return clientService.getByIdClientEntity(clientId).map(ResponseEntity::ok)
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + clientId));
    }

    /**
     * Endpoint pour créer un nouveau client
     * POST /api/clients
     * Prend un objet ClientEntity dans le corps de la requête et le sauvegarde
     * 
     * @param client
     * @return
     */

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createClient(@Valid @ModelAttribute ClientEntity client, BindingResult result) {
        if (result.hasErrors()) {
            // Retourne une liste des champs invalides avec leurs messages
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            ClientEntity savedClient = clientService.savClientEntity(client);
            return ResponseEntity.ok(savedClient);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Endpoint pour mettre à jour un client existant
     * PUT /api/clients/{clientId}
     * Prend un objet ClientEntity dans le corps de la requête et met à jour le
     * client correspondant à l'ID fourni
     * 
     * @param clientId
     * @param client
     * @return
     */

    @PutMapping(value = "/{clientId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateClient(
            @PathVariable Integer clientId,
            @ModelAttribute UpdateClientDTO dto) {

        try {
            ClientEntity updatedClient = clientService.updateClientEntity(clientId, dto);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Client mis à jour avec succès");
            response.put("data", updatedClient);
            response.put("statusCode", 200);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            response.put("statusCode", 400);
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Endpoint pour supprimer un client par son ID
     * DELETE /api/clients/{clientId}
     * Supprime le client correspondant à l'ID fourni
     * 
     * @param clientId
     * @return
     */

    @DeleteMapping("/{clientId}")
    public ResponseEntity<Map<String, Object>> deleteClient(@PathVariable Integer clientId) {
        Map<String, Object> response = new HashMap<>();

        Optional<ClientEntity> clientOpt = clientService.getClientById(clientId);

        if (clientOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "Client avec l'ID " + clientId + " introuvable");
            response.put("statusCode", 404);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        clientService.deleteClientEntity(clientId);

        response.put("success", true);
        response.put("message", "Client supprimé avec succès");
        response.put("statusCode", 200);
        return ResponseEntity.ok(response);
    }

}
