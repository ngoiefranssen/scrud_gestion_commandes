package gestion_commandes_apis.gestion_commandes_apis.models;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class DetailCommandeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer detailCommandeId;

    @ManyToOne
    @JoinColumn(name = "commande_id")
    private CommandeEntity commande;

    @ManyToOne
    @JoinColumn(name = "produit_id")
    private ProduitEntity produit;

    private Integer quantite;
    private BigDecimal prixUnitaireCommande;
}
