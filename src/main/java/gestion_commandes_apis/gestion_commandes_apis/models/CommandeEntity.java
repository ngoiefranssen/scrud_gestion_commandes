package gestion_commandes_apis.gestion_commandes_apis.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Entity
@Data
@Table(name = "commandes")
public class CommandeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "commande_id")
    private Integer commandeId;

    @Column(name = "date_commande", nullable = false)
    private LocalDateTime dateCommande = LocalDateTime.now();

    @NotNull(message = "Le montant total est obligatoire")
    @DecimalMin(value = "0.01", message = "Le montant total doit être supérieur à 0")
    @Column(name = "montant_total", precision = 38, scale = 2, nullable = false)
    private BigDecimal montantTotal;

    @NotNull(message = "Le montant HT est obligatoire")
    @DecimalMin(value = "0.01", message = "Le montant HT doit être supérieur à 0")
    @Column(name = "montant_totalht", precision = 38, scale = 2, nullable = false)
    private BigDecimal montantTotalHt;

    @ManyToOne(optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private ClientEntity client;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_statut", nullable = false)
    private StatutCommandeEntity statut;
}
