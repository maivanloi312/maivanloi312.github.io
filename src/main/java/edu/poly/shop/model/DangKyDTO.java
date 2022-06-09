package edu.poly.shop.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class DangKyDTO {
	private Long idLTC;
	private String idSinhVien;
	@Min(value = 0, message = "Điểm phải lớn hơn 0")
	@Max(value = 10, message = "Điểm phải nhỏ hơn 10")
	private Integer diemcc;
	@Min(value = 0, message = "Điểm phải lớn hơn 0")
	@Max(value = 10, message = "Điểm phải nhỏ hơn 10")
	private Float diemgk;
	@Min(value = 0, message = "Điểm phải lớn hơn 0")
	@Max(value = 10, message = "Điểm phải nhỏ hơn 10")
	private Float diemck;
	private Boolean huyDangKy;
	private Boolean isEdit= false;
}
