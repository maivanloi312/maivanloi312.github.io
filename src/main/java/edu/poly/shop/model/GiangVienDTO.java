package edu.poly.shop.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class GiangVienDTO {
	@Size(max = 10, message = "Mã không được quá 10 kí tự")
	@NotEmpty(message = "Mã không được để trống")
	private String idGiangVien;
	@Size(max = 50, message = "Họ không được quá 50 kí tự")
	@NotEmpty(message = "Họ không được để trống")
	private String ho;
	@Size(max = 10, message = "Tên không được quá 10 kí tự")
	@NotEmpty(message = "Tên không được để trống")
	private String ten;
	@Size(max = 20, message = "Học vị không được quá 20 kí tự")
	private String hocVi;
	@Size(max = 20, message = "Học hàm không được quá 20 kí tự")
	private String hocHam;
	@Size(max = 50, message = "Chuyên môn không được quá 50 kí tự")
	private String chuyenMon;
	private String idKhoa;
	private Long idTaiKhoan;
	private Boolean isEdit = false;
}
