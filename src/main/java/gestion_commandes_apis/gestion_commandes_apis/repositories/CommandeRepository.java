package gestion_commandes_apis.gestion_commandes_apis.repositories;

import gestion_commandes_apis.gestion_commandes_apis.models.CommandeEntity;

import java.util.Optional;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommandeRepository extends JpaRepository<CommandeEntity, Integer> {

        /**
         * Requête pour rechercher des commandes avec des critères optionnels
         * clientId, statutId, nomClient et emailClient peuvent être null
         * Si un critère est null, il n'est pas pris en compte dans la recherche
         * Utilisation de LOWER pour une recherche insensible à la casse
         * 
         * @param clientId
         * @param statutId
         * @param nomClient
         * @param emailClient
         * @param pageable
         * @return
         */
        @Query("""
                                SELECT c FROM CommandeEntity c
                                WHERE (:clientId IS NULL OR c.client.clientId = :clientId)
                                OR (:statutId IS NULL OR c.statut.id = :statutId)
                                OR (:nomClient IS NULL OR LOWER(c.client.nomClient) LIKE LOWER(CONCAT('%', :nomClient, '%')))
                                OR (:emailClient IS NULL OR LOWER(c.client.emailClient) LIKE LOWER(CONCAT('%', :emailClient, '%')))
                        """)

        /*
         * Requête pour rechercher des commandes avec des critères optionnels
         * clientId, statutId, nomClient et emailClient peuvent être null
         * Si un critère est null, il n'est pas pris en compte dans la recherche
         * * Utilisation de LOWER pour une recherche insensible à la casse
         */

        Page<CommandeEntity> searchCommandes(
                        @Param("clientId") Integer clientId,
                        @Param("statutId") Byte statutId,
                        @Param("nomClient") String nomClient,
                        @Param("emailClient") String emailClient,
                        Pageable pageable);

        Optional<CommandeEntity> findByCommandeId(Integer commandeId);
}
