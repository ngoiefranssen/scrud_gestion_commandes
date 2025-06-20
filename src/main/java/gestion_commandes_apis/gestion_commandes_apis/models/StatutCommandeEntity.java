package gestion_commandes_apis.gestion_commandes_apis.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "statut_commandes")
public class StatutCommandeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_statut")
    private Byte id;

    @Column(name = "libelle", nullable = false, unique = true)
    private String libelle;

    @Column(name = "description")
    private String description;
}
