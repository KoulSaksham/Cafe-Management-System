package com.spring.CMS.DAO;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import com.spring.CMS.POJO.Product;
import com.spring.CMS.Wrapper.ProductWrapper;

import jakarta.transaction.Transactional;

public interface ProductDAO extends JpaRepository<Product, Integer> {

	List<ProductWrapper> getAllProduct();

	@Modifying
	@Transactional
	Integer updateProductStatus(@Param("status") String status, @Param("id") Integer id);

	List<ProductWrapper> getProductByCategoryId(@Param("id") Integer id);

	ProductWrapper getProductById(@Param("id") Integer id);

}
