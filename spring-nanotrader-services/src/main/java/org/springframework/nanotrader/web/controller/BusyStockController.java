package org.springframework.nanotrader.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.nanotrader.service.domain.CollectionResult;
import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BusyStockController extends BaseController{
	
	@RequestMapping(value = "/analytics", method = RequestMethod.GET)
	public ResponseEntity<CollectionResult> findBusyStock(
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "pageSize", required = false) Integer pageSize) {
		//this.getSecurityUtil().checkAccount(accountId); // verify that the
														// account on the path
														// is the same as the
														// authenticated user
		System.out.println("In BusyStockController!");
		System.out.println(getTradingServiceFacade()
				.findBusyStock(page,pageSize));
		return new ResponseEntity<CollectionResult>(getTradingServiceFacade()
				.findBusyStock(page,pageSize),
				getNoCacheHeaders(), HttpStatus.OK);
	}
}
