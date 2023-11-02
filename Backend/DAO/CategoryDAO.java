package com.spring.CMS.DAO;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.CMS.POJO.Category;

public interface CategoryDAO extends JpaRepository<Category, Integer> {
	List<Category> getAllCategory();
}
