package wrss.rest.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import wrss.rest.entity.UserDetailsEntity;
import wrss.rest.entity.UserEntity;

@Repository
public interface UserDetailsRepository extends CrudRepository<UserDetailsEntity, Integer> {
	UserDetailsEntity findByUser(UserEntity user);

}
