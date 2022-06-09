package edu.poly.shop.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class KhoaDTO{
	@Size(max = 10, message = "Mã khoa không được quá 10 kí tự")
	@NotEmpty(message = "Mã khoa không được để trống")
	private String idKhoa;
	@Size(max = 50, message = "Tên khoa không được quá 50 kí tự")
	@NotEmpty(message = "Tên khoa không được để trống")
	private String tenKhoa;
	private Boolean isEdit = false;
}
