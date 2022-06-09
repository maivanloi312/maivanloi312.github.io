package edu.poly.shop.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class LopTinChiDTO {
	private Long idLTC;
	@Size(max = 9, message = "Niên khóa không được quá 9 kí tự")
	@NotEmpty(message = "Niên khóa không được để trống")
	private String nienKhoa;
	@Min(value = 1, message = "Học kì tối thiểu là 1")
	@Max(value = 4, message = "Học kì tối đa là 4")
	@NotNull(message = "Học kỳ không được để trống")
	private Integer hocKy;
	@Min(value = 0, message = "Số sinh viên tối thiểu là 0")
	@NotNull(message = "Số sinh viên tối thiểu không được để trống")
	private Integer soSVToiThieu;
	@Min(value = 1, message = "Nhóm tối thiểu là 1")
	@NotNull(message = "Nhóm không được để trống")
	private Integer nhom;
	private Boolean huyLop;
	private String idMonHoc;
	private String idGiangVien;
	private String idKhoa;
	private Boolean isEdit = false;
}
