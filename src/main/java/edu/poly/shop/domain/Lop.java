package edu.poly.shop.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "DSlop")
public class Lop implements Serializable{
	@Id @Column(name = "idlop", length = 10, columnDefinition = "nchar(10)")
	private String idLop;
	@Column(name = "tenlop", length = 50, columnDefinition = "nvarchar(50) not null")
	private String tenLop;
	@Column(name = "khoahoc", length = 9, columnDefinition = "nchar(9) not null")
	private String khoaHoc;
	@ManyToOne @JoinColumn(name = "idkhoa")
	private Khoa khoa;
	@OneToMany(mappedBy = "lop", cascade = CascadeType.ALL)
	List<SinhVien> dsSinhVien;
}
