package com.spring.CMS.DAO;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.spring.CMS.POJO.Bill;

public interface BillDAO extends JpaRepository<Bill,Integer>{

	List<Bill> getAllBills();

	List<Bill> getBillByUsername(@Param("username")String username);
	
}
