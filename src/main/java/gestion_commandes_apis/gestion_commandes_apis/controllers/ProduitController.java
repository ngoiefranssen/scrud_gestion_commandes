package gestion_commandes_apis.gestion_commandes_apis.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gestion_commandes_apis.gestion_commandes_apis.dtos.update.UpdateProduitRequestDTO;
import gestion_commandes_apis.gestion_commandes_apis.models.ProduitEntity;
import gestion_commandes_apis.gestion_commandes_apis.services.ProduitService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/products")

public class ProduitController {
    @Autowired
    private ProduitService produitService;

    /**
     * Endpoint pour récupérer tous les produits
     * GET /api/produits
     * Retourne une liste de tous les produits
     * 
     * @return
     */

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllProduits(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage,
            @RequestParam(defaultValue = "produitId,asc") String[] sort) {

        Map<String, Object> response = new HashMap<>();

        try {
            String sortField = sort[0];
            Sort.Direction sortDirection = sort.length > 1 && sort[1].equalsIgnoreCase("desc")
                    ? Sort.Direction.DESC
                    : Sort.Direction.ASC;

            Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(sortDirection, sortField));

            // Récupération paginée avec recherche optionnelle
            Page<ProduitEntity> pageProduits = produitService.getAllProduitEntities(search, pageable);
            List<ProduitEntity> produits = pageProduits.getContent();

            // Préparer les métadonnées de pagination
            Map<String, Object> paginationData = new HashMap<>();
            paginationData.put("statusCode", 200);
            paginationData.put("next", pageProduits.hasNext() ? page + 1 : null);
            paginationData.put("total", pageProduits.getTotalElements());
            paginationData.put("perPage", perPage);
            paginationData.put("last", pageProduits.isLast());
            paginationData.put("previous", pageProduits.hasPrevious() ? page - 1 : null);
            paginationData.put("from", (page - 1) * perPage + 1);
            paginationData.put("to", ((page - 1) * perPage) + produits.size());
            paginationData.put("page", page);
            paginationData.put("first", pageProduits.isFirst());
            paginationData.put("items", produits); // On inclut les produits ici

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
     * Endpoint pour récupérer un produit par son ID
     * GET /api/products/{produitId}
     * Retourne le produit correspondant à l'ID fourni
     * 
     * @param produitId
     * @return
     */

    @GetMapping("/{produitId}")
    public ResponseEntity<ProduitEntity> getProduitById(@PathVariable Integer produitId) {
        return produitService.getByIdProduitEntity(produitId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * Endpoint pour créer un nouveau produit
     * POST /api/products
     * Prend un objet ProduitEntity dans le corps de la requête et le sauvegarde
     * 
     * @param produit
     * @return
     */

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createProduct(@Valid @ModelAttribute ProduitEntity produit, BindingResult result) {

        if (result.hasErrors()) {
            // Retourne une liste des champs invalides avec leurs messages
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            ProduitEntity savedproduct = produitService.saveProduitEntity(produit);
            return ResponseEntity.ok(savedproduct);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Endpoint pour mettre à jour un produit existant
     * PUT /api/products/{produitId}
     * Prend un objet ProduitEntity dans le corps de la requête et met à jour
     * le produit correspondant à l'ID fourni
     * 
     * @param produitId
     * @param produit
     * @return
     * @throws RuntimeException si le produit n'existe pas
     */

    @PutMapping(value = "/{produitId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateProduct(
            @PathVariable Integer produitId,
            @Valid @ModelAttribute UpdateProduitRequestDTO dto,
            BindingResult result) {

        if (result.hasErrors()) {
            // Retourne une liste des champs invalides avec leurs messages
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            ProduitEntity updatedProduct = produitService.updateProduitEntity(produitId, dto);
            return ResponseEntity.ok(updatedProduct);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * Endpoint pour supprimer un produit
     * DELETE /api/products/{produitId}
     * Supprime le produit correspondant à l'ID fourni
     * 
     * @param produitId
     * @return
     * @throws RuntimeException si le produit n'existe pas
     * 
     */

    @DeleteMapping("/{produitId}")
    public ResponseEntity<Map<String, Object>> deleteProduct(@PathVariable Integer produitId) {

        Map<String, Object> response = new HashMap<>();
        // Vérification de l'existence du produit avant la suppression
        if (!produitService.getByIdProduitEntity(produitId).isPresent()) {
            response.put("success", false);
            response.put("message", "Produit introuvable");
            response.put("statusCode", 404);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        produitService.deleteProduitEntity(produitId);

        try {
            response.put("success", true);
            response.put("message", "Client supprimé avec succès");
            response.put("statusCode", 200);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            errorResponse.put("statusCode", 404);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }
}
