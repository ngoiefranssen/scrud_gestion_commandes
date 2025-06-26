package gestion_commandes_apis.gestion_commandes_apis.dtos.create;

import java.util.List;

public class CreateCommandeRequestDTO {
    private Integer clientId;
    private Byte statutId;
    private List<ProduitCommandeRequestDTO> produits;

    // Getters et Setters
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

    public List<ProduitCommandeRequestDTO> getProduits() {
        return produits;
    }

    public void setProduits(List<ProduitCommandeRequestDTO> produits) {
        this.produits = produits;
    }

    @Override
    public String toString() {
        return "CreateCommandeRequestDTO{" +
                "clientId=" + clientId +
                ", statutId=" + statutId +
                ", produits=" + produits +
                '}';
    }
}
