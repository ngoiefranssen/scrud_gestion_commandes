package gestion_commandes_apis.gestion_commandes_apis.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import gestion_commandes_apis.gestion_commandes_apis.dtos.create.CreateCommandeRequestDTO;
import gestion_commandes_apis.gestion_commandes_apis.dtos.create.ProduitCommandeRequestDTO;
import gestion_commandes_apis.gestion_commandes_apis.models.ClientEntity;
import gestion_commandes_apis.gestion_commandes_apis.models.CommandeEntity;
import gestion_commandes_apis.gestion_commandes_apis.models.DetailCommandeEntity;
import gestion_commandes_apis.gestion_commandes_apis.models.ProduitEntity;
import gestion_commandes_apis.gestion_commandes_apis.models.StatutCommandeEntity;
import gestion_commandes_apis.gestion_commandes_apis.repositories.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommandeService {
    @Autowired
    private CommandeRepository commandeRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private StatutCommandeRepository statutCommandeRepository;
    @Autowired
    private final ProduitRepository produitRepository;
    @Autowired
    private final DetailCommandeRepository detailCommandeRepository;

    /**
     * Méthode de service pour obtenir toutes les commandes
     * Cette méthode utilise le repository pour récupérer toutes les commandes
     * 
     * @return
     */

    public List<CommandeEntity> getAllCommandes() {
        return commandeRepository.findAll();
    }

    /**
     * Méthode de service pour obtenir toutes les commandes avec pagination et
     * recherche
     * Si un paramètre de recherche est fourni, on utilise la méthode de recherche
     * du repository
     * sinon, on retourne toutes les commandes avec la pagination
     * 
     * @param clientId
     * @param idStatut
     * @param search
     * @param pageable
     * @return
     */

    public Page<CommandeEntity> getAllCommandes(Integer clientId, Byte statutId, String search, Pageable pageable) {

        if (search != null && !search.isEmpty()) {
            return commandeRepository.searchCommandes(clientId, statutId, search, search, pageable);
        } else {
            return commandeRepository.findAll(pageable);
        }
    }

    // Méthode pour trouver une commande par son ID
    public CommandeEntity getByIdCommande(Integer commandeId) {
        return commandeRepository.findById(commandeId)
                .orElseThrow(() -> new RuntimeException("Commande introuvable"));
    }

    /**
     * 
     * @param dto
     * @return
     */

    @Transactional
    public String saveCommande(CreateCommandeRequestDTO dto) {

        //
        BigDecimal montantTotal = BigDecimal.ZERO;
        BigDecimal montantTotalHt = BigDecimal.ZERO;

        //
        List<String> erreurs = new ArrayList<>();

        // Vérification des produits
        for (ProduitCommandeRequestDTO produitDTO : dto.getProduits()) {
            ProduitEntity produit = produitRepository.findById(produitDTO.getProduitId())
                    .orElseThrow(
                            () -> new RuntimeException("Produit ID " + produitDTO.getProduitId() + " introuvable"));

            if (produitDTO.getQuantite() > produit.getQuantiteStock()) {
                erreurs.add("Produit '" + produit.getNomProduit() + "' : stock insuffisant (disponible : "
                        + produit.getQuantiteStock() + ")");
            }
        }

        if (!erreurs.isEmpty()) {
            return "Échec :\n" + String.join("\n", erreurs);
        }

        // Récupération des entités liées
        ClientEntity client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new RuntimeException("Client ID invalide"));

        StatutCommandeEntity statut = statutCommandeRepository.findById(dto.getStatutId())
                .orElseThrow(() -> new RuntimeException("Statut ID invalide"));

        // Création de la commande (on calcule d'abord les montants)
        CommandeEntity commande = new CommandeEntity();
        commande.setClient(client);
        commande.setStatut(statut);
        commande.setDateCommande(LocalDateTime.now());

        // Calcul des montants à partir des produits
        for (ProduitCommandeRequestDTO produitDTO : dto.getProduits()) {
            ProduitEntity produit = produitRepository.findById(produitDTO.getProduitId()).get();

            BigDecimal prixUnitaire = produit.getPrixUnitaire();
            BigDecimal quantite = BigDecimal.valueOf(produitDTO.getQuantite()); // Utilisation de BigDecimal pour les
                                                                                // calculs

            // Sous-total TTC (TVA incluse)
            BigDecimal sousTotal = prixUnitaire.multiply(quantite);

            // Montant HT (hors taxes)
            BigDecimal ht = sousTotal.divide(BigDecimal.valueOf(1.18), 2, RoundingMode.HALF_UP);

            montantTotal = montantTotal.add(sousTotal);
            montantTotalHt = montantTotalHt.add(ht);
        }

        commande.setMontantTotal(montantTotal);
        commande.setMontantTotalHt(montantTotalHt);

        // Maintenant on peut sauvegarder la commande
        commande = commandeRepository.save(commande);

        // Enregistrement des détails + mise à jour du stock
        for (ProduitCommandeRequestDTO produitDTO : dto.getProduits()) {
            ProduitEntity produit = produitRepository.findById(produitDTO.getProduitId()).get();

            DetailCommandeEntity detail = new DetailCommandeEntity();
            detail.setCommande(commande);
            detail.setProduit(produit);
            detail.setQuantite(produitDTO.getQuantite());
            detail.setPrixUnitaireCommande(produit.getPrixUnitaire());

            detailCommandeRepository.save(detail);

            // Mise à jour du stock
            produit.setQuantiteStock(produit.getQuantiteStock() - produitDTO.getQuantite());
            produitRepository.save(produit);
        }

        return "Commande enregistrée avec succès.";
    }

    /**
     * Méthode pour mettre à jour une commande
     * Cette méthode prend un ID de commande et un DTO de mise à jour,
     * récupère la commande existante, met à jour les champs nécessaires,
     * et enregistre la commande mise à jour dans le repository.
     * 
     * @param id
     * @param dto
     * @return
     */

    @Transactional
    public String updateCommande(Integer commandeId, CreateCommandeRequestDTO dto) {
        CommandeEntity commande = commandeRepository.findById(commandeId)
                .orElseThrow(() -> new RuntimeException("Commande introuvable"));

        List<String> erreurs = new ArrayList<>();
        BigDecimal montantTotal = BigDecimal.ZERO;
        BigDecimal montantTotalHt = BigDecimal.ZERO;

        // Récupération des détails actuels
        List<DetailCommandeEntity> anciensDetails = detailCommandeRepository.findByCommandeId(commandeId);

        // Recalcul du stock : on restitue les anciens stocks
        for (DetailCommandeEntity ancien : anciensDetails) {
            ProduitEntity produit = ancien.getProduit();
            produit.setQuantiteStock(produit.getQuantiteStock() + ancien.getQuantite());
            produitRepository.save(produit);
        }

        // Suppression des anciens détails
        detailCommandeRepository.deleteByCommandeId(commandeId);

        // Vérification des nouveaux produits
        for (ProduitCommandeRequestDTO produitDTO : dto.getProduits()) {
            ProduitEntity produit = produitRepository.findById(produitDTO.getProduitId())
                    .orElseThrow(
                            () -> new RuntimeException("Produit ID " + produitDTO.getProduitId() + " introuvable"));

            if (produitDTO.getQuantite() > produit.getQuantiteStock()) {
                erreurs.add("Produit '" + produit.getNomProduit() + "' : stock insuffisant (disponible : "
                        + produit.getQuantiteStock() + ")");
            }
        }

        if (!erreurs.isEmpty()) {
            return "Échec :\n" + String.join("\n", erreurs);
        }

        // Mise à jour des métadonnées de la commande
        ClientEntity client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new RuntimeException("Client ID invalide"));

        StatutCommandeEntity statut = statutCommandeRepository.findById(dto.getStatutId())
                .orElseThrow(() -> new RuntimeException("Statut ID invalide"));

        commande.setClient(client);
        commande.setStatut(statut);
        commande.setDateCommande(LocalDateTime.now());

        // Calcul et enregistrement des nouveaux détails
        for (ProduitCommandeRequestDTO produitDTO : dto.getProduits()) {
            ProduitEntity produit = produitRepository.findById(produitDTO.getProduitId()).get();

            BigDecimal quantite = BigDecimal.valueOf(produitDTO.getQuantite());
            BigDecimal sousTotal = produit.getPrixUnitaire().multiply(quantite);
            BigDecimal ht = sousTotal.divide(BigDecimal.valueOf(1.18), 2, RoundingMode.HALF_UP);

            montantTotal = montantTotal.add(sousTotal);
            montantTotalHt = montantTotalHt.add(ht);

            DetailCommandeEntity detail = new DetailCommandeEntity();
            detail.setCommande(commande);
            detail.setProduit(produit);
            detail.setQuantite(produitDTO.getQuantite());
            detail.setPrixUnitaireCommande(produit.getPrixUnitaire());
            detailCommandeRepository.save(detail);

            // Mise à jour du stock
            produit.setQuantiteStock(produit.getQuantiteStock() - produitDTO.getQuantite());
            produitRepository.save(produit);
        }

        commande.setMontantTotal(montantTotal);
        commande.setMontantTotalHt(montantTotalHt);
        commandeRepository.save(commande);

        return "Commande mise à jour avec succès.";
    }

    // Définition de la méthode pour supprimer une commande
    public void deleteCommande(Integer id) {
        CommandeEntity commande = commandeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commande introuvable"));
        commandeRepository.delete(commande);
    }
}
