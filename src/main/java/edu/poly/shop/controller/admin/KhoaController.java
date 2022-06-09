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
import edu.poly.shop.domain.Khoa;
import edu.poly.shop.model.KhoaDTO;

@Controller
@RequestMapping("admin/dskhoa")
public class KhoaController {
	@Autowired
	EntityManagerFactory entityManagerFactory;

	public Khoa getKhoaByIdKhoa(String idKhoa) {
		Session session = entityManagerFactory.createEntityManager().unwrap(Session.class);
		Transaction t = session.beginTransaction();
		try {
			Query query = session.createQuery("from Khoa t where t.idKhoa = :idKhoa");
			query.setParameter("idKhoa", idKhoa);
			Khoa entity = (Khoa) query.list().get(0);
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
	public Khoa getKhoaByTenKhoa(String tenKhoa) {
		Session session = entityManagerFactory.createEntityManager().unwrap(Session.class);
		Transaction t = session.beginTransaction();
		try {
			Query query = session.createQuery("from Khoa t where t.tenKhoa = :tenKhoa");
			query.setParameter("tenKhoa", tenKhoa);
			Khoa entity = (Khoa) query.list().get(0);
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
	@GetMapping("add")
	public String add(Model model) {
		KhoaDTO dto = new KhoaDTO();
		dto.setIsEdit(false);
		model.addAttribute("khoa", dto);
		return "admin/dskhoa/addOrEdit";
	}
	@GetMapping("edit/{idKhoa}")
	public ModelAndView edit(ModelMap model, @PathVariable("idKhoa") String idKhoa) {
		Session session = entityManagerFactory.createEntityManager().unwrap(Session.class);
		Transaction t = session.beginTransaction();
		try {
			Query query = session.createQuery("from Khoa k where k.idKhoa = :idKhoa");
			query.setParameter("idKhoa", idKhoa);
			Khoa entity = (Khoa) query.list().get(0);
			KhoaDTO dto = new KhoaDTO();
			if(entity != null) {
				BeanUtils.copyProperties(entity, dto);
				dto.setIsEdit(true);
				model.addAttribute("khoa", dto);
				return new ModelAndView("admin/dskhoa/addOrEdit", model);
			}
			t.commit();
		} catch (Exception e) {
			t.rollback();
		} finally {
			session.close();
		}
		model.addAttribute("message", "Khoa không tồn tại");
		return new ModelAndView("forward:/admin/dskhoa", model);
	}
	@GetMapping("delete/{idKhoa}")
	public ModelAndView delete(ModelMap model, @PathVariable("idKhoa") String idKhoa) {
		Session session = entityManagerFactory.createEntityManager().unwrap(Session.class);
		Transaction t = session.beginTransaction();
		try {
			Query query = session.createQuery("DELETE FROM Khoa k WHERE k.idKhoa = :idKhoa");
			query.setParameter("idKhoa", idKhoa);
			query.executeUpdate();
			t.commit();
		} catch (Exception e) {
			t.rollback();
		} finally {
			session.close();
		}
		model.addAttribute("message", "Khoa đã được xóa!");
		return new ModelAndView("forward:/admin/dskhoa/search");
	}
	@PostMapping("saveOrUpdate")
	public ModelAndView saveOrUpdate(ModelMap model,
			@Valid @ModelAttribute("khoa") KhoaDTO dto, BindingResult result) {
		if(getKhoaByIdKhoa(dto.getIdKhoa()) != null && dto.getIsEdit() == false) {
			result.rejectValue("idKhoa", "dto", "Mã khoa đã tồn tại");
		}
		if(getKhoaByTenKhoa(dto.getTenKhoa()) != null && dto.getIsEdit() == false) {
			result.rejectValue("tenKhoa", "dto", "Tên khoa đã tồn tại");
		}
		if(result.hasErrors()) {
			return new ModelAndView("admin/dskhoa/addOrEdit");
		}
		Khoa entity = new Khoa();
		BeanUtils.copyProperties(dto, entity);
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
		model.addAttribute("message","Khoa đã được lưu!");
		return new ModelAndView("forward:/admin/dskhoa", model);
	}
	@RequestMapping("")
	public String list(ModelMap model) {
		Session session = entityManagerFactory.createEntityManager().unwrap(Session.class);
		Transaction t = session.beginTransaction();
		try {
			Query<Khoa> query = session.createQuery("from Khoa");
			List<Khoa> list = query.list();
			model.addAttribute("dskhoa", list);
			t.commit();
		} catch (Exception e) {
			t.rollback();
		} finally {
			session.close();
		}
		return "admin/dskhoa/list";
	}
	@GetMapping("search")
	public ModelAndView search(ModelMap model,
			@RequestParam(name = "tenKhoa", required = false) String tenKhoa) {
		List<Khoa> list = null;
		if(StringUtils.hasText(tenKhoa)) {
			Session session = entityManagerFactory.createEntityManager().unwrap(Session.class);
			Transaction t = session.beginTransaction();
			try {
				Query query = session.createQuery("from Khoa h where h.tenKhoa like :tenKhoa");
				query.setParameter("tenKhoa", "%" + tenKhoa + "%");
				list = query.list();
				if (list.isEmpty()) {
					model.addAttribute("message","Khoa không tồn tại!");
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
				Query<Khoa> query = session.createQuery("from Khoa");
				list = query.list();
				t.commit();
			} catch (Exception e) {
				t.rollback();
			} finally {
				session.close();
			}
		}
		model.addAttribute("dskhoa", list);
		return new ModelAndView ("admin/dskhoa/search", model);
	}
}
