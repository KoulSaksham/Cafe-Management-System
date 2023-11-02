package com.spring.CMS.RestImpl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.spring.CMS.Rest.DashBoardRest;
import com.spring.CMS.Services.DashBoardService;
@RestController
@CrossOrigin
public class DashBoardRestImpl implements DashBoardRest{

	@Autowired
	DashBoardService dbService;
	@Override
	public ResponseEntity<Map<String, Object>> getCount() {
		return dbService.getCount();
	}
	
}
