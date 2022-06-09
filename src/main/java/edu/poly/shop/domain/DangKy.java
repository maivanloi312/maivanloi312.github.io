package edu.poly.shop.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "DSdangky")
public class DangKy implements Serializable{
	@Id @ManyToOne @JoinColumn(name = "idltc")
	private LopTinChi lopTinChi;
	@Id @ManyToOne @JoinColumn(name = "idsinhvien")
	private SinhVien sinhVien;
	@Column(name = "diemcc")
	private Integer diemcc;
	@Column(name = "diemgk")
	private Float diemgk;
	@Column(name = "diemck")
	private Float diemck;
	@Column(name = "huydangky")
	private Boolean huyDangKy = false;
}
