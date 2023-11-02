package com.spring.CMS.Wrapper;

import lombok.Data;

@Data
public class UserWrapper {
	private Integer id;
	private String name;
	private String email;
	private String contactNo;
	private String status;

	public UserWrapper(Integer id, String name, String email, String contactNo, String status) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.contactNo = contactNo;
		this.status = status;
	}
}
