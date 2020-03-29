package wrss.rest.dao;

/*
import javax.persistence.EntityManagerFactory;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;

import wrss.rest.entity.UserEntity;
*/
import org.springframework.stereotype.Repository;

@Repository
public class UserDao {

	/* HIBERNATE DAO CLASS WHICH USE SESSION FACTORY BUILD FOR THIS PROJECT BEFORE IMPLEMENT SPRING DATA JPA
	
	@Autowired
	private EntityManagerFactory entityManager;
	
	private SessionFactory sessionFactory = entityManager.unwrap(SessionFactory.class);

	public UserEntity findUserByEmail(String email) {
		
		Session session = sessionFactory.getCurrentSession();
		Query<UserEntity> query = session.createQuery("from UserEntity where email=:mail", UserEntity.class);
		query.setParameter("mail", email);
		
		UserEntity userEntity = null;
		try {
			userEntity = query.getSingleResult();
		} catch (Exception exc) {
			userEntity = null;
		}
		
		return userEntity;
	}
	
	public UserEntity findUserByUsername(String username) {
		
		Session session = entityManager.unwrap(Session.class);
		Query<UserEntity> query = session.createQuery("from UserEntity where username=:name", UserEntity.class);
		query.setParameter("name", username);
		
		UserEntity userEntity = null;
		try {
			userEntity = query.getSingleResult();
		} catch (Exception exc) {
			userEntity = null;
		}
		
		return userEntity;
	}
	
	public UserEntity createUser(UserEntity userEntity) {
		
		Session session = entityManager.unwrap(Session.class);
		int id = (int) session.save(userEntity);
		UserEntity savedEntity = session.get(UserEntity.class, id);
		
		return savedEntity;
	}
	*/
}
