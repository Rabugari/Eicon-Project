package br.com.eicon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import br.com.eicon.domain.Client;
import br.com.eicon.repository.ClientRepository;

@Component
public class DataLoader implements ApplicationRunner{

	private ClientRepository repository;
	
	@Autowired
	public DataLoader(ClientRepository repository) {
		this.repository = repository;
	}

	@Override
	public void run(ApplicationArguments arg0) throws Exception {
		repository.save(new Client(1,"Fulano"));
		repository.save(new Client(2,"Betrano"));
		repository.save(new Client(3,"Ciclano"));
		repository.save(new Client(4,"João"));
		repository.save(new Client(5,"Danny"));
		repository.save(new Client(6,"Celia"));
		repository.save(new Client(7,"Samara"));
		repository.save(new Client(8,"Maria"));
		repository.save(new Client(9,"Tadeu"));
		repository.save(new Client(10,"Shimira"));
	}
}
