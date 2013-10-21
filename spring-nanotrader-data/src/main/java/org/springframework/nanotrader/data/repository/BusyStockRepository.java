package org.springframework.nanotrader.data.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.nanotrader.data.domain.BusyStock;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusyStockRepository extends JpaRepository<BusyStock, Integer>, JpaSpecificationExecutor<BusyStock>  {
	@Query(value = "select bs from BusyStock bs order by bs.count DESC")
	public List<BusyStock> findBusyStock(Pageable pagegable);
	
	@Query(value = "select count(bs) from BusyStock bs")
	public Long findCountOfBusyStock();
}
