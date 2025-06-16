package gestion_commandes_apis.gestion_commandes_apis.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import gestion_commandes_apis.gestion_commandes_apis.models.ClientEntity;
import java.util.Optional;

// C'est une interface, pas une classe !
public interface ClientRepository extends JpaRepository<ClientEntity, Integer> {

    // Méthode pour trouver un client par son email
    Optional<ClientEntity> findByEmailClient(String emailClient);

    // Méthode pour vérifier l'existence d'un client par son email
    boolean existsByEmailClient(String emailClient);

    // Pas besoin de redéclarer deleteById, elle est déjà héritée de JpaRepository
}
