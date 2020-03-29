package wrss.rest.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import wrss.rest.entity.UserEntity;

@Repository
public interface UserRepository extends PagingAndSortingRepository<UserEntity, Integer> {
	UserEntity findByUserId(String userId);
	UserEntity findByEmail(String email);
	UserEntity findByUsername(String username);

}
