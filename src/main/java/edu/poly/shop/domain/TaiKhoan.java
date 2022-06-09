package edu.poly.shop.domain;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "DStaikhoan")
public class TaiKhoan implements Serializable{
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idtaikhoan")
	private Long idTaiKhoan;
	@Column(name = "username", unique = true, length = 20, columnDefinition = "varchar(20) not null")
	private String username;
	@Column(name = "password", length = 60, columnDefinition = "varchar(60) not null")
	private String password;
	@Column(name = "email", length = 20, columnDefinition = "varchar(60) not null")
	private String email;
	@ManyToOne @JoinColumn(name = "idquyen")
	private Quyen quyen;
    @OneToOne(mappedBy = "taiKhoanSinhVien", cascade = CascadeType.ALL)
    private SinhVien sinhVien;
    @OneToOne(mappedBy = "taiKhoanGiangVien", cascade = CascadeType.ALL)
    private GiangVien giangVien;
	public TaiKhoan(String username, String password, String email, Quyen quyen) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.quyen = quyen;
	}
}
