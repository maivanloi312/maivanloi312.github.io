package edu.poly.shop.service.impl;

import javax.persistence.EntityManagerFactory;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import edu.poly.shop.domain.TaiKhoan;
import edu.poly.shop.service.TaiKhoanService;

@Service
public class TaiKhoanServiceImpl implements TaiKhoanService {
	@Autowired
	EntityManagerFactory entityManagerFactory;
	@Autowired
	BCryptPasswordEncoder passwordEncoder;
	@Override
	public TaiKhoan getTaiKhoanByEmail(String email) {
		Session session = entityManagerFactory.createEntityManager().unwrap(Session.class);
		Transaction t = session.beginTransaction();
		try {
			Query query = session.createQuery("from TaiKhoan t where t.email = :email");
			query.setParameter("email", email);
			TaiKhoan entity = (TaiKhoan) query.list().get(0);
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
	@Override
	public TaiKhoan getTaiKhoanByUsername(String username) {
		Session session = entityManagerFactory.createEntityManager().unwrap(Session.class);
		Transaction t = session.beginTransaction();
		try {
			Query query = session.createQuery("from TaiKhoan t where t.username = :username");
			query.setParameter("username", username);
			TaiKhoan entity = (TaiKhoan) query.list().get(0);
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
}
