package com.spring.CMS.JWT;

import java.util.ArrayList;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.spring.CMS.DAO.UserDAO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomerUserDetailsService implements UserDetailsService {
	@Autowired
	UserDAO userdao;
	private com.spring.CMS.POJO.User userDetail;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.info("Inside loadUserByUsername {}", username);
		userDetail = userdao.findByEmailId(username);
		if (!Objects.isNull(userDetail)) {
			return new User(userDetail.getEmail(), userDetail.getPassword(), new ArrayList<>());
		} else
			throw new UsernameNotFoundException("Username not found.");
	}

	public com.spring.CMS.POJO.User getUserDetail() {
		return userDetail;
	}

}
