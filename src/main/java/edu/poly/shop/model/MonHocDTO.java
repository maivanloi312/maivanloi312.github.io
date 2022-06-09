package edu.poly.shop.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class MonHocDTO{	@Size(max = 10, message = "Mã môn học không được quá 10 kí tự")

	@NotEmpty(message = "Mã môn học không được để trống")
	private String idMonHoc;
	@Size(max = 50, message = "Tên môn học không được quá 50 kí tự")
	@NotEmpty(message = "Tên môn học không được để trống")
	private String tenMonHoc;
	@Min(value = 0, message = "Số tiết lý thuyết phải lớn hơn hoặc bằng 0")
	@NotNull(message = "Số tiết lí thuyết không được để trống")
	private Integer soTietLT;
	@Min(value = 0, message = "Số tiết thực hành phải lớn hơn hoặc bằng 0")
	@NotNull(message = "Số tiết thực hành không được để trống")
	private Integer soTietTH;
	private Boolean isEdit = false;
}
