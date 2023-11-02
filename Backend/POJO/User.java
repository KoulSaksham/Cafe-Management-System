package com.spring.CMS.POJO;

import java.io.Serializable;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import lombok.Data;

@NamedQuery(name = "User.findByEmailId", query = "select x from User x where x.email=:email")
@NamedQuery(name = "User.getAllUsers", query = "select new com.spring.CMS.Wrapper.UserWrapper(x.id,x.name,x.email,x.contactNo,x.status) from User x where x.role='user'")
@NamedQuery(name = "User.updateStatus", query = "update User x set x.status=:status where x.id=:id")
@NamedQuery(name = "User.getAllAdmin", query = "select x.email from User x where x.role='admin'")
@Data
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "user")
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "name")
	private String name;

	@Column(name = "phone")
	private String contactNo;

	@Column(name = "email")
	private String email;

	@Column(name = "password")
	private String password;

	@Column(name = "status")
	private String status;

	@Column(name = "role")
	private String role;
}
