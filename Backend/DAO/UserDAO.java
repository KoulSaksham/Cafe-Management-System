package com.spring.CMS.DAO;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import com.spring.CMS.POJO.User;
import com.spring.CMS.Wrapper.UserWrapper;

import jakarta.transaction.Transactional;

public interface UserDAO extends JpaRepository<User, Integer> {

	User findByEmailId(@Param("email") String email);

	List<UserWrapper> getAllUsers();

	List<String> getAllAdmin();

	@Transactional
	@Modifying
	Integer updateStatus(@Param("status") String status, @Param("id") Integer id);

	User findByEmail(String email);
}
