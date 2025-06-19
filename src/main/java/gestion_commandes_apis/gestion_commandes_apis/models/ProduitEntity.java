package gestion_commandes_apis.gestion_commandes_apis.models;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Data
public class ProduitEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer produitId;

    @NotBlank( message = "Le nom du produit ne peut pas être vide")
    private String nomProduit;

    @NotBlank(message = "La description du produit ne peut pas être vide")
    private String descriptionProduit;

    @NotBlank(message = "Le prix unitaire ne peut pas être vide")
    private BigDecimal prixUnitaire;

    @NotBlank(message = "La quantite ne peut pas être vide")
    private Integer quantiteStock;
}
