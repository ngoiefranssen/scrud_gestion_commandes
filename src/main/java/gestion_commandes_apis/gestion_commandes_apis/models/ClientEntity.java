package gestion_commandes_apis.gestion_commandes_apis.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class ClientEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer clientId;
    private String nomClient;
    private String emailClient;
    private String adresseClient;
    private String telephoneClient;
}
