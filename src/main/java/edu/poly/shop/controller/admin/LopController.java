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

import edu.poly.shop.domain.Khoa;
import edu.poly.shop.domain.Lop;
import edu.poly.shop.model.KhoaDTO;
import edu.poly.shop.model.LopDTO;

@Controller
@RequestMapping("admin/dslop")
public class LopController {	
	@Autowired
	EntityManagerFactory entityManagerFactory;
	public Lop getLopByIdLop(String idLop) {
		Session session = entityManagerFactory.createEntityManager().unwrap(Session.class);
		Transaction t = session.beginTransaction();
		try {
			Query query = session.createQuery("from Lop t where t.idLop = :idLop");
			query.setParameter("idLop", idLop);
			Lop entity = (Lop) query.list().get(0);
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
	public Lop getLopByTenLop(String tenLop) {
		Session session = entityManagerFactory.createEntityManager().unwrap(Session.class);
		Transaction t = session.beginTransaction();
		try {
			Query query = session.createQuery("from Lop t where t.tenLop = :tenLop");
			query.setParameter("tenLop", tenLop);
			Lop entity = (Lop) query.list().get(0);
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
	
	@GetMapping("add")
	public String add(Model model) {
		LopDTO dto= new LopDTO();
		dto.setIsEdit(false);
		model.addAttribute("lop", dto);
		return "admin/dslop/addOrEdit";
	}
	@GetMapping("edit/{idLop}")
	public ModelAndView edit(ModelMap model, @PathVariable("idLop") String idLop) {
		Session session = entityManagerFactory.createEntityManager().unwrap(Session.class);
		Transaction t = session.beginTransaction();
		try {
			Query query = session.createQuery("from Lop l where l.idLop = :idLop");
			query.setParameter("idLop", idLop);
			Lop entity = (Lop) query.list().get(0);
			LopDTO dto = new LopDTO();
			if(entity != null) {
				BeanUtils.copyProperties(entity, dto);
				dto.setIdKhoa(entity.getKhoa().getIdKhoa());
				dto.setIsEdit(true);
				model.addAttribute("lop", dto);
				return new ModelAndView("admin/dslop/addOrEdit", model);
			}
			t.commit();
		} catch (Exception e) {
			t.rollback();
		} finally {
			session.close();
		}
		model.addAttribute("message", "Lớp không tồn tại");
		return new ModelAndView("forward:/admin/dslop", model);
	}
	@GetMapping("delete/{idLop}")
	public ModelAndView delete(ModelMap model, @PathVariable("idLop") String idLop) {
		Session session = entityManagerFactory.createEntityManager().unwrap(Session.class);
		Transaction t = session.beginTransaction();
		try {
			Query query = session.createQuery("DELETE FROM Lop l WHERE l.idLop = :idLop");
			query.setParameter("idLop", idLop);
			query.executeUpdate();
			t.commit();
		} catch (Exception e) {
			t.rollback();
		} finally {
			session.close();
		}
		model.addAttribute("message", "Lớp đã được xóa!");
		return new ModelAndView("forward:/admin/dslop/search");
	}
	@PostMapping("saveOrUpdate")
	public ModelAndView saveOrUpdate(ModelMap model,
			@Valid @ModelAttribute("lop") LopDTO dto, BindingResult result) {
		if(getLopByIdLop(dto.getIdLop()) != null && dto.getIsEdit() == false) {
			result.rejectValue("idLop", "dto", "Mã lớp đã tồn tại");
		}
		if(getLopByTenLop(dto.getTenLop()) != null && dto.getIsEdit() == false) {
			result.rejectValue("tenLop", "dto", "Tên lớp đã tồn tại");
		}
		if(dto.getIdKhoa() == null) {
			result.rejectValue("idKhoa", "dto", "Khoa không được để trống");
		}
		if(result.hasErrors()) {
			return new ModelAndView("admin/dslop/addOrEdit");
		}
		Lop entity = new Lop();
		BeanUtils.copyProperties(dto, entity);
		Khoa heDaoTao = new Khoa();
		
		Khoa khoaDaoTao = new Khoa();
		khoaDaoTao.setIdKhoa(dto.getIdKhoa());
		entity.setKhoa(khoaDaoTao);
		
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
		model.addAttribute("message","Lớp đã được lưu!");
		return new ModelAndView("forward:/admin/dslop", model);
	}
	@RequestMapping("")
	public String list(ModelMap model) {
		Session session = entityManagerFactory.createEntityManager().unwrap(Session.class);
		Transaction t = session.beginTransaction();
		try {
			Query<Lop> query = session.createQuery("from Lop");
			List<Lop> list = query.list();
			model.addAttribute("dslop", list);
			t.commit();
		} catch (Exception e) {
			t.rollback();
		} finally {
			session.close();
		}
		return "admin/dslop/list";
	}
	@GetMapping("search")
	public ModelAndView search(ModelMap model,
			@RequestParam(name = "tenLop", required = false) String tenLop) {
		List<Lop> list = null;
		if(StringUtils.hasText(tenLop)) {
			Session session = entityManagerFactory.createEntityManager().unwrap(Session.class);
			Transaction t = session.beginTransaction();
			try {
				Query query = session.createQuery("from Lop l where l.tenLop like :tenLop");
				query.setParameter("tenLop", "%" + tenLop + "%");
				list = query.list();
				if (list.isEmpty()) {
					model.addAttribute("message","Lớp không tồn tại!");
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
				Query<Lop> query = session.createQuery("from Lop");
				list = query.list();
				t.commit();
			} catch (Exception e) {
				t.rollback();
			} finally {
				session.close();
			}
		}
		model.addAttribute("dslop", list);
		return new ModelAndView ("admin/dslop/search", model);
	}
}
