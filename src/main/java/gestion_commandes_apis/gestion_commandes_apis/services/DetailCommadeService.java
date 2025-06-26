package gestion_commandes_apis.gestion_commandes_apis.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gestion_commandes_apis.gestion_commandes_apis.dtos.gets.DetailCommandeResponseDTO;
import gestion_commandes_apis.gestion_commandes_apis.dtos.gets.DetailCommandeWithTotalResponseDTO;
import gestion_commandes_apis.gestion_commandes_apis.models.CommandeEntity;
import gestion_commandes_apis.gestion_commandes_apis.models.DetailCommandeEntity;
import gestion_commandes_apis.gestion_commandes_apis.models.ProduitEntity;
import gestion_commandes_apis.gestion_commandes_apis.repositories.CommandeRepository;
import gestion_commandes_apis.gestion_commandes_apis.repositories.DetailCommandeRepository;

@Service
public class DetailCommadeService {
    @Autowired
    private DetailCommandeRepository detailCommandeRepository;
    @Autowired
    private CommandeRepository commandeRepository;

    /**
     * Récupère les détails d'une commande par son ID.
     * 
     * @param commandeId
     * @return
     */
    public DetailCommandeWithTotalResponseDTO getDetailsByCommandeId(Integer commandeId) {
        commandeRepository.findById(commandeId)
                .orElseThrow(() -> new RuntimeException("Commande introuvable"));

        List<DetailCommandeEntity> details = detailCommandeRepository.findByCommandeId(commandeId);

        List<DetailCommandeResponseDTO> produits = details.stream().map(detail -> {
            ProduitEntity produit = detail.getProduit();
            Integer quantite = detail.getQuantite();
            BigDecimal prixUnitaire = detail.getPrixUnitaireCommande();

            BigDecimal montantTTC = prixUnitaire.multiply(BigDecimal.valueOf(quantite));
            BigDecimal montantHT = montantTTC.divide(BigDecimal.valueOf(1.18), 2, RoundingMode.HALF_UP);

            return new DetailCommandeResponseDTO(
                    produit.getProduitId(),
                    produit.getNomProduit(),
                    quantite,
                    prixUnitaire,
                    montantTTC,
                    montantHT);
        }).collect(Collectors.toList());

        return new DetailCommandeWithTotalResponseDTO(produits);
    }

}
