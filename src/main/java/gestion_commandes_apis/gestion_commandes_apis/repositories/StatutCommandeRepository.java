package gestion_commandes_apis.gestion_commandes_apis.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gestion_commandes_apis.gestion_commandes_apis.models.StatutCommandeEntity;

@Repository
public interface StatutCommandeRepository extends JpaRepository<StatutCommandeEntity, Integer> {
    // Méthode pour trouver un statut de commande par son ID
    Optional<StatutCommandeEntity> findById(Byte id);
}
