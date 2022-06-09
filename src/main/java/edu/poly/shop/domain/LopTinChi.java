package edu.poly.shop.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "DSloptinchi", uniqueConstraints = {
		@UniqueConstraint(columnNames = {"nienkhoa", "hocky", "idmonhoc", "nhom"})
})
public class LopTinChi implements Serializable{
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idltc")
	private Long idLTC;
	@Column(name = "nienkhoa", length = 9, columnDefinition = "nchar(9) not null")
	private String nienKhoa;
	@Column(name = "hocky", nullable = false)
	private Integer hocKy;
	@Column(name = "sosvtoithieu", nullable = false)
	private Integer soSVToiThieu;
	@Column(name = "nhom", nullable = false)
	private Integer nhom;
	@Column(name = "huylop")
	private Boolean huyLop = false;
	@ManyToOne @JoinColumn(name = "idmonhoc")
	private MonHoc monHoc;
	@ManyToOne @JoinColumn(name = "idgiangvien")
	private GiangVien giangVien;
	@ManyToOne @JoinColumn(name = "idkhoa")
	private Khoa khoaLopTinChi;
	@OneToMany(mappedBy = "lopTinChi", cascade = CascadeType.ALL)
	List<DangKy> dsDangKy;
}