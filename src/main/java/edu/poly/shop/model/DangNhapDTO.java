package edu.poly.shop.model;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class DangNhapDTO {
	@NotEmpty
	private String idTaiKhoan;
	@NotEmpty		
	private String matKhau;
	private Boolean rememberMe = false;
}
