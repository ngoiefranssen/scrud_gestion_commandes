package gestion_commandes_apis.gestion_commandes_apis.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class ClientModel {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    // client_id, nom_client, email_client, adresse_client, telephone_client
    private Long clientId;
    private String nomClient;
    private String emailClient;
    private String adresseClient;
    private String telephoneClient;
}
