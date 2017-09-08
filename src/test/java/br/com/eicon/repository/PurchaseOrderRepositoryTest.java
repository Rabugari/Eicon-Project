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
import br.com.eicon.domain.PurchaseOrder;

@RunWith(SpringJUnit4ClassRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
public class PurchaseOrderRepositoryTest {

	private static final String PRODUCT_NAME = "Produto 1";

	@Autowired
	private TestEntityManager entityManager;
	
	@Autowired
	private PurchaseOrderRepository repository;
	
	@Before
	public void preparaPedidos() {
		createOrder(1L, createClient(1L, "Paulo"), PRODUCT_NAME, 2L, 2.0d);
	}
	
	@Test
	public void deveEncontrarPedidoPeloProdutoIndependenteDasLetras() {
		assertEquals(PRODUCT_NAME, repository.findOneByProductNameIgnoreCase("produto 1").get().getProductName());
		assertEquals(PRODUCT_NAME, repository.findOneByProductNameIgnoreCase("ProdUtO 1").get().getProductName());
		assertEquals(PRODUCT_NAME, repository.findOneByProductNameIgnoreCase("PRODUTO 1").get().getProductName());
		assertEquals(PRODUCT_NAME, repository.findOneByProductNameIgnoreCase("PROdutO 1").get().getProductName());
	}
	
	@Test
	public void naoDeveEncontrarOProduto() {
		assertFalse(PRODUCT_NAME, repository.findOneByProductNameIgnoreCase("produto_1").isPresent());
		assertFalse(PRODUCT_NAME, repository.findOneByProductNameIgnoreCase("roduto_1").isPresent());
		assertFalse(PRODUCT_NAME, repository.findOneByProductNameIgnoreCase("produto 2").isPresent());
	}
	
	private void createOrder(Long controlId, Client client, String productName, Long productQuantity, Double productValue) {
		PurchaseOrder order = new PurchaseOrder();
		order.setControlId(controlId);
		order.setClient(client);
		order.setProductName(productName);
		order.setProductQuantity(productQuantity);
		order.setProductValue(productValue);
		entityManager.merge(client);
		entityManager.persist(order);
	}
	
	public Client createClient(long id, String name) {
		Client client = new Client();
		client.setId(id);
		client.setName(name);
		return client;
	}
}
