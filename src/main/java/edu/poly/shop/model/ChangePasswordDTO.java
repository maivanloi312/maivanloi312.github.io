package edu.poly.shop.model;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ChangePasswordDTO {
	@NotEmpty(message = "Vui lòng nhập mật khẩu hiện tại")
	private String currentPassword;
	@NotEmpty(message = "Vui lòng nhập mật khẩu mới")
	private String newPassword;
	@NotEmpty(message = "Vui lòng nhập lại mật khẩu mới")
	private String confirmPassword;
}
