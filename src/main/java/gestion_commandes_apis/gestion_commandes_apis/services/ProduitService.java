package gestion_commandes_apis.gestion_commandes_apis.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gestion_commandes_apis.gestion_commandes_apis.dtos.UpdateProduitDTO;
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

    // Méthode pour trouver un produit par son ID
    public ProduitEntity getByIdProduitEntity(Integer produitId) {
        return produitRepository.findById(produitId)
                .orElseThrow(() -> new RuntimeException("Produit introuvable"));
    }

    // Méthode pour ajouter un produit
    public ProduitEntity saveProduitEntity(ProduitEntity produit) {
        return produitRepository.save(produit);
    }

    // Méthode pour mettre à jour un produit
    public ProduitEntity updateProduitEntity(Integer id, UpdateProduitDTO dto) {
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
