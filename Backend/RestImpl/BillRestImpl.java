package com.spring.CMS.RestImpl;

import java.util.List;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.spring.CMS.POJO.Bill;
import com.spring.CMS.Rest.BillRest;
import com.spring.CMS.Services.BillService;
import com.spring.CMS.Utils.CafeUtility;
import com.spring.CMS.constants.CafeConstants;
@CrossOrigin
@RestController
public class BillRestImpl implements BillRest{
	
	@Autowired
	BillService billServ;
	
	@Override
	public ResponseEntity<String> generateReport(Map<String, Object> requestMap) {
		try {
			return billServ.generateReport(requestMap);
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return CafeUtility.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<List<Bill>> getBills() {
		try {
			return billServ.getBills();
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return null;
	}

	@Override
	public ResponseEntity<byte[]> getPdf(Map<String, Object> requestMap) {
		try {
			return billServ.getPdf(requestMap);
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return null;
	}

	@Override
	public ResponseEntity<String> deleteBill(Integer id) {
		try {
			return billServ.deleteBill(id);
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return CafeUtility.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
