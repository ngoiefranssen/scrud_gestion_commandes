package gestion_commandes_apis.gestion_commandes_apis.dtos.create;

import jakarta.validation.constraints.*;

public class ProduitCommandeRequestDTO {
    @NotNull(message = "L'identifiant du produit est requis")
    private Integer produitId;
    
    @NotNull(message = "La quantité est requise")
    @Min(value = 1, message = "La quantité doit être au moins égale à 1")
    private Integer quantite;

    // Getters et Setters
    public Integer getProduitId() {
        return produitId;
    }

    public void setProduitId(Integer produitId) {
        this.produitId = produitId;
    }

    public Integer getQuantite() {
        return quantite;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }

    @Override
    public String toString() {
        return "ProduitCommandeDTO{" +
                "produitId=" + produitId +
                ", quantite=" + quantite +
                '}';
    }
}
