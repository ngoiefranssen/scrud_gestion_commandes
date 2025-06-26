package gestion_commandes_apis.gestion_commandes_apis.dtos.gets;

import java.util.List;

public class DetailCommandeWithTotalResponseDTO {

    private List<DetailCommandeResponseDTO> produits;

    public DetailCommandeWithTotalResponseDTO() {
    }

    public DetailCommandeWithTotalResponseDTO(List<DetailCommandeResponseDTO> produits) {
        this.produits = produits;
    }

    // Getters
    public List<DetailCommandeResponseDTO> getProduits() {
        return produits;
    }

    // Setters
    public void setProduits(List<DetailCommandeResponseDTO> produits) {
        this.produits = produits;
    }
}
