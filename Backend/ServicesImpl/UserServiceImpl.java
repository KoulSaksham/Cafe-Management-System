package com.spring.CMS.ServicesImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;
import com.spring.CMS.DAO.UserDAO;
import com.spring.CMS.JWT.CustomerUserDetailsService;
import com.spring.CMS.JWT.JwtFilter;
import com.spring.CMS.JWT.JwtUtil;
import com.spring.CMS.POJO.User;
import com.spring.CMS.Services.UserService;
import com.spring.CMS.Utils.CafeUtility;
import com.spring.CMS.Utils.EmailUtils;
import com.spring.CMS.Wrapper.UserWrapper;
import com.spring.CMS.constants.CafeConstants;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserDAO userdao;
	@Autowired
	AuthenticationManager authManager;
	@Autowired
	CustomerUserDetailsService customerUserDetailsService;
	@Autowired
	JwtUtil jwtUtil;
	@Autowired
	JwtFilter jwtFilter;
	@Autowired
	EmailUtils emailUtils;

	@Override
	public ResponseEntity<String> signUp(Map<String, String> requestMap) {
		try {
			log.info("Inside signup {}", requestMap);
			if (validateSignUpMap(requestMap)) {
				User user = userdao.findByEmailId(requestMap.get("email"));
				if (Objects.isNull(user)) {
					userdao.save(getUserFromMap(requestMap));
					return CafeUtility.getResponseEntity("Successfully Registered.", HttpStatus.OK);
				} else {
					return CafeUtility.getResponseEntity("Email already exists", HttpStatus.BAD_REQUEST);
				}
			} else {
				return CafeUtility.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return CafeUtility.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private boolean validateSignUpMap(Map<String, String> requestMap) {
		if (requestMap.containsKey("name") && requestMap.containsKey("email") && requestMap.containsKey("contactNumber")
				&& requestMap.containsKey("password")) {
			return true;
		} else
			return false;
	}

	private User getUserFromMap(Map<String, String> requestMap) {
		User user = new User();
		user.setName(requestMap.get("name"));
		user.setContactNo(requestMap.get("contactNumber"));
		user.setEmail(requestMap.get("email"));
		user.setPassword(requestMap.get("password"));
		user.setRole("user");
		user.setStatus("false");
		return user;
	}

	@Override
	public ResponseEntity<String> login(Map<String, String> requestMap) {
		log.info("Inside login");
		try {
			String email = requestMap.get("email");
			String password = requestMap.get("password");
			Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
			if (auth.isAuthenticated()) {
				if (customerUserDetailsService.getUserDetail().getStatus().equalsIgnoreCase("true")) {
					return new ResponseEntity<String>(
							"{\"token\":\""
									+ jwtUtil.generateToken(customerUserDetailsService.getUserDetail().getEmail(),
											customerUserDetailsService.getUserDetail().getRole())
									+ "\"}",
							HttpStatus.OK);
				} else {
					return new ResponseEntity<String>("{\"message\":\"" + "Wait for Admin Approval." + "\"}",
							HttpStatus.BAD_REQUEST);
				}
			}

		} catch (Exception ex) {
			log.error("{}", ex);
		}
		return new ResponseEntity<String>("{\"message\":\"" + "Bad Credentials." + "\"}", HttpStatus.BAD_REQUEST);
	}


	@Override
	public ResponseEntity<List<UserWrapper>> getAllUsers() {
		try {
			if (jwtFilter.isAdmin()) {
				return new ResponseEntity<>(userdao.getAllUsers(), HttpStatus.OK);
			} else {
				return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> update(Map<String, String> requestMap) {
		try {
			if (jwtFilter.isAdmin()) {
				Optional<User> optional = userdao.findById(Integer.parseInt(requestMap.get("id")));
				if (!optional.isEmpty()) {
					userdao.updateStatus(requestMap.get("status"), Integer.parseInt(requestMap.get("id")));
					sendMailToAllAdmins(requestMap.get("status"), optional.get().getEmail(), userdao.getAllAdmin());
					return CafeUtility.getResponseEntity("User Status Updated Successfully.", HttpStatus.OK);
				} else {
					return CafeUtility.getResponseEntity("User ID does not exist.", HttpStatus.OK);
				}

			} else {
				return CafeUtility.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return CafeUtility.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private void sendMailToAllAdmins(String status, String user, List<String> allAdmin) {
		allAdmin.remove(jwtFilter.getCurrentUser());
		if (status != null && status.equalsIgnoreCase("true")) {
			emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(), "Account Approved",
					"USER:-" + user + "\n is approved by \nADMIN:-" + jwtFilter.getCurrentUser(), allAdmin);
		} else {
			emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(), "Account Disabled",
					"USER:-" + user + "\n is disabled by \nADMIN:-" + jwtFilter.getCurrentUser(), allAdmin);
		}

	}

	@Override
	public ResponseEntity<String> checkToken() {
		return CafeUtility.getResponseEntity("true", HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
		try {
			User userObj = userdao.findByEmail(jwtFilter.getCurrentUser());
			if (!userObj.equals(null)) {
				if (userObj.getPassword().equals(requestMap.get("oldPassword"))) {
					userObj.setPassword(requestMap.get("newPassword"));
					userdao.save(userObj);
					return CafeUtility.getResponseEntity("Password Updated Successfully.", HttpStatus.OK);
				}
				return CafeUtility.getResponseEntity("Incorrect Old Password.", HttpStatus.BAD_REQUEST);
			}
			return CafeUtility.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return CafeUtility.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> forgetPassword(Map<String, String> requestMap) {
		try {
			User user = userdao.findByEmail(requestMap.get("email"));
			if (!Objects.isNull(user) && !Strings.isNullOrEmpty(user.getEmail()))
				emailUtils.forgotMail(user.getEmail(), "Your Forgot Password Details by Cafe Management System",
						user.getPassword());
			return CafeUtility.getResponseEntity("Check your mail for credentials.", HttpStatus.OK);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return CafeUtility.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
