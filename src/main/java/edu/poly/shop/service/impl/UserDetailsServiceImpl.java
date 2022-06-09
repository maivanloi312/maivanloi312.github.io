package edu.poly.shop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import edu.poly.shop.domain.TaiKhoan;
import edu.poly.shop.domain.UserDetailsImpl;
import edu.poly.shop.service.TaiKhoanService;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private TaiKhoanService taiKhoanService;
     
    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        TaiKhoan taiKhoan = taiKhoanService.getTaiKhoanByUsername(username);
         
        if (taiKhoan == null) {
            throw new UsernameNotFoundException("Người dùng không tồn tại");
        }
         
        return new UserDetailsImpl(taiKhoan);
    }
    
}
