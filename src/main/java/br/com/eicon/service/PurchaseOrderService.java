package br.com.eicon.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.eicon.EiconApplication;
import br.com.eicon.domain.PurchaseOrder;
import br.com.eicon.repository.PurchaseOrderRepository;
import br.com.eicon.validator.PurchaseOrderValidator;

/**
 * funções para controle de pedidos
 * @author Douglas-Takara
 */
@Service
public class PurchaseOrderService {

	@Autowired
	private PurchaseOrderRepository orderRepository;
	
	@Autowired
	private PurchaseOrderValidator validator;

	public void save(@Valid PurchaseOrder purchaseOrder) {
		try {
			orderRepository.save(purchaseOrder);
		}catch(Exception e) {
			EiconApplication.log.error(e.getMessage());
		}
	}

	public String saveAll(List<PurchaseOrder> purchaseOrders) {
		if(validator.valida(purchaseOrders)) {
			purchaseOrders.forEach(order -> {
				save(order);
			});
		}else {
			return "Pedido inválido";
		}
		return "Pedidos salvos com sucesso";
	}

	public List<PurchaseOrder> findAll(){
		List<PurchaseOrder> orders = new ArrayList<>();
		Iterable<PurchaseOrder> allOrders = orderRepository.findAll();
		allOrders.forEach(orders::add);
		return orders;
	}

	public PurchaseOrder findOne() {
		return new PurchaseOrder();
	}

	public List<PurchaseOrder> findByClientId(Long clientId) {
		List<PurchaseOrder> pedidos = new ArrayList<>();
		Iterable<PurchaseOrder> allPedidos = orderRepository.findByClientId(clientId);
		allPedidos.forEach(pedidos::add);
		return pedidos;
	}

	public List<PurchaseOrder> findByControlIdAndRegisterDate(Long pedidoId, LocalDate localDate) {
		List<PurchaseOrder> pedidos = new ArrayList<>();
		java.util.Date date = java.sql.Date.valueOf(localDate);
		Iterable<PurchaseOrder> allPedidos = orderRepository.findByControlIdAndRegisterDate(pedidoId, date);
		allPedidos.forEach(pedidos::add);
		return pedidos;
	}
}
