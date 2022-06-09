package edu.poly.shop.service;

import edu.poly.shop.domain.TaiKhoan;

public interface TaiKhoanService {

	TaiKhoan getTaiKhoanByUsername(String username);

	TaiKhoan getTaiKhoanByEmail(String email);

}
