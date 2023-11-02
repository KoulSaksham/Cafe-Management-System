package com.spring.CMS.ServicesImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.spring.CMS.DAO.ProductDAO;
import com.spring.CMS.JWT.JwtFilter;
import com.spring.CMS.POJO.Category;
import com.spring.CMS.POJO.Product;
import com.spring.CMS.Services.ProductService;
import com.spring.CMS.Utils.CafeUtility;
import com.spring.CMS.Wrapper.ProductWrapper;
import com.spring.CMS.constants.CafeConstants;

@Service
public class ProductServiceImpl implements ProductService {
	@Autowired
	ProductDAO productDao;
	@Autowired
	JwtFilter jwtFilter;

	@Override
	public ResponseEntity<String> addNewProduct(Map<String, String> requestMap) {
		try {
			if (jwtFilter.isAdmin()) {
				if (validateProductMap(requestMap, false)) {
					productDao.save(getProductMap(requestMap, false));
					return CafeUtility.getResponseEntity("Product Added Successfully.", HttpStatus.OK);
				}
				return CafeUtility.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
			} else {
				return CafeUtility.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return CafeUtility.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private Product getProductMap(Map<String, String> requestMap, boolean isAdd) {
		Category category = new Category();
		category.setId(Integer.parseInt(requestMap.get("categoryId")));
		Product product = new Product();
		if (isAdd) {
			product.setId(Integer.parseInt(requestMap.get("id")));
		} else {
			product.setStatus("true");
		}
		product.setCategory(category);
		product.setName(requestMap.get("name"));
		product.setDescription(requestMap.get("description"));
		product.setPrice(Integer.parseInt(requestMap.get("price")));
		return product;
	}

	private boolean validateProductMap(Map<String, String> requestMap, boolean validateId) {
		if (requestMap.containsKey("name")) {
			if (requestMap.containsKey("id") && validateId) {
				return true;
			} else if (!validateId)
				return true;
		}
		return false;
	}

	@Override
	public ResponseEntity<List<ProductWrapper>> getAllProduct() {
		try {
			return new ResponseEntity<>(productDao.getAllProduct(), HttpStatus.OK);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> updateProduct(Map<String, String> requestMap) {
		try {
			if (jwtFilter.isAdmin()) {
				if (validateProductMap(requestMap, true)) {
					Optional<Product> optional = productDao.findById(Integer.parseInt(requestMap.get("id")));
					if (!optional.isEmpty()) {
						Product product = getProductMap(requestMap, true);
						product.setStatus(optional.get().getStatus());
						productDao.save(product);
						return CafeUtility.getResponseEntity("Product Updated Successfully.", HttpStatus.OK);
					} else {
						return CafeUtility.getResponseEntity("Product id does not exist!", HttpStatus.OK);
					}
				} else {
					return CafeUtility.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
				}
			} else {
				return CafeUtility.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return CafeUtility.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> deleteProduct(Integer id) {
		try {
			if (jwtFilter.isAdmin()) {
				Optional optional = productDao.findById(id);
				if (!optional.isEmpty()) {
					productDao.deleteById(id);
					return CafeUtility.getResponseEntity("Product Deleted Successfully.", HttpStatus.OK);
				}
				return CafeUtility.getResponseEntity("Product id does not exist!", HttpStatus.OK);
			} else {
				return CafeUtility.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return CafeUtility.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> updateStatus(Map<String, String> requestMap) {
		try {
			if (jwtFilter.isAdmin()) {
				Optional optional = productDao.findById(Integer.parseInt(requestMap.get("id")));
				if (!optional.isEmpty()) {
					productDao.updateProductStatus(requestMap.get("status"), Integer.parseInt(requestMap.get("id")));
					return CafeUtility.getResponseEntity("Product Status Updated Successfully.", HttpStatus.OK);
				}
				return CafeUtility.getResponseEntity("Product id does not exist!", HttpStatus.OK);
			} else {
				return CafeUtility.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return CafeUtility.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<List<ProductWrapper>> getByCategory(Integer id) {
		try {
			return new ResponseEntity<>(productDao.getProductByCategoryId(id), HttpStatus.OK);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<ProductWrapper> getProductById(Integer id) {
		try {
			return new ResponseEntity<>(productDao.getProductById(id), HttpStatus.OK);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new ResponseEntity<>(new ProductWrapper(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
