package com.spring.CMS.Rest;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
@CrossOrigin
@RequestMapping(path = "/dashboard")
public interface DashBoardRest {
	@GetMapping(path = "/details")
	ResponseEntity<Map<String, Object>> getCount();
}
