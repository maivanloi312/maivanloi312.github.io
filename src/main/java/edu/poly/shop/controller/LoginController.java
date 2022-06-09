package edu.poly.shop.controller;

import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import edu.poly.shop.domain.TaiKhoan;
import edu.poly.shop.domain.UserDetailsImpl;
import edu.poly.shop.service.TaiKhoanService;

@Controller
public class LoginController {
	@Autowired
	EntityManagerFactory entityManagerFactory;
	@Autowired
	TaiKhoanService taiKhoanService;
	@Autowired
	BCryptPasswordEncoder passwordEncoder;
	@GetMapping("403")
	public String showAccessDeniedPage() {
		return "site/403";
	}
	@GetMapping("login")
	public String showMyLoginPage() {
		return "login";
	}
	@GetMapping("/")
	public String showHomePage(){
		return "trangchu";
	}
    @GetMapping("changePassword")
    public String showChangePasswordPage() {
        return "site/changePassword";
    }
    @PostMapping("changePassword")
	public ModelAndView changePassword(ModelMap model,
			HttpServletRequest request, HttpServletResponse response,
	        Authentication authentication) throws ServletException{
	    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
	    TaiKhoan taiKhoan = taiKhoanService.getTaiKhoanByUsername(userDetails.getUsername());
	    String oldPassword = request.getParameter("oldPassword");
	    String newPassword = request.getParameter("newPassword");
	    if (oldPassword.equals(newPassword)) {
	        model.addAttribute("message", "Mật khẩu mới phải khác mật khẩu hiện tại");
	        return new ModelAndView("site/changePassword", model);
	    }
	     
	    if (!passwordEncoder.matches(oldPassword, taiKhoan.getPassword())) {
	        model.addAttribute("message", "Mật khẩu hiện tại không chính xác");          
	        return new ModelAndView("site/changePassword", model);
	         
	    } else {
			Session session = entityManagerFactory.createEntityManager().unwrap(Session.class);
			Transaction t = session.beginTransaction();
			try {
				taiKhoan.setPassword(passwordEncoder.encode(newPassword));
				session.update(taiKhoan);
				t.commit();
			} catch (Exception e) {
				t.rollback();
			} finally {
				session.close();
			}
			model.addAttribute("message", "Thay đổi mật khẩu thành công");
			return new ModelAndView("site/changePassword", model);
	    } 
	}
}
