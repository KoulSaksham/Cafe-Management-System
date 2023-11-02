package com.spring.CMS.Services;

import java.util.Map;

import org.springframework.http.ResponseEntity;

public interface DashBoardService {

	ResponseEntity<Map<String, Object>> getCount();

}
