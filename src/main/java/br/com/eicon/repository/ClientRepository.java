package br.com.eicon.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import br.com.eicon.domain.Client;

public interface ClientRepository extends CrudRepository<Client, Long>{

	Optional<Client> findOneByNameIgnoreCase(String nome);
}
