package gestion_commandes_apis.gestion_commandes_apis.dtos.update;

public class UpdateClientRequestDTO {
    private String nomClient;
    private String emailClient;
    private String adresseClient;
    private String telephoneClient;

    // Constructeur par défaut
    public UpdateClientRequestDTO() {
    }

    // Getters
    public String getNomClient() {
        return nomClient;
    }

    public String getEmailClient() {
        return emailClient;
    }

    public String getAdresseClient() {
        return adresseClient;
    }

    public String getTelephoneClient() {
        return telephoneClient;
    }

    // Setters
    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }

    public void setEmailClient(String emailClient) {
        this.emailClient = emailClient;
    }

    public void setAdresseClient(String adresseClient) {
        this.adresseClient = adresseClient;
    }

    public void setTelephoneClient(String telephoneClient) {
        this.telephoneClient = telephoneClient;
    }

    @Override
    public String toString() {
        return "UpdateClientDTO{" +
                "nomClient='" + nomClient + '\'' +
                ", emailClient='" + emailClient + '\'' +
                ", adresseClient='" + adresseClient + '\'' +
                ", telephoneClient='" + telephoneClient + '\'' +
                '}';
    }
}
