package br.com.eicon.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import br.com.eicon.domain.Client;

@RunWith(SpringJUnit4ClassRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
public class ClientRepositoryTest {

	private static final String CLIENT_NAME = "João";

	@Autowired
	private TestEntityManager entityManager;
	
	@Autowired
	private ClientRepository repository;
	
	@Before
	public void preparaCliente() {
		createClient(1L, CLIENT_NAME);
	}
	
	@Test
	public void deveEncontrarNomeCliente() {
		assertEquals(CLIENT_NAME, repository.findOneByNameIgnoreCase("joão").get().getName());
		assertEquals(CLIENT_NAME, repository.findOneByNameIgnoreCase("JOÃO").get().getName());
		assertEquals(CLIENT_NAME, repository.findOneByNameIgnoreCase("João").get().getName());
		assertEquals(CLIENT_NAME, repository.findOneByNameIgnoreCase("jOÃO").get().getName());
	}
	
	@Test
	public void naoDeveEncontrarNomeCliete() {
		assertFalse(CLIENT_NAME, repository.findOneByNameIgnoreCase("joao").isPresent());
		assertFalse(CLIENT_NAME, repository.findOneByNameIgnoreCase("joõ").isPresent());
		assertFalse(CLIENT_NAME, repository.findOneByNameIgnoreCase("Joã").isPresent());
	}
	
	public void createClient(long id, String name) {
		Client client = new Client();
		client.setId(id);
		client.setName(name);
		entityManager.merge(client);
	}
}
