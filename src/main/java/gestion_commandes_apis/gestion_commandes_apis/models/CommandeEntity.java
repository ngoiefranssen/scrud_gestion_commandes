package gestion_commandes_apis.gestion_commandes_apis.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class CommandeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer commandeId;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private ClientEntity client;

    private LocalDateTime dateCommande = LocalDateTime.now();

    private String statutCommande;
    private BigDecimal montantTotal;
    
}
