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
@Table(name = "DSgiangvien")
public class GiangVien implements Serializable{
	@Id @Column(name = "idgiangvien", length = 10, columnDefinition = "nchar(10)")
	private String idGiangVien;
	@Column(name = "ho", length = 20, columnDefinition = "nvarchar(50) not null")
	private String ho;
	@Column(name = "ten", length = 10, columnDefinition = "nvarchar(10) not null")
	private String ten;
	@Column(name = "hocvi", length = 20, columnDefinition = "nvarchar(20)")
	private String hocVi;
	@Column(name = "hocham", length = 20, columnDefinition = "nvarchar(20)")
	private String hocHam;
	@Column(name = "chuyenmon", length = 50, columnDefinition = "nvarchar(50)")
	private String chuyenMon;
	@OneToMany(mappedBy = "giangVien", cascade = CascadeType.ALL)
	List<LopTinChi> dsLopTinChi;
	@ManyToOne @JoinColumn(name = "idkhoa")
	private Khoa khoaGiangVien;
    @OneToOne @JoinColumn(name = "idtaikhoan", unique = true)
    private TaiKhoan taiKhoanGiangVien;
}
