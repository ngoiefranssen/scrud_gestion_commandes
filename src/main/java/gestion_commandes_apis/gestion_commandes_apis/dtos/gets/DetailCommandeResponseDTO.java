package gestion_commandes_apis.gestion_commandes_apis.dtos.gets;

import java.math.BigDecimal;

public class DetailCommandeResponseDTO {
    private Integer produitId;
    private String nomProduit;
    private Integer quantite;
    private BigDecimal prixUnitaireCommande;
    private BigDecimal montantTotalTTC;
    private BigDecimal montantTotalHT;

    public DetailCommandeResponseDTO() {
    }

    // Constructeur
    public DetailCommandeResponseDTO(Integer produitId, String nomProduit, Integer quantite,
            BigDecimal prixUnitaireCommande,
            BigDecimal montantTotalTTC,
            BigDecimal montantTotalHT) {
        //
        this.produitId = produitId;
        this.nomProduit = nomProduit;
        this.quantite = quantite;
        this.prixUnitaireCommande = prixUnitaireCommande;
        this.montantTotalTTC = montantTotalTTC;
        this.montantTotalHT = montantTotalHT;
    }

    // Getters

    public Integer getProduitId() {
        return produitId;
    }

    public String getNomProduit() {
        return nomProduit;
    }

    public Integer getQuantite() {
        return quantite;
    }

    public BigDecimal getPrixUnitaireCommande() {
        return prixUnitaireCommande;
    }

    public BigDecimal getMontantTotalTTC() {
        return montantTotalTTC;
    }

    public BigDecimal getMontantTotalHT() {
        return montantTotalHT;
    }

    // Setters
    public void setProduitId(Integer produitId) {
        this.produitId = produitId;
    }

    public void setNomProduit(String nomProduit) {
        this.nomProduit = nomProduit;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }

    public void setPrixUnitaireCommande(BigDecimal prixUnitaireCommande) {
        this.prixUnitaireCommande = prixUnitaireCommande;
    }

    public void setMontantTotalTTC(BigDecimal montantTotalTTC) {
        this.montantTotalTTC = montantTotalTTC;
    }

    public void setMontantTotalHT(BigDecimal montantTotalHT) {
        this.montantTotalHT = montantTotalHT;
    }
}
