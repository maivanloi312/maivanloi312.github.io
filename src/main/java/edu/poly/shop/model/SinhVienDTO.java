package edu.poly.shop.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class SinhVienDTO {
	@Size(max = 10, message = "Mã sinh viên không được quá 10 kí tự")
	@NotEmpty(message = "Mã sinh viên không được để trống")
	private String idSinhVien;
	@Size(max = 50, message = "Họ sinh viên không được quá 50 kí tự")
	@NotEmpty(message = "Họ không được để trống")
	private String ho;
	@Size(max = 10, message = "Tên sinh viên không được quá 10 kí tự")
	@NotEmpty(message = "Tên không được để trống")
	private String ten;
	private Boolean phai;
	@Size(max = 100, message = "Địa chỉ không được quá 100 kí tự")
	private String diaChi;
	private Boolean daNghiHoc;
	private String idLop;
	private Long idTaiKhoan;
	private Boolean isEdit = false;
}
