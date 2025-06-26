package gestion_commandes_apis.gestion_commandes_apis.dtos.update;

public class UpdateProduitRequestDTO {
    public String nomProduit;
    public String descriptionProduit;
    public String prixUnitaire;
    public String quantiteStock;

    // Constructeur par défaut
    public UpdateProduitRequestDTO() {
    }

    // Getters
    public String getNomProduit() {
        return nomProduit;
    }

    public String getDescriptionProduit() {
        return descriptionProduit;
    }

    public String getPrixUnitaire() {
        return prixUnitaire;
    }

    public String getQuantiteStock() {
        return quantiteStock;
    }

    // Setters
    public void setNomProduit(String nomProduit) {
        this.nomProduit = nomProduit;
    }

    public void setDescriptionProduit(String descriptionProduit) {
        this.descriptionProduit = descriptionProduit;
    }

    public void setPrixUnitaire(String prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public void setQuantiteStock(String quantiteStock) {
        this.quantiteStock = quantiteStock;
    }

    @Override
    public String toString() {
        return "UpdateProduitDTO{" +
                "nomProduit='" + nomProduit + '\'' +
                ", descriptionProduit='" + descriptionProduit + '\'' +
                ", prixUnitaire='" + prixUnitaire + '\'' +
                ", quantiteStock='" + quantiteStock + '\'' +
                '}';
    }
}
