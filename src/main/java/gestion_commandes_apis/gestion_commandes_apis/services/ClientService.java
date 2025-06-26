package gestion_commandes_apis.gestion_commandes_apis.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import gestion_commandes_apis.gestion_commandes_apis.dtos.update.UpdateClientRequestDTO;
import gestion_commandes_apis.gestion_commandes_apis.models.ClientEntity;
import gestion_commandes_apis.gestion_commandes_apis.repositories.ClientRepository;

@Service
public class ClientService {
    @Autowired
    private ClientRepository clientRepository;

    // Méthode pour trouver tous les clients
    public List<ClientEntity> getAllClientEntities() {
        return clientRepository.findAll();
    }

    // Méthode pour obtenir tous les clients avec pagination et recherche
    public Page<ClientEntity> getAllClientEntities(String search, Pageable pageable) {
        if (search != null && !search.isEmpty()) {
            return clientRepository.findByNomClientContainingIgnoreCaseOrEmailClientContainingIgnoreCase(search, search,
                    pageable);
        } else {
            return clientRepository.findAll(pageable);
        }
    }

    // Méthode pour trouver un client par son ID
    public Optional<ClientEntity> getByIdClientEntity(Integer clientId) {
        return clientRepository.findById(clientId);
    }

    // Méthode pour ajouter un client
    public ClientEntity savClientEntity(ClientEntity client) {
        Optional<ClientEntity> existingClient = clientRepository.findByEmailClient(client.getEmailClient());

        if (!existingClient.isEmpty())
            throw new IllegalArgumentException("L'email existe déjà !");

        return clientRepository.save(client);
    }

    // Méthode pour mettre à jour un client
    public ClientEntity updateClientEntity(Integer id, UpdateClientRequestDTO dto) {

        ClientEntity client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client introuvable"));

        if (dto.getNomClient() != null)
            client.setNomClient(dto.getNomClient());

        if (dto.getEmailClient() != null)
            client.setEmailClient(dto.getEmailClient());

        if (dto.getAdresseClient() != null)
            client.setAdresseClient(dto.getAdresseClient());

        if (dto.getTelephoneClient() != null)
            client.setTelephoneClient(dto.getTelephoneClient());

        return clientRepository.save(client);
    }

    // Méthode pour supprimer un client par son ID
    public void deleteClientEntity(Integer clientId) {
        // Vérification de l'existence du client avant la suppression
        if (!clientRepository.existsById(clientId)) {
            throw new RuntimeException("Client not found with id: " + clientId);
        }
        clientRepository.deleteById(clientId);
    }

    // Méthode pour trouver un client par son email
    public Optional<ClientEntity> getByEmailClient(String emailClient) {
        return clientRepository.findByEmailClient(emailClient);
    }

    // Méthode pour vérifier l'existence d'un client par son email
    public boolean existsByEmailClient(String emailClient) {
        return clientRepository.existsByEmailClient(emailClient);
    }

    // Méthode pour obtenir un client par son ID
    public Optional<ClientEntity> getClientById(Integer id) {
        return clientRepository.findById(id);
    }

}
