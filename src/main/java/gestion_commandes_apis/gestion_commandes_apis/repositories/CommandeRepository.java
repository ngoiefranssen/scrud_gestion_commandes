package gestion_commandes_apis.gestion_commandes_apis.repositories;

import gestion_commandes_apis.gestion_commandes_apis.models.CommandeEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CommandeRepository extends JpaRepository<CommandeEntity, Integer> {}
