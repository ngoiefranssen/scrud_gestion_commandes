package gestion_commandes_apis.gestion_commandes_apis.dtos.update;

public class UpdateCommandeRequestDTO {

    private Integer commandeId;
    private String dateCommande;
    private String montantTotal;
    private String montantTotalHt;
    private Integer clientId;
    private Byte statutId;

    // Getters and Setters
    public Integer getCommandeId() {
        return commandeId;
    }

    public void setCommandeId(Integer commandeId) {
        this.commandeId = commandeId;
    }

    public String getDateCommande() {
        return dateCommande;
    }

    public void setDateCommande(String dateCommande) {
        this.dateCommande = dateCommande;
    }

    public String getMontantTotal() {
        return montantTotal;
    }

    public void setMontantTotal(String montantTotal) {
        this.montantTotal = montantTotal;
    }

    public String getMontantTotalHt() {
        return montantTotalHt;
    }

    public void setMontantTotalHt(String montantTotalHt) {
        this.montantTotalHt = montantTotalHt;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public Byte getStatutId() {
        return statutId;
    }

    public void setStatutId(Byte statutId) {
        this.statutId = statutId;
    }

    @Override
    public String toString() {
        return "UpdateCommandeDTO{" +
                "commandeId=" + commandeId +
                ", dateCommande='" + dateCommande + '\'' +
                ", montantTotal='" + montantTotal + '\'' +
                ", montantTotalHt='" + montantTotalHt + '\'' +
                ", clientId=" + clientId +
                ", statutId=" + statutId +
                '}';
    }

}
