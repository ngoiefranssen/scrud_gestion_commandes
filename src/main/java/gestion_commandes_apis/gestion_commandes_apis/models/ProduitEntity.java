package gestion_commandes_apis.gestion_commandes_apis.models;

import java.math.BigDecimal;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
@Table(name = "produits")
public class ProduitEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer produitId;

    @NotBlank(message = "Le nom du produit ne peut pas être vide")
    private String nomProduit;

    @NotBlank(message = "La description du produit ne peut pas être vide")
    private String descriptionProduit;

    @NotNull(message = "Le prix unitaire est obligatoire")
    @DecimalMin(value = "0.01", message = "Le prix unitaire doit être supérieur à 0")
    private BigDecimal prixUnitaire;

    @NotNull(message = "La quantité ne peut pas être vide")
    @Min(value = 0, message = "La quantité ne peut pas être négative")
    private Integer quantiteStock;
}
