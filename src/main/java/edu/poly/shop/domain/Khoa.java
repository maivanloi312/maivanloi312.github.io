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
@Table(name = "DSkhoa")

public class Khoa implements Serializable{
	@Id @Column(name = "idkhoa", length = 10, columnDefinition = "nchar(10)")
	private String idKhoa;
	@Column(name="tenkhoa", unique = true, length = 50, columnDefinition = "nvarchar(50) not null")
	private String tenKhoa;
	@OneToMany(mappedBy = "khoa", cascade = CascadeType.ALL)
	List<Lop> dsLop;
	@OneToMany(mappedBy = "khoaGiangVien", cascade = CascadeType.ALL)
	List<GiangVien> dsGiangVien;
	@OneToMany(mappedBy = "khoaLopTinChi", cascade = CascadeType.ALL)
	List<LopTinChi> dsLopTinChi;
}
