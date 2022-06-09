package edu.poly.shop.controller.admin;

import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.validation.Valid;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.util.StringUtils;

import edu.poly.shop.domain.Lop;
import edu.poly.shop.domain.Quyen;
import edu.poly.shop.domain.TaiKhoan;
import edu.poly.shop.model.QuyenDTO;
import edu.poly.shop.model.TaiKhoanDTO;
import edu.poly.shop.service.TaiKhoanService;

@Controller
@RequestMapping("admin/dstaikhoan")
public class TaiKhoanController {
	@Autowired
	EntityManagerFactory entityManagerFactory;
	@Autowired
	TaiKhoanService taiKhoanService;
	@Autowired
	BCryptPasswordEncoder passwordEncoder;
	
	@ModelAttribute("dsquyen")
	public List<QuyenDTO> getDSQuyen(){
		Session session = entityManagerFactory.createEntityManager().unwrap(Session.class);
		Transaction t = session.beginTransaction();
		try {
			Query<Quyen> query = session.createQuery("from Quyen");
			List<Quyen> list = query.list();
			t.commit();
			return list.stream().map(item->{
				QuyenDTO dto = new QuyenDTO();
				BeanUtils.copyProperties(item, dto);
				return dto;
			}).toList();
		} catch (Exception e) {
			t.rollback();
		} finally {
			session.close();
		}
		return null;
	}

	@GetMapping("add")
	public String add(Model model) {
		TaiKhoanDTO dto = new TaiKhoanDTO();
		dto.setIsEdit(false);
		model.addAttribute("taikhoan", dto);
		return "admin/dstaikhoan/addOrEdit";
	}
	@GetMapping("edit/{idTaiKhoan}")
	public ModelAndView edit(ModelMap model, @PathVariable("idTaiKhoan") Long idTaiKhoan) {	
		Session session = entityManagerFactory.createEntityManager().unwrap(Session.class);
		Transaction t = session.beginTransaction();
		try {
			Query query = session.createQuery("from TaiKhoan t where t.idTaiKhoan = :idTaiKhoan");
			query.setParameter("idTaiKhoan", idTaiKhoan);
			TaiKhoan entity = (TaiKhoan) query.list().get(0);
			TaiKhoanDTO dto = new TaiKhoanDTO();
			if(entity != null) {
				BeanUtils.copyProperties(entity, dto);
				dto.setIdQuyen(entity.getQuyen().getIdQuyen());
				dto.setIsEdit(true);
				model.addAttribute("taikhoan", dto);
				return new ModelAndView("admin/dstaikhoan/addOrEdit", model);
			}
			t.commit();
		} catch (Exception e) {
			t.rollback();
		} finally {
			session.close();
		}
		model.addAttribute("message", "Tài khoản không tồn tại");
		return new ModelAndView("forward:/admin/dstaikhoan", model);
	}
	@GetMapping("delete/{idTaiKhoan}")
	public ModelAndView delete(ModelMap model, @PathVariable("idTaiKhoan") Long idTaiKhoan) {
		Session session = entityManagerFactory.createEntityManager().unwrap(Session.class);
		Transaction t = session.beginTransaction();
		try {
			Query query = session.createQuery("DELETE FROM TaiKhoan t WHERE t.idTaiKhoan = :idTaiKhoan");
			query.setParameter("idTaiKhoan", idTaiKhoan);
			query.executeUpdate();
			t.commit();
		} catch (Exception e) {
			t.rollback();
		} finally {
			session.close();
		}
		model.addAttribute("message", "Tài khoản đã được xóa!");
		return new ModelAndView("forward:/admin/dstaikhoan/search");
	}
	
