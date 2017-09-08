package br.com.eicon.validator;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.eicon.domain.PurchaseOrder;
import br.com.eicon.repository.PurchaseOrderRepository;

/**
 * Classe responsável por validar o serviço de pedidos 
 * @author Douglas-Takara
 */
@Component
public class PurchaseOrderValidator{

	private static final Double DESCONTO_DE_5 = 0.5;
	private static final Double DESCONTO_DE_10 = 0.10;

	@Autowired
	private PurchaseOrderRepository repository;
	
	@Autowired
	private ClientValidator clientValidator;
	
	private boolean result;
	
	public boolean valida(List<PurchaseOrder> orders) {
		
		result = true;
		
		if(orders.size() > 10)
			return false;
		
		orders.forEach(order -> {
			PurchaseOrder ordersDB = searchOnDB(order);

			if(ordersDB!=null && ordersDB.getControlId()!=null) {
				result = false;
				return;
			}
			
			if(!clientValidator.isValid(order)) {
				result = false;
				return;
			}
				 
			if(order.getRegisterDate() == null)
				order.setRegisterDate(java.sql.Date.valueOf(LocalDate.now()));
			if(order.getProductQuantity() == null)
				order.setProductQuantity(1L);
			
			if(order.getProductValue() == null) {
				result = false;
				return;
			}
			
			double totalValue = order.getProductQuantity() * order.getProductValue();
			
			if(order.getProductQuantity() > 10) {
				order.setTotalValue(insertDiscount(totalValue, DESCONTO_DE_10));
			}else if(order.getProductQuantity()>5) {
				order.setTotalValue(insertDiscount(totalValue, DESCONTO_DE_5));
			}else
				order.setTotalValue(totalValue);
		});
		return result;
	}

	private PurchaseOrder searchOnDB(PurchaseOrder order) {
		Optional<PurchaseOrder> purchaseOrder = repository.findByControlId(order.getControlId());
		if(purchaseOrder!=null && purchaseOrder.isPresent())
			return purchaseOrder.get();
		return null;
	}

	private double insertDiscount(double totalValue, double desconto) {
		return totalValue - (totalValue * desconto);
	}
	
	public void setRepository(PurchaseOrderRepository repository) {
		this.repository = repository;
	}
	
	public void setClientValidator(ClientValidator clientValidator) {
		this.clientValidator = clientValidator;
	}
}