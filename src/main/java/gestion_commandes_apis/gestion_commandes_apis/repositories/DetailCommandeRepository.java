package gestion_commandes_apis.gestion_commandes_apis.repositories;

import gestion_commandes_apis.gestion_commandes_apis.models.DetailCommandeEntity;
import jakarta.transaction.Transactional;

import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DetailCommandeRepository extends JpaRepository<DetailCommandeEntity, Integer> {

    @Transactional
    @Modifying
    @Query("DELETE FROM DetailCommandeEntity d WHERE d.commande.id = :commandeId")
    void deleteByCommandeId(@Param("commandeId") Integer commandeId);

    @Query("SELECT d FROM DetailCommandeEntity d WHERE d.commande.id = :commandeId")
    List<DetailCommandeEntity> findByCommandeId(@Param("commandeId") Integer commandeId);
}
