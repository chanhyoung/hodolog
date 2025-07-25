package com.hodolog.api.domain.shop;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString(of = {"id", "name", "address"})	
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Member {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "member_id")
	private Long id;

	@Column(nullable = false, length = 50)
	private String name;
	
  @Embedded
	private Address address;

  @OneToMany(mappedBy = "member")
  private List<Order> orders = new ArrayList<>();

	@Builder
  public Member(String name, Address address) {
		this.name = name;
		this.address = address;
  }

  public void setName(String name) {
    this.name = name;
  }

	public void setAddress(Address address) {
		this.address = address;
	}
}
