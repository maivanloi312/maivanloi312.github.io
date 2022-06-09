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
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import edu.poly.shop.domain.GiangVien;
import edu.poly.shop.domain.Khoa;
import edu.poly.shop.domain.LopTinChi;
import edu.poly.shop.domain.MonHoc;
import edu.poly.shop.domain.LopTinChi;
import edu.poly.shop.model.GiangVienDTO;
import edu.poly.shop.model.KhoaDTO;
import edu.poly.shop.model.LopTinChiDTO;
import edu.poly.shop.model.MonHocDTO;

@Controller
@RequestMapping("admin/dsloptinchi")
public class LopTinChiController {	
	@Autowired
	EntityManagerFactory entityManagerFactory;
	
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
	@ModelAttribute("dsgiangvien")
	public List<GiangVienDTO> getDSGiangVien(){
		Session session = entityManagerFactory.createEntityManager().unwrap(Session.class);
		Transaction t = session.beginTransaction();
		try {
			Query<GiangVien> query = session.createQuery("from GiangVien");
			List<GiangVien> list = query.list();
			t.commit();
			return list.stream().map(item->{
				GiangVienDTO dto = new GiangVienDTO();
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
	@ModelAttribute("dsmonhoc")
	public List<MonHocDTO> getDSMonHoc(){
		Session session = entityManagerFactory.createEntityManager().unwrap(Session.class);
		Transaction t = session.beginTransaction();
		try {
			Query<MonHoc> query = session.createQuery("from MonHoc");
			List<MonHoc> list = query.list();
			t.commit();
			return list.stream().map(item->{
				MonHocDTO dto = new MonHocDTO();
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
		LopTinChiDTO dto= new LopTinChiDTO();
		dto.setIsEdit(false);
		model.addAttribute("loptinchi", dto);
		return "admin/dsloptinchi/addOrEdit";
	}
	@GetMapping("edit/{idLTC}")
	public ModelAndView edit(ModelMap model, @PathVariable("idLTC") Long idLTC) {
		Session session = entityManagerFactory.createEntityManager().unwrap(Session.class);
		Transaction t = session.beginTransaction();
		try {
			Query query = session.createQuery("from LopTinChi l where l.idLTC = :idLTC");
			query.setParameter("idLTC", idLTC);
			LopTinChi entity = (LopTinChi) query.list().get(0);
			LopTinChiDTO dto = new LopTinChiDTO();
			if(entity != null) {
				BeanUtils.copyProperties(entity, dto);
				dto.setIdKhoa(entity.getKhoaLopTinChi().getIdKhoa());
				dto.setIdMonHoc(entity.getMonHoc().getIdMonHoc());
				dto.setIdGiangVien(entity.getGiangVien().getIdGiangVien());
				dto.setIsEdit(true);
				model.addAttribute("loptinchi", dto);
				return new ModelAndView("admin/dsloptinchi/addOrEdit", model);
			}
			t.commit();
		} catch (Exception e) {
			t.rollback();
		} finally {
			session.close();
		}
		model.addAttribute("message", "Lớp tín chỉ không tồn tại");
		return new ModelAndView("forward:/admin/dsloptinchi", model);
	}
	@GetMapping("delete/{idLTC}")
	public ModelAndView delete(ModelMap model, @PathVariable("idLTC") Long idLTC) {
		Session session = entityManagerFactory.createEntityManager().unwrap(Session.class);
		Transaction t = session.beginTransaction();
		try {
			Query query = session.createQuery("DELETE FROM Lop l WHERE l.idLTC = :idLTC");
			query.setParameter("idLTC", idLTC);
			query.executeUpdate();
			t.commit();
		} catch (Exception e) {
			t.rollback();
		} finally {
			session.close();
		}
		model.addAttribute("message", "Lớp tín chỉ đã được xóa!");
		return new ModelAndView("forward:/admin/dsloptinchi/search");
	}
	@PostMapping("saveOrUpdate")
	public ModelAndView saveOrUpdate(ModelMap model,
			@Valid @ModelAttribute("loptinchi") LopTinChiDTO dto, BindingResult result) {
		if (dto.getIdKhoa() == null) {
			result.rejectValue("idKhoa", "dto", "Khoa không được để trống");
		}
		if (dto.getIdMonHoc() == null) {
			result.rejectValue("idMonHoc", "dto", "Môn học không được để trống");
		}
		if (dto.getIdGiangVien() == null) {
			result.rejectValue("idGiangVien", "dto", "Giảng viên không được để trống");
		}
		if(result.hasErrors()) {
			return new ModelAndView("admin/dsloptinchi/addOrEdit");
		}
		LopTinChi entity = new LopTinChi();
		BeanUtils.copyProperties(dto, entity);
		Khoa heDaoTao = new Khoa();
		
		Khoa khoa = new Khoa();
		khoa.setIdKhoa(dto.getIdKhoa());
		entity.setKhoaLopTinChi(khoa);
		
		GiangVien giangVien= new GiangVien();
		giangVien.setIdGiangVien(dto.getIdGiangVien());
		entity.setGiangVien(giangVien);
		
		MonHoc monHoc = new MonHoc();
		monHoc.setIdMonHoc(dto.getIdMonHoc());
		entity.setMonHoc(monHoc);
		
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
		model.addAttribute("message","Lớp tín chỉ đã được lưu!");
		return new ModelAndView("forward:/admin/dsloptinchi", model);
	}
	@RequestMapping("")
	public String list(ModelMap model) {
		Session session = entityManagerFactory.createEntityManager().unwrap(Session.class);
		Transaction t = session.beginTransaction();
		try {
			Query<LopTinChi> query = session.createQuery("from LopTinChi");
			List<LopTinChi> list = query.list();
			model.addAttribute("dsloptinchi", list);
			t.commit();
		} catch (Exception e) {
			t.rollback();
		} finally {
			session.close();
		}
		return "admin/dsloptinchi/list";
	}
	@GetMapping("search")
	public ModelAndView search(ModelMap model,
			@RequestParam(name = "tenMonHoc", required = false) String tenMonHoc) {
		List<LopTinChi> list = null;	
		if(StringUtils.hasText(tenMonHoc)) {
			Session session = entityManagerFactory.createEntityManager().unwrap(Session.class);
			Transaction t = session.beginTransaction();
			try {
				Query query = session.createQuery("from LopTinChi s where s.monHoc.tenMonHoc like :tenMonHoc");
				query.setParameter("tenMonHoc", "%" + tenMonHoc + "%");
				list = query.list();
				if (list.isEmpty()) {
					model.addAttribute("message","Lớp tín chỉ không tồn tại!");
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
				Query<LopTinChi> query = session.createQuery("from LopTinChi");
				list = query.list();
				t.commit();
			} catch (Exception e) {
				t.rollback();
			} finally {
				session.close();
			}
		}
		model.addAttribute("dsloptinchi", list);
		return new ModelAndView ("admin/dsloptinchi/search", model);
	}
	@GetMapping("views/{idMonHoc}")
	public String view(ModelMap model, @PathVariable("idMonHoc") String idMonHoc) {
		List<LopTinChi> list = null;
		Session session = entityManagerFactory.createEntityManager().unwrap(Session.class);
		Transaction t = session.beginTransaction();
		try {
			Query query = session.createQuery("from LopTinChi s where s.monHoc.idMonHoc = :idMonHoc");
			query.setParameter("idMonHoc", idMonHoc);
			list = query.list();
			if (list.isEmpty()) {
				model.addAttribute("message","Danh sách lớp tín chỉ trống!");
			}
			t.commit();
		} catch (Exception e) {
			t.rollback();
		} finally {
			session.close();
		}
		model.addAttribute("dsloptinchi", list);
		return "admin/dsloptinchi/search";
	}
	@GetMapping("viewt/{idGiangVien}")
	public String viewt(ModelMap model, @PathVariable("idGiangVien") String idGiangVien) {
		List<LopTinChi> list = null;
		Session session = entityManagerFactory.createEntityManager().unwrap(Session.class);
		Transaction t = session.beginTransaction();
		try {
			Query query = session.createQuery("from LopTinChi s where s.giangVien.idGiangVien = :idGiangVien");
			query.setParameter("idGiangVien", idGiangVien);
			list = query.list();
			if (list.isEmpty()) {
				model.addAttribute("message","Danh sách lớp tín chỉ trống!");
			}
			t.commit();
		} catch (Exception e) {
			t.rollback();
		} finally {
			session.close();
		}
		model.addAttribute("dsloptinchi", list);
		return "admin/dsloptinchi/search";
	}
}
