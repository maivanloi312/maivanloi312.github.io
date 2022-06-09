package edu.poly.shop.model;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class TaiKhoanDTO {
	private Long idTaiKhoan;
	@Size(min = 3, max = 10, message = "Tên tài khoản phải từ 3-10 kí tự")
	private String username;
	private String password;
	@NotEmpty(message = "Email không được để trống")
	@Email(message = "Email không hợp lệ")
	private String email;
	private Boolean isEdit = false;
	@Valid
	private Long idQuyen;
}
