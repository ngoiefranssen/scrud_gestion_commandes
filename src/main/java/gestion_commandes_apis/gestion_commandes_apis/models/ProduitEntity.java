package gestion_commandes_apis.gestion_commandes_apis.models;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class ProduitEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer produitId;
    private String nomProduit;
    private String descriptionProduit;
    private BigDecimal prixUnitaire;
    private Integer quantiteStock;
}