	@PostMapping("saveOrUpdate")
	public ModelAndView saveOrUpdate(ModelMap model,
			@Valid @ModelAttribute("taikhoan") TaiKhoanDTO dto, BindingResult result) {
		if(taiKhoanService.getTaiKhoanByUsername(dto.getUsername()) != null && dto.getIsEdit() == false) {
			result.rejectValue("username", "dto", "Tên tài khoản đã tồn tại");
		}
		if (dto.getIdQuyen() == null) {
			result.rejectValue("idQuyen", "dto", "Quyền không được để trống");
		}
		if(!dto.getPassword().isEmpty() && dto.getPassword().length() <3 && dto.getIsEdit() == true) {
			result.rejectValue("password", "dto", "Mật khẩu tối thiểu 3 kí tự");
		}
		if(dto.getPassword().length() < 3 && dto.getIsEdit() == false) {
			result.rejectValue("password", "dto", "Mật khẩu tối thiểu 3 kí tự");
		}
		if(taiKhoanService.getTaiKhoanByEmail(dto.getEmail()) != null && dto.getIsEdit() == false){
			result.rejectValue("email", "dto", "Email đã tồn tại");
		}
		if(result.hasErrors()) {
			return new ModelAndView("admin/dstaikhoan/addOrEdit");
		}
		TaiKhoan entity = new TaiKhoan();
		BeanUtils.copyProperties(dto, entity);
		Quyen quyen = new Quyen();
		quyen.setIdQuyen(dto.getIdQuyen());
		entity.setQuyen(quyen);
		
		Session session = entityManagerFactory.createEntityManager().unwrap(Session.class);
		Transaction t = session.beginTransaction();
		try {
			TaiKhoan taiKhoan = taiKhoanService.getTaiKhoanByUsername(entity.getUsername());
			if(taiKhoan != null) {
				if(entity.getPassword().isEmpty()) {
					entity.setPassword(taiKhoan.getPassword());
				}else {
					entity.setPassword(passwordEncoder.encode(entity.getPassword()));
				}
			}
			else {
				entity.setPassword(passwordEncoder.encode(entity.getPassword()));
			}
			session.saveOrUpdate(entity);
			t.commit();
		} catch (Exception e) {
			t.rollback();
		} finally {
			session.close();
		}
		model.addAttribute("message","Tài khoản đã được lưu!");
		return new ModelAndView("forward:/admin/dstaikhoan", model);
	}
	@RequestMapping("")
	public String list(ModelMap model) {
		Session session = entityManagerFactory.createEntityManager().unwrap(Session.class);
		Transaction t = session.beginTransaction();
		try {
			Query<Lop> query = session.createQuery("from TaiKhoan");
			List<Lop> list = query.list();
			model.addAttribute("dstaikhoan", list);
			t.commit();
		} catch (Exception e) {
			t.rollback();
		} finally {
			session.close();
		}
		return "admin/dstaikhoan/list";
	}
	@GetMapping("search")
	public ModelAndView search(ModelMap model,
			@RequestParam(name = "username", required = false) String username) {
		List<TaiKhoan> list = null;
		if(StringUtils.hasText(username)) {
			Session session = entityManagerFactory.createEntityManager().unwrap(Session.class);
			Transaction t = session.beginTransaction();
			try {
				Query query = session.createQuery("from TaiKhoan t where t.username like :username");
				query.setParameter("username", "%" + username + "%");
				list = query.list();
				if (list.isEmpty()) {
					model.addAttribute("message","Tài khoản không tồn tại!");
				}
				t.commit();
			} catch (Exception e) {
				t.rollback();
			} finally {
				session.close();
			}
		}else {
			Session session = entityManagerFactory.createEntityManager().unwrap(Session.class);
			Transaction t = session.beginTransaction();
			try {
				Query<TaiKhoan> query = session.createQuery("from TaiKhoan");
				list = query.list();
				t.commit();
			} catch (Exception e) {
				t.rollback();
			} finally {
				session.close();
			}
		}
		model.addAttribute("dstaikhoan", list);
		return new ModelAndView ("admin/dstaikhoan/search", model);
	}
}
