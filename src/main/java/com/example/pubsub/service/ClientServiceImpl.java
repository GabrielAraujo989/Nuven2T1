package com.example.pubsub.service;

import com.example.pubsub.domain.Client;
import com.example.pubsub.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public Client save(Client client) {
        return clientRepository.findByExternalId(client.getExternalId())
                .map(existingClient -> {
                    // Atualiza apenas se necessário
                    if (!existingClient.getName().equals(client.getName())) {
                        existingClient.setName(client.getName());
                        return clientRepository.save(existingClient);
                    }
                    return existingClient;
                })
                .orElseGet(() -> clientRepository.save(client));
    }

    @Override
    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    @Override
    public Optional<Client> findById(Long id) {
        return clientRepository.findById(id);
    }

    @Override
    public Client update(Client client) {
        return clientRepository.save(client);
    }

    @Override
    public void deleteById(Long id) {
        clientRepository.deleteById(id);
    }
}