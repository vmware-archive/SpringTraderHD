package org.springframework.nanotrader.data.service;

import java.util.List;

import org.springframework.nanotrader.data.domain.BusyStock;

public interface BusyStockService {
	public List<BusyStock> listBusyStock();
}
