package edu.poly.shop.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "DSmonhoc")

public class MonHoc implements Serializable{
	@Id @Column(name = "idmonhoc", length = 10, columnDefinition = "nchar(10)")
	private String idMonHoc;
	@Column(name = "tenmonhoc", unique = true, length = 50, columnDefinition = "nvarchar(50) not null")
	private String tenMonHoc;
	@Column(name = "sotietlt", nullable = false)
	private Integer soTietLT;
	@Column(name = "sotietth", nullable = false)
	private Integer soTietTH;
	@OneToMany(mappedBy = "monHoc", cascade = CascadeType.ALL)
	List<LopTinChi> dsLopTinChi;
}
