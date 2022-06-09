package edu.poly.shop.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class LopDTO {
	@Size(max = 10, message = "Mã lớp không được quá 10 kí tự")
	@NotEmpty(message = "Mã lớp không được để trống")
	private String idLop;
	@Size(max = 50, message = "Tên lớp không được quá 50 kí tự")
	@NotEmpty(message = "Tên lớp không được để trống")
	private String tenLop;
	@Size(max = 9, message = "Khóa học không được quá 9 kí tự")
	@NotEmpty(message = "Khóa học không được để trống")
	private String khoaHoc;
	private String idKhoa;
	private Boolean isEdit = false;
}
