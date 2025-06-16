package gestion_commandes_apis.gestion_commandes_apis.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import gestion_commandes_apis.gestion_commandes_apis.models.ProduitEntity;

public interface ProduitRepository extends JpaRepository<ProduitEntity, Integer> {}
