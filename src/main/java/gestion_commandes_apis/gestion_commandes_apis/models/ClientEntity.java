package gestion_commandes_apis.gestion_commandes_apis.models;

import jakarta.persistence.*;
import lombok.Data;
import jakarta.validation.constraints.*;

@Entity
@Data
@Table(name = "clients")
public class ClientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer clientId;

    @NotBlank(message = "Le champ nomClient ne peut pas être vide")
    private String nomClient;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "Le champ emailClient ne peut pas être vide")
    @Email(message = "Format d'email invalide")
    private String emailClient;

    @NotBlank(message = "Le champ adresseClient ne peut pas être vide")
    private String adresseClient;

    @NotBlank(message = "Le champ telephoneClient ne peut pas être vide")
    private String telephoneClient;
}
