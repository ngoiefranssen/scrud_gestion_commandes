package gestion_commandes_apis.gestion_commandes_apis.repositories;


import gestion_commandes_apis.gestion_commandes_apis.models.DetailCommandeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetailCommandeRepository extends JpaRepository<DetailCommandeEntity, Integer> {}
