package gestion_commandes_apis.gestion_commandes_apis.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import gestion_commandes_apis.gestion_commandes_apis.models.ProduitEntity;

public interface ProduitRepository extends JpaRepository<ProduitEntity, Integer> {
    // Méthode pour rechercher par nom ou email avec pagination
    Page<ProduitEntity> findByNomProduitContainingIgnoreCaseOrDescriptionProduitContainingIgnoreCase(String nomProduit,
            String descriptionProduit, org.springframework.data.domain.Pageable pageable);
}
