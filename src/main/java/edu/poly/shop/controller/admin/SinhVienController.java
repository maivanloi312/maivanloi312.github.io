package edu.poly.shop.controller.admin;

import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.validation.Valid;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

import edu.poly.shop.domain.TaiKhoan;
import edu.poly.shop.domain.Lop;
import edu.poly.shop.domain.SinhVien;
import edu.poly.shop.model.TaiKhoanDTO;
import edu.poly.shop.model.LopDTO;
import edu.poly.shop.model.SinhVienDTO;

@Controller
@RequestMapping("admin/dssinhvien")
public class SinhVienController {
	@Autowired
	EntityManagerFactory entityManagerFactory;
	public SinhVien getSinhVienByIdSinhVien(String idSinhVien) {
		Session session = entityManagerFactory.createEntityManager().unwrap(Session.class);
		Transaction t = session.beginTransaction();
		try {
			Query query = session.createQuery("from SinhVien t where t.idSinhVien = :idSinhVien");
			query.setParameter("idSinhVien", idSinhVien);
			SinhVien entity = (SinhVien) query.list().get(0);
			if(entity != null) {
				return entity;
				}
			t.commit();
		} catch (Exception e) {
			t.rollback();
		} finally {
			session.close();
		}
		return null;
	}
	public SinhVien getSinhVienByTenSinhVien(String tenSinhVien) {
		Session session = entityManagerFactory.createEntityManager().unwrap(Session.class);
		Transaction t = session.beginTransaction();
		try {
			Query query = session.createQuery("from SinhVien t where t.tenSinhVien = :tenSinhVien");
			query.setParameter("tenSinhVien", tenSinhVien);
			SinhVien entity = (SinhVien) query.list().get(0);
			if(entity != null) {
				return entity;
			}
			t.commit();
		} catch (Exception e) {
			t.rollback();
		} finally {
			session.close();
		}
		return null;
	}
	@ModelAttribute("dstaikhoan")
	public List<TaiKhoanDTO> getDSTaiKhoan(){
		Session session = entityManagerFactory.createEntityManager().unwrap(Session.class);
		Transaction t = session.beginTransaction();
		try {
			Query<TaiKhoan> query = session.createQuery("from TaiKhoan where quyen.tenQuyen = 'ROLE_USER'"
					+ "and idTaiKhoan not in (select taiKhoanSinhVien.idTaiKhoan from SinhVien)");
			List<TaiKhoan> list = query.list();
			t.commit();
			return list.stream().map(item->{
				TaiKhoanDTO dto = new TaiKhoanDTO();
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
	@ModelAttribute("dslop")
	public List<LopDTO> getDSLop(){
		Session session = entityManagerFactory.createEntityManager().unwrap(Session.class);
		Transaction t = session.beginTransaction();
		try {
			Query<Lop> query = session.createQuery("from Lop");
			List<Lop> list = query.list();
			t.commit();
			return list.stream().map(item->{
				LopDTO dto = new LopDTO();
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
	@PostMapping("saveOrUpdate")
	public ModelAndView saveOrUpdate(ModelMap model,
			@Valid @ModelAttribute("sinhvien") SinhVienDTO dto, BindingResult result) {
		if(getSinhVienByIdSinhVien(dto.getIdSinhVien()) != null && dto.getIsEdit() == false) {
			result.rejectValue("idSinhVien", "dto", "Mã sinh viên đã tồn tại");
		}
		if (dto.getIdLop() == null) {
			result.rejectValue("idLop", "dto", "Lớp không được để trống");
		}
		if (dto.getIdTaiKhoan() == null) {
			result.rejectValue("idTaiKhoan", "dto", "Tài khoản không được để trống");
		}
		if(result.hasErrors()) {
			return new ModelAndView("admin/dssinhvien/addOrEdit");
		}
		SinhVien entity = new SinhVien();
		BeanUtils.copyProperties(dto, entity);
		Lop lop = new Lop();
		lop.setIdLop(dto.getIdLop());
		entity.setLop(lop);
		
		TaiKhoan taiKhoan = new TaiKhoan();
		taiKhoan.setIdTaiKhoan(dto.getIdTaiKhoan());
		entity.setTaiKhoanSinhVien(taiKhoan);
		
		Session session = entityManagerFactory.createEntityManager().unwrap(Session.class);
		Transaction t = session.beginTransaction();
		try {
			session.saveOrUpdate(entity);
			t.commit();
		} catch (Exception e) {
			t.rollback();
		} finally {
			session.close();
		}
		model.addAttribute("message","Sinh viên đã được lưu!");
		return new ModelAndView("forward:/admin/dssinhvien", model);
	}
	@GetMapping("add")
	public String add(Model model) {
		SinhVienDTO dto = new SinhVienDTO();
		dto.setIsEdit(false);
		model.addAttribute("sinhvien", dto);
		return "admin/dssinhvien/addOrEdit";
	}
	@GetMapping("edit/{idSinhVien}")
	public ModelAndView edit(ModelMap model, @PathVariable("idSinhVien") String idSinhVien) {
		Session session = entityManagerFactory.createEntityManager().unwrap(Session.class);
		Transaction t = session.beginTransaction();
		try {
			Query query = session.createQuery("from SinhVien s where s.idSinhVien = :idSinhVien");
			query.setParameter("idSinhVien", idSinhVien);
			SinhVien entity= (SinhVien) query.list().get(0);
			SinhVienDTO dto = new SinhVienDTO();
			if(entity != null) {
				BeanUtils.copyProperties(entity, dto);
				dto.setIdLop(entity.getLop().getIdLop());
				dto.setIdTaiKhoan(entity.getTaiKhoanSinhVien().getIdTaiKhoan());
				dto.setIsEdit(true);
				model.addAttribute("sinhvien", dto);
				return new ModelAndView("admin/dssinhvien/addOrEdit", model);
			}
			t.commit();
		} catch (Exception e) {
			t.rollback();
		} finally {
			session.close();
		}
		model.addAttribute("message", "Sinh viên không tồn tại");
		return new ModelAndView("forward:/admin/dssinhvien", model);
	}
	@GetMapping("delete/{idSinhVien}")
	public ModelAndView delete(ModelMap model, @PathVariable("idSinhVien") String idSinhVien) {
		Session session = entityManagerFactory.createEntityManager().unwrap(Session.class);
		Transaction t = session.beginTransaction();
		try {
			Query query = session.createQuery("DELETE FROM SinhVien s WHERE s.idSinhVien = :idSinhVien");
			query.setParameter("idSinhVien", idSinhVien);
			query.executeUpdate();
			t.commit();
		} catch (Exception e) {
			t.rollback();
		} finally {
			session.close();
		}
		model.addAttribute("message", "Sinh viên đã được xóa!");
		return new ModelAndView("forward:/admin/dssinhvien/search");
	}

	@RequestMapping("")
	public String list(ModelMap model) {
		Session session = entityManagerFactory.createEntityManager().unwrap(Session.class);
		Transaction t = session.beginTransaction();
		try {
			Query<SinhVien> query = session.createQuery("from SinhVien");
			List<SinhVien> list = query.list();
			model.addAttribute("dssinhvien", list);
			t.commit();
		} catch (Exception e) {
			t.rollback();
		} finally {
			session.close();
		}
		return "admin/dssinhvien/list";
	}
	@GetMapping("search")
	public ModelAndView search(ModelMap model,
			@RequestParam(name = "tenSinhVien", required = false) String tenSinhVien) {
		List<SinhVien> list = null;	
		if(StringUtils.hasText(tenSinhVien)) {
			Session session = entityManagerFactory.createEntityManager().unwrap(Session.class);
			Transaction t = session.beginTransaction();
			try {
				Query query = session.createQuery("from SinhVien s where s.tenSinhVien like :tenSinhVien");
				query.setParameter("tenSinhVien", "%" + tenSinhVien + "%");
				list = query.list();
				if (list.isEmpty()) {
					model.addAttribute("message","Sinh viên không tồn tại!");
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
				Query<SinhVien> query = session.createQuery("from SinhVien");
				list = query.list();
				t.commit();
			} catch (Exception e) {
				t.rollback();
			} finally {
				session.close();
			}
		}
		model.addAttribute("dssinhvien", list);
		return new ModelAndView ("admin/dssinhvien/search", model);
	}
	@GetMapping("view/{idLop}")
	public String view(ModelMap model, @PathVariable("idLop") String idLop) {
		List<SinhVien> list = null;
		Session session = entityManagerFactory.createEntityManager().unwrap(Session.class);
		Transaction t = session.beginTransaction();
		try {
			Query query = session.createQuery("from SinhVien s where s.lop.idLop = :idLop");
			query.setParameter("idLop", idLop);
			list = query.list();
			if (list.isEmpty()) {
				model.addAttribute("message","Danh sách sinh viên trống!");
			}
			t.commit();
		} catch (Exception e) {
			t.rollback();
		} finally {
			session.close();
		}
		model.addAttribute("dssinhvien", list);
		return "admin/dssinhvien/search";
	}
}
