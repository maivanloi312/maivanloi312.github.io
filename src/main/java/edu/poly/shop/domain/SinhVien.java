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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "DSsinhvien")
public class SinhVien implements Serializable{
	@Id @Column(name = "idsinhvien", length = 10, columnDefinition = "nchar(10)")
	private String idSinhVien;
	@Column(name = "ho", length = 20, columnDefinition = "nvarchar(50) not null")
	private String ho;
	@Column(name = "ten", length = 10, columnDefinition = "nvarchar(10) not null")
	private String ten;
	@Column(name = "phai", columnDefinition = "boolean default false")
	private Boolean phai;
	@Column(name = "diachi", length = 100, columnDefinition = "nvarchar(100) not null")
	private String diaChi;
	@Column(name = "danghihoc", columnDefinition = "boolean default false")
	private Boolean daNghiHoc;
	@ManyToOne @JoinColumn(name = "idlop")
	private Lop lop;
    @OneToOne @JoinColumn(name = "idtaikhoan", unique = true)
    private TaiKhoan taiKhoanSinhVien;
    @OneToMany(mappedBy = "sinhVien", cascade = CascadeType.ALL)
    List<DangKy> dangKy;
}
