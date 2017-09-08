package br.com.eicon.repository;

import java.util.Date;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import br.com.eicon.domain.PurchaseOrder;

public interface PurchaseOrderRepository extends CrudRepository<PurchaseOrder, Long> {

	Optional<PurchaseOrder> findOneByProductNameIgnoreCase(String nome);
	
	Optional<PurchaseOrder> findByControlId(Long controlId);

	Iterable<PurchaseOrder> findByClientId(Long clientId);

	Iterable<PurchaseOrder> findByControlIdAndRegisterDate(Long pedidoId, Date date);
}
