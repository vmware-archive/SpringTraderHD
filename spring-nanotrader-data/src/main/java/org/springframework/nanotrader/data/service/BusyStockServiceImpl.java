package org.springframework.nanotrader.data.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.nanotrader.data.domain.BusyStock;
import org.springframework.nanotrader.data.repository.BusyStockRepository;

public class BusyStockServiceImpl implements BusyStockService {
	
	@Autowired
	BusyStockRepository busyStockRepository;
	
	@Override
	public List<BusyStock> listBusyStock() {
		// TODO Auto-generated method stub
		return busyStockRepository.findAll();
	}

}
