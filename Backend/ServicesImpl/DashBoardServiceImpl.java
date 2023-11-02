package com.spring.CMS.ServicesImpl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.spring.CMS.DAO.BillDAO;
import com.spring.CMS.DAO.CategoryDAO;
import com.spring.CMS.DAO.ProductDAO;
import com.spring.CMS.Services.DashBoardService;
@CrossOrigin
@Service
public class DashBoardServiceImpl implements DashBoardService {
	@Autowired
	CategoryDAO categoryDao;
	@Autowired
	ProductDAO productDao;
	@Autowired
	BillDAO billDao;
	
	@Override
	public ResponseEntity<Map<String, Object>> getCount() {
		Map<String,Object> map = new HashMap<>();
		map.put("category", categoryDao.count());
		map.put("product", productDao.count());
		map.put("bill", billDao.count());
		return new ResponseEntity<>(map,HttpStatus.OK);
	}

}
