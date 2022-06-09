package edu.poly.shop.controller.admin;

import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.validation.Valid;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.annotations.Cascade;
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
import org.springframework.web.servlet.ModelAndView;

import edu.poly.shop.domain.SinhVien;
import edu.poly.shop.domain.DangKy;
import edu.poly.shop.domain.LopTinChi;
import edu.poly.shop.model.SinhVienDTO;
import edu.poly.shop.model.DangKyDTO;
import edu.poly.shop.model.LopTinChiDTO;

@Controller
@RequestMapping("admin/dsdangky")
public class DangKyController {
	@Autowired
	EntityManagerFactory entityManagerFactory;
	public LopTinChi getLopTinChiByIdLopTinChi(Long idLopTinChi) {
		Session session = entityManagerFactory.createEntityManager().unwrap(Session.class);
		Transaction t = session.beginTransaction();
		try {
			Query query = session.createQuery("from LopTinChi t where t.idLopTinChi = :idLopTinChi");
			query.setParameter("idLopTinChi", idLopTinChi);
			LopTinChi entity = (LopTinChi) query.list().get(0);
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
	@ModelAttribute("dsloptinchi")
	public List<LopTinChiDTO> getDSLopTinChi(){
		Session session = entityManagerFactory.createEntityManager().unwrap(Session.class);
		Transaction t = session.beginTransaction();
		try {
			Query<LopTinChi> query = session.createQuery("from LopTinChi");
			List<LopTinChi> list = query.list();
			t.commit();
			return list.stream().map(item->{
				LopTinChiDTO dto = new LopTinChiDTO();
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
	@ModelAttribute("dssinhvien")
	public List<SinhVienDTO> getDSSinhVien(){
		Session session = entityManagerFactory.createEntityManager().unwrap(Session.class);
		Transaction t = session.beginTransaction();
		try {
			Query<SinhVien> query = session.createQuery("from SinhVien");
			List<SinhVien> list = query.list();
			t.commit();
			return list.stream().map(item->{
				SinhVienDTO dto = new SinhVienDTO();
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
		DangKyDTO dto = new DangKyDTO();
		dto.setIsEdit(false);
		model.addAttribute("dangky", dto);
		return "admin/dsdangky/addOrEdit";
	}
	@GetMapping("edit/{idSinhVien}/{idLTC}")
	public ModelAndView edit(ModelMap model, 
			@PathVariable("idSinhVien") String idSinhVien,
			@PathVariable("idLTC") Long idLTC) {
		Session session = entityManagerFactory.createEntityManager().unwrap(Session.class);
		Transaction t = session.beginTransaction();
		try {
			Query query = session.createQuery("from DangKy t where t.lopTinChi.idLTC = :idLTC and t.sinhVien.idSinhVien = :idSinhVien");
			query.setParameter("idLTC", idLTC);
			query.setParameter("idSinhVien", idSinhVien);
			DangKy entity = (DangKy) query.list().get(0);
			DangKyDTO dto = new DangKyDTO();
			if(entity != null) {
				BeanUtils.copyProperties(entity, dto);
				dto.setIdLTC(entity.getLopTinChi().getIdLTC());
				dto.setIdSinhVien(entity.getSinhVien().getIdSinhVien());
				dto.setIsEdit(true);
				model.addAttribute("dangky", dto);
				return new ModelAndView("admin/dsdangky/addOrEdit", model);
			}
			t.commit();
		} catch (Exception e) {
			t.rollback();
		} finally {
			session.close();
		}
		model.addAttribute("message", "Đăng ký không tồn tại");
		return new ModelAndView("forward:/admin/dsdangky", model);
	}
	@GetMapping("delete/{idSinhVien}/{idLTC}")
	public ModelAndView delete(ModelMap model, 
			@PathVariable("idSinhVien") String idSinhVien,
			@PathVariable("idLTC") Long idLTC) {
		Session session = entityManagerFactory.createEntityManager().unwrap(Session.class);
		Transaction t = session.beginTransaction();
		try {
			Query query = session.createQuery("DELETE FROM DangKy t WHERE t.lopTinChi.idLTC = :idLTC AND t.sinhVien.idSinhVien = :idSinhVien");
			query.setParameter("idLTC", idLTC);
			query.setParameter("idSinhVien", idSinhVien);
			query.executeUpdate();
			t.commit();
		} catch (Exception e) {
			t.rollback();
		} finally {
			session.close();
		}
		model.addAttribute("message", "Đăng ký đã được xóa!");
		return new ModelAndView("forward:/admin/dsdangky/search");
	}
	@PostMapping("saveOrUpdate")
	public ModelAndView saveOrUpdate(ModelMap model,
			@Valid @ModelAttribute("dangky") DangKyDTO dto, BindingResult result) {
		if(dto.getIdLTC() == null) {
			result.rejectValue("idLTC", "dto", "Lớp tín chỉ không được để trống");
		}
		if(dto.getIdSinhVien() == null) {
			result.rejectValue("idSinhVien", "dto", "Sinh viên không được để trống");
		}
		if(getLopTinChiByIdLopTinChi(dto.getIdLTC()) != null 
				&& getSinhVienByIdSinhVien(dto.getIdSinhVien()) != null && dto.getIsEdit() == false) {
			result.rejectValue("idLTC", "dto", "Sinh viên đã đăng ký lớp tín chỉ này");
			result.rejectValue("idSinhVien", "dto", "Sinh viên đã đăng ký lớp tín chỉ này");
		}
		if(result.hasErrors()) {
			return new ModelAndView("admin/dsdangky/addOrEdit");
		}
		DangKy entity = new DangKy();
		BeanUtils.copyProperties(dto, entity);
		
		SinhVien sinhVien = new SinhVien();
		sinhVien.setIdSinhVien(dto.getIdSinhVien());
		entity.setSinhVien(sinhVien);
		
		LopTinChi lopTinChi = new LopTinChi();
		lopTinChi.setIdLTC(dto.getIdLTC());
		entity.setLopTinChi(lopTinChi);
		
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
		model.addAttribute("message","Đăng ký đã được lưu!");
		return new ModelAndView("forward:/admin/dsdangky", model);
	}
	@RequestMapping("")
	public String list(ModelMap model) {
		Session session = entityManagerFactory.createEntityManager().unwrap(Session.class);
		Transaction t = session.beginTransaction();
		try {
			Query<DangKy> query = session.createQuery("from DangKy");
			List<DangKy> list = query.list();
			model.addAttribute("dsdangky", list);
			t.commit();
		} catch (Exception e) {
			t.rollback();
		} finally {
			session.close();
		}
		return "admin/dsdangky/list";
	}
	@GetMapping("search")
	public String search(ModelMap model) {
		Session session = entityManagerFactory.createEntityManager().unwrap(Session.class);
		Transaction t = session.beginTransaction();
		try {
			Query<DangKy> query = session.createQuery("from DangKy");
			List<DangKy> list = query.list();
			model.addAttribute("dsdangky", list);
			t.commit();
		} catch (Exception e) {
			t.rollback();
		} finally {
			session.close();
		}
		return "admin/dsdangky/search";
	}
	@GetMapping("viewr/{idLTC}")
	public String viewr(ModelMap model, @PathVariable("idLTC") Long idLTC) {
		List<DangKy> list = null;
		Session session = entityManagerFactory.createEntityManager().unwrap(Session.class);
		Transaction t = session.beginTransaction();
		try {
			Query query = session.createQuery("from DangKy h where h.lopTinChi.idLTC = :idLTC");
			query.setParameter("idLTC", idLTC);
			list = query.list();
			if (list.isEmpty()) {
				model.addAttribute("message","Danh sách đăng ký trống!");
			}
			t.commit();
		} catch (Exception e) {
			t.rollback();
		} finally {
			session.close();
		}
		model.addAttribute("dsdangky", list);
		return "admin/dsdangky/search";
	}
	@GetMapping("views/{idSinhVien}")
	public String views(ModelMap model, @PathVariable("idSinhVien") String idSinhVien) {
		List<DangKy> list = null;
		Session session = entityManagerFactory.createEntityManager().unwrap(Session.class);
		Transaction t = session.beginTransaction();
		try {
			Query query = session.createQuery("from DangKy h where h.sinhVien.idSinhVien = :idSinhVien");
			query.setParameter("idSinhVien", idSinhVien);
			list = query.list();
			if (list.isEmpty()) {
				model.addAttribute("message","Danh sách đăng ký trống!");
			}
			t.commit();
		} catch (Exception e) {
			t.rollback();
		} finally {
			session.close();
		}
		model.addAttribute("dsdangky", list);
		return "admin/dsdangky/search";
	}
}
