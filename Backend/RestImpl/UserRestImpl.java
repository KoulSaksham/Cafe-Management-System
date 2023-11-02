package com.spring.CMS.RestImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.spring.CMS.Rest.UserRest;
import com.spring.CMS.Services.UserService;
import com.spring.CMS.Utils.CafeUtility;
import com.spring.CMS.Wrapper.UserWrapper;
import com.spring.CMS.constants.CafeConstants;

import lombok.extern.slf4j.Slf4j;
@CrossOrigin
@RestController
@Slf4j
public class UserRestImpl implements UserRest {
	@Autowired
	UserService userService;

	@Override
	public ResponseEntity<String> signUp(Map<String, String> requestMap) {
		try {
			return userService.signUp(requestMap);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return CafeUtility.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> login(Map<String, String> requestMap) {
		try {
			log.info("inside login {}",requestMap);
			return userService.login(requestMap);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return CafeUtility.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<List<UserWrapper>> getAllUsers() {
		try {
			return userService.getAllUsers();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new ResponseEntity<List<UserWrapper>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> update(Map<String, String> requestMap) {
		try {
			return userService.update(requestMap);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return CafeUtility.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> checkToken() {
		try {
			return userService.checkToken();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return CafeUtility.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
		try {
			return userService.changePassword(requestMap);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return CafeUtility.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> forgotPassword(Map<String, String> requestMap) {
		try {
			return userService.forgetPassword(requestMap);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return CafeUtility.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
