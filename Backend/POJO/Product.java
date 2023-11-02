package com.spring.CMS.POJO;

import java.io.Serializable;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import lombok.Data;

@NamedQuery(name = "Product.getAllProduct", query = "select new com.spring.CMS.Wrapper.ProductWrapper(p.id,p.name,p.description,p.price,p.status,p.category.id,p.category.name) from Product p")
@NamedQuery(name = "Product.updateProductStatus", query = "update Product p set p.status=:status where p.id=:id")
@NamedQuery(name = "Product.getProductByCategoryId", query = "select new com.spring.CMS.Wrapper.ProductWrapper(p.id,p.name) from Product p where p.category.id=:id and p.status='true'")
@NamedQuery(name = "Product.getProductById", query = "select new com.spring.CMS.Wrapper.ProductWrapper(p.id,p.name,p.description,p.price) from Product p where p.id=:id")
@Data
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "product")
public class Product implements Serializable {
	public static final long serialVersionUID = 123456L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "name")
	private String name;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_fk", nullable = false)
	private Category category;

	@Column(name = "description")
	String description;

	@Column(name = "price")
	private Integer price;

	@Column(name = "status")
	private String status;

}
