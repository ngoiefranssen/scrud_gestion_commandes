package gestion_commandes_apis.gestion_commandes_apis.models;

import java.math.BigDecimal;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class DetailCommandeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
