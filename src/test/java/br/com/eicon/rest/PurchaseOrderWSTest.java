package br.com.eicon.rest;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.ResourceUtils;

import br.com.eicon.EiconApplicationTests;
import br.com.eicon.domain.Client;
import br.com.eicon.domain.PurchaseOrder;
import br.com.eicon.repository.PurchaseOrderRepository;
import br.com.eicon.service.PurchaseOrderService;

@RunWith(SpringJUnit4ClassRunner.class)
@DataJpaTest
public class PurchaseOrderWSTest extends EiconApplicationTests {


	@Autowired
	private TestEntityManager entityManager;

	private MockMvc mockMvc;

	@InjectMocks
	private PurchaseOrderWS wsService;

	@Mock
	private PurchaseOrderService service;
	
	@Mock
	private PurchaseOrderRepository repository;
	

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(wsService).build();
	}

	@Test
	public void testListOrders() throws Exception {
		List<PurchaseOrder> users = Arrays.asList(
				createOrder(1L, createClient(1, "Fulano 1"), "Produto 1", 1L, 10.0D),
				createOrder(2L, createClient(1,"Fulano 2"), "Produto 2", 2L, 20.0D));
	    when(wsService.getPedidoJson()).thenReturn(users);
	    
		createOrder(1L, createClient(1, "Fulano 1"), "Produto 1", 1L, 10.0D);
		createOrder(2L, createClient(1,"Fulano 2"), "Produto 2", 2L, 20.0D);

		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/pedidos/json").accept(MediaType.APPLICATION_JSON_VALUE);
		MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isOk()).andReturn();

		StringBuilder expected = new StringBuilder();
		expected.append("[{\"id\":1,\"controlId\":1,\"registerDate\":null,\"productName\":\"Produto 1\",\"productValue\":10.0,\"productQuantity\":1,\"client\":{\"id\":1,\"name\":\"Fulano 1\"},\"totalValue\":null}");
		expected.append(",{\"id\":2,\"controlId\":2,\"registerDate\":null,\"productName\":\"Produto 2\",\"productValue\":20.0,\"productQuantity\":2,\"client\":{\"id\":1,\"name\":\"Fulano 2\"},\"totalValue\":null}]");
				
		JSONAssert.assertEquals(expected.toString(), result.getResponse().getContentAsString(), false);
	}
	
	@Test
	public void testSaveOrders() throws Exception {
		File file = ResourceUtils.getFile("classpath:pedidos.xml");
		
		byte[] bytesArray = new byte[(int) file.length()];

		FileInputStream fis = new FileInputStream(file);
		fis.read(bytesArray); //read file into bytes[]
		fis.close();
		
		MockMultipartFile mockMultipart = new MockMultipartFile("file", "pedidos.xml", null, bytesArray);
		mockMvc.perform(fileUpload("/pedidos/upload/xml").file(mockMultipart))
		.andExpect(status().isOk());
	}


	public PurchaseOrder createOrder(Long controlId, Client client, String productName, Long productQuantity, Double productValue) {
		PurchaseOrder order = new PurchaseOrder();
		order.setControlId(controlId);
		order.setClient(client);
		order.setProductName(productName);
		order.setProductQuantity(productQuantity);
		order.setProductValue(productValue);
		entityManager.merge(client);
		entityManager.persist(order);
		return order;
	}

	public Client createClient(long id, String name) {
		Client client = new Client();
		client.setId(id);
		client.setName(name);
		return client;
	}
}
