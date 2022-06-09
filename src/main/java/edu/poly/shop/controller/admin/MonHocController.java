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

import edu.poly.shop.domain.MonHoc;
import edu.poly.shop.model.MonHocDTO;

@Controller
@RequestMapping("admin/dsmonhoc")
public class MonHocController {
	@Autowired
	EntityManagerFactory entityManagerFactory;
	public MonHoc getMonHocByIdMonHoc(String idMonHoc) {
		Session session = entityManagerFactory.createEntityManager().unwrap(Session.class);
		Transaction t = session.beginTransaction();
		try {
			Query query = session.createQuery("from MonHoc t where t.idMonHoc = :idMonHoc");
			query.setParameter("idMonHoc", idMonHoc);
			MonHoc entity = (MonHoc) query.list().get(0);
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
	public MonHoc getMonHocByTenMonHoc(String tenMonHoc) {
		Session session = entityManagerFactory.createEntityManager().unwrap(Session.class);
		Transaction t = session.beginTransaction();
		try {
			Query query = session.createQuery("from MonHoc t where t.tenMonHoc = :tenMonHoc");
			query.setParameter("tenMonHoc", tenMonHoc);
			MonHoc entity = (MonHoc) query.list().get(0);
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
		MonHocDTO dto = new MonHocDTO();
		dto.setIsEdit(false);
		model.addAttribute("monhoc", dto);
		return "admin/dsmonhoc/addOrEdit";
	}
	@GetMapping("edit/{idMonHoc}")
	public ModelAndView edit(ModelMap model, @PathVariable("idMonHoc") String idMonHoc) {
		Session session = entityManagerFactory.createEntityManager().unwrap(Session.class);
		Transaction t = session.beginTransaction();
		try {
			Query query = session.createQuery("from MonHoc t where t.idMonHoc = :idMonHoc");
			query.setParameter("idMonHoc", idMonHoc);
			MonHoc entity = (MonHoc) query.list().get(0);
			MonHocDTO dto = new MonHocDTO();
			if(entity != null) {
				BeanUtils.copyProperties(entity, dto);
				dto.setIsEdit(true);
				model.addAttribute("monhoc", dto);
				return new ModelAndView("admin/dsmonhoc/addOrEdit", model);
			}
			t.commit();
		} catch (Exception e) {
			t.rollback();
		} finally {
			session.close();
		}
		model.addAttribute("message", "Môn học không tồn tại");
		return new ModelAndView("forward:/admin/dsmonhoc", model);
	}
	@GetMapping("delete/{idMonHoc}")
	public ModelAndView delete(ModelMap model, @PathVariable("idMonHoc") String idMonHoc) {
		Session session = entityManagerFactory.createEntityManager().unwrap(Session.class);
		Transaction t = session.beginTransaction();
		try {
			Query query = session.createQuery("DELETE FROM MonHoc t WHERE t.idMonHoc = :idMonHoc");
			query.setParameter("idMonHoc", idMonHoc);
			query.executeUpdate();
			t.commit();
		} catch (Exception e) {
			t.rollback();
		} finally {
			session.close();
		}
		model.addAttribute("message", "Môn học đã được xóa!");
		return new ModelAndView("forward:/admin/dsmonhoc/search");
	}
	@PostMapping("saveOrUpdate")
	public ModelAndView saveOrUpdate(ModelMap model,
			@Valid @ModelAttribute("monhoc") MonHocDTO dto, BindingResult result) {
		if(getMonHocByIdMonHoc(dto.getIdMonHoc()) != null && dto.getIsEdit() == false) {
			result.rejectValue("idMonHoc", "dto", "Mã môn học đã tồn tại");
		}
		if(getMonHocByTenMonHoc(dto.getTenMonHoc()) != null && dto.getIsEdit() == false) {
			result.rejectValue("tenMonHoc", "dto", "Tên môn học đã tồn tại");
		}
		if(result.hasErrors()) {
			return new ModelAndView("admin/dsmonhoc/addOrEdit");
		}
		MonHoc entity = new MonHoc();
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
		model.addAttribute("message","Môn học đã được lưu!");
		return new ModelAndView("forward:/admin/dsmonhoc", model);
	}
	@RequestMapping("")
	public String list(ModelMap model) {
		Session session = entityManagerFactory.createEntityManager().unwrap(Session.class);
		Transaction t = session.beginTransaction();
		try {
			Query<MonHoc> query = session.createQuery("from MonHoc");
			List<MonHoc> list = query.list();
			model.addAttribute("dsmonhoc", list);
			t.commit();
		} catch (Exception e) {
			t.rollback();
		} finally {
			session.close();
		}
		return "admin/dsmonhoc/list";
	}
	@GetMapping("search")
	public ModelAndView search(ModelMap model,
			@RequestParam(name = "tenMonHoc", required = false) String tenMonHoc) {
		List<MonHoc> list = null;
		if(StringUtils.hasText(tenMonHoc)) {
			Session session = entityManagerFactory.createEntityManager().unwrap(Session.class);
			Transaction t = session.beginTransaction();
			try {
				Query query = session.createQuery("from MonHoc t where t.tenMonHoc like :tenMonHoc");
				query.setParameter("tenMonHoc", "%" + tenMonHoc + "%");
				list = query.list();
				if (list.isEmpty()) {
					model.addAttribute("message","Môn học không tồn tại!");
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
				Query<MonHoc> query = session.createQuery("from MonHoc");
				list = query.list();
				t.commit();
			} catch (Exception e) {
				t.rollback();
			} finally {
				session.close();
			}
		}
		model.addAttribute("dsmonhoc", list);
		return new ModelAndView ("admin/dsmonhoc/search", model);
	}
}
