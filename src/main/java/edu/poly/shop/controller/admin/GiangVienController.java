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

import edu.poly.shop.domain.GiangVien;
import edu.poly.shop.domain.Lop;
import edu.poly.shop.domain.TaiKhoan;
import edu.poly.shop.domain.Khoa;
import edu.poly.shop.model.GiangVienDTO;
import edu.poly.shop.model.KhoaDTO;
import edu.poly.shop.model.TaiKhoanDTO;

@Controller
@RequestMapping("admin/dsgiangvien")
public class GiangVienController {
	@Autowired
	EntityManagerFactory entityManagerFactory;
	public GiangVien getGiangVienByIdGiangVien(String idGiangVien) {
		Session session = entityManagerFactory.createEntityManager().unwrap(Session.class);
		Transaction t = session.beginTransaction();
		try {
			Query query = session.createQuery("from GiangVien t where t.idGiangVien = :idGiangVien");
			query.setParameter("idGiangVien", idGiangVien);
			GiangVien entity = (GiangVien) query.list().get(0);
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
	public GiangVien getGiangVienByTenGiangVien(String tenGiangVien) {
		Session session = entityManagerFactory.createEntityManager().unwrap(Session.class);
		Transaction t = session.beginTransaction();
		try {
			Query query = session.createQuery("from GiangVien t where t.tenGiangVien = :tenGiangVien");
			query.setParameter("tenGiangVien", tenGiangVien);
			GiangVien entity = (GiangVien) query.list().get(0);
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
	@ModelAttribute("dskhoa")
	public List<KhoaDTO> getDSKhoa(){
		Session session = entityManagerFactory.createEntityManager().unwrap(Session.class);
		Transaction t = session.beginTransaction();
		try {
			Query<Khoa> query = session.createQuery("from Khoa");
			List<Khoa> list = query.list();
			t.commit();
			return list.stream().map(item->{
				KhoaDTO dto = new KhoaDTO();
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
	@ModelAttribute("dstaikhoan")
	public List<TaiKhoanDTO> getDSTaiKhoan(){
		Session session = entityManagerFactory.createEntityManager().unwrap(Session.class);
		Transaction t = session.beginTransaction();
		try {
			Query<TaiKhoan> query = session.createQuery("from TaiKhoan where quyen.tenQuyen = 'ROLE_AUTHOR'"
					+ "and idTaiKhoan not in (select taiKhoanGiangVien.idTaiKhoan from GiangVien)");
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
	@PostMapping("saveOrUpdate")
	public ModelAndView saveOrUpdate(ModelMap model,
			@Valid @ModelAttribute("giangvien") GiangVienDTO dto, BindingResult result) {
		if(getGiangVienByIdGiangVien(dto.getIdGiangVien()) != null && dto.getIsEdit() == false) {
			result.rejectValue("idGiangVien", "dto", "Mã giảng viên đã tồn tại");
		}
		if (dto.getIdKhoa() == null) {
			result.rejectValue("idKhoa", "dto", "Khoa không được để trống");
		}
		if (dto.getIdTaiKhoan() == null) {
			result.rejectValue("idTaiKhoan", "dto", "Tài khoản không được để trống");
		}
		if(result.hasErrors()) {
			return new ModelAndView("admin/dsgiangvien/addOrEdit");
		}
		GiangVien entity = new GiangVien();
		BeanUtils.copyProperties(dto, entity);
		Khoa khoa = new Khoa();
		khoa.setIdKhoa(dto.getIdKhoa());
		entity.setKhoaGiangVien(khoa);

		TaiKhoan taiKhoan = new TaiKhoan();
		taiKhoan.setIdTaiKhoan(dto.getIdTaiKhoan());
		entity.setTaiKhoanGiangVien(taiKhoan);
		
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
		model.addAttribute("message","Giảng viên đã được lưu!");
		return new ModelAndView("forward:/admin/dsgiangvien", model);
	}
	@RequestMapping("")
	public String list(ModelMap model) {
		Session session = entityManagerFactory.createEntityManager().unwrap(Session.class);
		Transaction t = session.beginTransaction();
		try {
			Query<Lop> query = session.createQuery("from GiangVien");
			List<Lop> list = query.list();
			model.addAttribute("dsgiangvien", list);
			t.commit();
		} catch (Exception e) {
			t.rollback();
		} finally {
			session.close();
		}
		return "admin/dsgiangvien/list";
	}
	@GetMapping("add")
	public String add(Model model) {
		GiangVienDTO dto = new GiangVienDTO();
		dto.setIsEdit(false);
		model.addAttribute("giangvien", dto);
		return "admin/dsgiangvien/addOrEdit";
	}
	@GetMapping("edit/{idGiangVien}")
	public ModelAndView edit(ModelMap model, @PathVariable("idGiangVien") String idGiangVien) {
		Session session = entityManagerFactory.createEntityManager().unwrap(Session.class);
		Transaction t = session.beginTransaction();
		try {
			Query query = session.createQuery("from GiangVien g where g.idGiangVien = :idGiangVien");
			query.setParameter("idGiangVien", idGiangVien);
			GiangVien entity = (GiangVien) query.list().get(0);
			GiangVienDTO dto = new GiangVienDTO();
			if(entity != null) {
				BeanUtils.copyProperties(entity, dto);
				dto.setIdKhoa(entity.getKhoaGiangVien().getIdKhoa());
				dto.setIdTaiKhoan(entity.getTaiKhoanGiangVien().getIdTaiKhoan());
				dto.setIsEdit(true);
				model.addAttribute("giangvien", dto);
				return new ModelAndView("admin/dsgiangvien/addOrEdit", model);
			}
			t.commit();
		} catch (Exception e) {
			t.rollback();
		} finally {
			session.close();
		}
		model.addAttribute("message", "Giảng viên không tồn tại");
		return new ModelAndView("forward:/admin/dsgiangvien", model);
	}
	@GetMapping("delete/{idGiangVien}")
	public ModelAndView delete(ModelMap model, @PathVariable("idGiangVien") String idGiangVien) {
		Session session = entityManagerFactory.createEntityManager().unwrap(Session.class);
		Transaction t = session.beginTransaction();
		try {
			Query query = session.createQuery("DELETE FROM GiangVien g WHERE g.idGiangVien = :idGiangVien");
			query.setParameter("idGiangVien", idGiangVien);
			query.executeUpdate();
			t.commit();
		} catch (Exception e) {
			t.rollback();
		} finally {
			session.close();
		}
		model.addAttribute("message", "Giảng viên đã được xóa!");
		return new ModelAndView("forward:/admin/dsgiangvien/search");
	}
	@GetMapping("search")
	public ModelAndView search(ModelMap model,
			@RequestParam(name = "tenGiangVien", required = false) String tenGiangVien) {
		List<GiangVien> list = null;
		if(StringUtils.hasText(tenGiangVien)) {
			Session session = entityManagerFactory.createEntityManager().unwrap(Session.class);
			Transaction t = session.beginTransaction();
			try {
				Query query = session.createQuery("from GiangVien g where g.tenGiangVien like :tenGiangVien");
				query.setParameter("tenGiangVien", "%" + tenGiangVien + "%");
				list = query.list();
				if (list.isEmpty()) {
					model.addAttribute("message","Giảng viên không tồn tại!");
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
				Query<GiangVien> query = session.createQuery("from GiangVien");
				list = query.list();
				t.commit();
			} catch (Exception e) {
				t.rollback();
			} finally {
				session.close();
			}
		}
		model.addAttribute("dsgiangvien", list);
		return new ModelAndView("admin/dsgiangvien/search", model);
	}
}
