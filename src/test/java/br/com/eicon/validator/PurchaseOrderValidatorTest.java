package br.com.eicon.validator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import br.com.eicon.domain.Client;
import br.com.eicon.domain.PurchaseOrder;
import br.com.eicon.repository.ClientRepository;
import br.com.eicon.repository.PurchaseOrderRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
public class PurchaseOrderValidatorTest {

	@Autowired
	private TestEntityManager entityManager;
	
	@InjectMocks
	private PurchaseOrderValidator validator= new PurchaseOrderValidator();
	
//	@InjectMocks
//	private ClientValidator clientValidator = new ClientValidator();
	 
	@Autowired
	private PurchaseOrderRepository repository;
	
//	@Mock
	@Autowired
	private ClientRepository clientRepository;
	
	@Before
	public void insertDataOnDB() {
		try {
		    MockitoAnnotations.initMocks(this);
		    validator.setRepository(repository);
		    ClientValidator clientValidator = new ClientValidator();
		    clientValidator.setClientReposity(clientRepository);
			validator.setClientValidator(clientValidator);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testaTamanhoMaximoDePedidos() {
		PurchaseOrder order = createOrder(1L, createClient(1, "Fulano 1"), "Produto 1", 1L, 10.0D);
		PurchaseOrder order2 = createOrder(2L, createClient(1,"Fulano 2"), "Produto 2", 2L, 20.0D);
		PurchaseOrder order3 = createOrder(3L, createClient(1,"Fulano 3"), "Produto 3", 3L, 30.0D);
		PurchaseOrder order4 = createOrder(4L, createClient(1,"Fulano 4"), "Produto 4", 4L, 40.0D);
		PurchaseOrder order5 = createOrder(5L, createClient(1,"Fulano 5"), "Produto 5", 5L, 50.0D);
		PurchaseOrder order6 = createOrder(6L, createClient(1,"Fulano 6"), "Produto 6", 4L, 40.0D);
		PurchaseOrder order7 = createOrder(7L, createClient(1,"Fulano 7"), "Produto 7", 3L, 15.0D);
		PurchaseOrder order8 = createOrder(8L, createClient(1,"Fulano 8"), "Produto 8", 2L, 16.0D);
		PurchaseOrder order9 = createOrder(9L, createClient(1,"Fulano 9"), "Produto 9", 1L, 18.0D);
		PurchaseOrder order10 = createOrder(10L, createClient(1,"Fulano 10"), "Produto 10", 2L, 17.0D);
		PurchaseOrder order11 = createOrder(11L, createClient(1,"Fulano 11"), "Produto 11", 4L, 98.0D);
		
		boolean result = validator.validade(Arrays.asList(order, order2, order3, order4,  order5, order6, order7, order8, order9, order10, order11));
		assertFalse(result);
	}
	
	@Test
	public void testaDescontoDe5() {
		PurchaseOrder order = createOrder(12L, createClient(1, "Fulano"), "Produto 12", 6L, 10.0D);
		validator.validade(Arrays.asList(order));
		entityManager.persist(order);
		PurchaseOrder orderDB =  repository.findByControlId(12L).get();
		assertEquals(30.0, orderDB.getTotalValue().doubleValue(), 0d);
	}
	
	@Test
	public void testaDescontoDe10() {
		PurchaseOrder order = createOrder(13L, createClient(1, "Fulano"), "Produto 13", 11L, 15.50D);
		validator.validade(Arrays.asList(order));
		entityManager.persist(order);
		PurchaseOrder orderDB =  repository.findByControlId(13L).get();
		assertEquals(153.45D, orderDB.getTotalValue().doubleValue(), 0d);
	}
	
	public PurchaseOrder createOrder(Long controlId, Client client, String productName, Long productQuantity, Double productValue) {
		PurchaseOrder order = new PurchaseOrder();
		order.setControlId(controlId);
		order.setClient(client);
		order.setProductName(productName);
		order.setProductQuantity(productQuantity);
		order.setProductValue(productValue);
		entityManager.merge(client);
		return order;
	}
	
	public Client createClient(long id, String name) {
		Client client = new Client();
		client.setId(id);
		client.setName(name);
		return client;
	}
}
