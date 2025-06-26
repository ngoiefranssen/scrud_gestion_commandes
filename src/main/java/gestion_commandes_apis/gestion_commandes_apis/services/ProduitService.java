package gestion_commandes_apis.gestion_commandes_apis.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import gestion_commandes_apis.gestion_commandes_apis.dtos.update.UpdateProduitRequestDTO;
import gestion_commandes_apis.gestion_commandes_apis.models.ProduitEntity;
import gestion_commandes_apis.gestion_commandes_apis.repositories.ProduitRepository;

@Service
public class ProduitService {
    @Autowired
    private ProduitRepository produitRepository;

    // Méthode pour trouver tous les produits
    public List<ProduitEntity> getAllProduitEntities() {
        return produitRepository.findAll();
    }

    // Méthode pour obtenir tous les clients avec pagination et recherche
    public Page<ProduitEntity> getAllProduitEntities(String search, Pageable pageable) {
        if (search != null && !search.isEmpty()) {
            return produitRepository.findByNomProduitContainingIgnoreCaseOrDescriptionProduitContainingIgnoreCase(
                    search, search,
                    pageable);
        } else {
            return produitRepository.findAll(pageable);
        }
    }

    // Méthode pour trouver un produit par son ID
    public Optional<ProduitEntity> getByIdProduitEntity(Integer produitId) {
        return produitRepository.findById(produitId);
    }

    // Méthode pour ajouter un produit
    public ProduitEntity saveProduitEntity(ProduitEntity produit) {
        return produitRepository.save(produit);
    }

    // Méthode pour mettre à jour un produit
    public ProduitEntity updateProduitEntity(Integer id, UpdateProduitRequestDTO dto) {
        ProduitEntity existingProduit = produitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produit introuvable"));

        if (dto.getNomProduit() != null)
            existingProduit.setNomProduit(dto.getNomProduit());

        if (dto.getDescriptionProduit() != null)
            existingProduit.setDescriptionProduit(dto.getDescriptionProduit());

        if (dto.getPrixUnitaire() != null)
            existingProduit.setPrixUnitaire(new java.math.BigDecimal(dto.getPrixUnitaire()));

        if (dto.getQuantiteStock() != null)
            existingProduit.setQuantiteStock(Integer.parseInt(dto.getQuantiteStock()));

        return produitRepository.save(existingProduit);
    }

    // Méthode pour supprimer un produit
    public void deleteProduitEntity(Integer produitId) {

        // Vérification de l'existence du produit avant la suppression
        if (!produitRepository.existsById(produitId))
            throw new RuntimeException("Produit introuvable");

        produitRepository.deleteById(produitId);
    }
}
