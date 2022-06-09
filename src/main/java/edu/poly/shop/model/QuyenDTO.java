package edu.poly.shop.model;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class QuyenDTO{
	private Long idQuyen;
	@NotEmpty
	private String tenQuyen;
	private Boolean isEdit = false;
}
