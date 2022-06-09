package edu.poly.shop.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "DSquyen")

public class Quyen implements Serializable{
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idquyen")
	private Long idQuyen;
	@Column(name="tenquyen", unique = true, length = 20, columnDefinition = "nvarchar(20) not null")
	private String tenQuyen;
	@OneToMany(mappedBy = "quyen", cascade = CascadeType.ALL)
	List<TaiKhoan> dsTaiKhoan;
	public Quyen(String tenQuyen) {
		this.tenQuyen = tenQuyen;
	}
}
