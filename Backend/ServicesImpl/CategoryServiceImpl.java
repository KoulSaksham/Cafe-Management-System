package com.spring.CMS.ServicesImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;
import com.spring.CMS.DAO.CategoryDAO;
import com.spring.CMS.JWT.JwtFilter;
import com.spring.CMS.POJO.Category;
import com.spring.CMS.Services.CategoryService;
import com.spring.CMS.Utils.CafeUtility;
import com.spring.CMS.constants.CafeConstants;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {
	@Autowired
	CategoryDAO categoryDao;
	@Autowired
	JwtFilter jwtFilter;

	@Override
	public ResponseEntity<String> addNewCategory(Map<String, String> requestMap) {
		try {
			if (jwtFilter.isAdmin()) {
				if (validCategoryMap(requestMap, false)) {
					categoryDao.save(getCategoryFromMap(requestMap, false));
					return CafeUtility.getResponseEntity("Category Added Successfully.", HttpStatus.OK);
				}
			} else {
				return CafeUtility.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return CafeUtility.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private boolean validCategoryMap(Map<String, String> requestMap, boolean validateId) {
		if (requestMap.containsKey("name")) {
			if (requestMap.containsKey("id") && validateId) {
				return true;
			} else if (!validateId) {
				return true;
			}
		}
		return false;
	}

	private Category getCategoryFromMap(Map<String, String> requestMap, boolean isAdd) {
		Category category = new Category();
		if (isAdd) {
			category.setId(Integer.parseInt(requestMap.get("id")));
		}
		category.setName(requestMap.get("name"));
		return category;
	}

	@Override
	public ResponseEntity<List<Category>> getAllCategory(String filterValue) {
		try {
			if (!Strings.isNullOrEmpty(filterValue) && filterValue.equalsIgnoreCase("true")) {
				log.info("Inside if");
				return new ResponseEntity<List<Category>>(categoryDao.getAllCategory(), HttpStatus.OK);
			}
			return new ResponseEntity<>(categoryDao.findAll(), HttpStatus.OK);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> updateCategory(Map<String, String> requestMap) {
		try {
			if (jwtFilter.isAdmin()) {
				if (validCategoryMap(requestMap, true)) {
					Optional optional = categoryDao.findById(Integer.parseInt(requestMap.get("id")));
					if(!optional.isEmpty()) {
						categoryDao.save(getCategoryFromMap(requestMap,true));
						return CafeUtility.getResponseEntity("Category Updated Successfully",HttpStatus.OK);
					}else {
						return CafeUtility.getResponseEntity("Category id does not exist", HttpStatus.OK);
					}
				}
				return CafeUtility.getResponseEntity(CafeConstants.INVALID_DATA,HttpStatus.BAD_REQUEST);
			} else {
				return CafeUtility.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return CafeUtility.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
