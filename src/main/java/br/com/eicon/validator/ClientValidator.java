package br.com.eicon.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.eicon.domain.PurchaseOrder;
import br.com.eicon.repository.ClientRepository;

@Component
public class ClientValidator {

	@Autowired
	private ClientRepository clientReposity;
	
	public boolean isValid(PurchaseOrder order) {
		if(order.getClient()==null)
			return false;
		
		return clientReposity.findOne(order.getClient().getId()) != null;
	}
	
	public void setClientReposity(ClientRepository clientReposity) {
		this.clientReposity = clientReposity;
	}
}