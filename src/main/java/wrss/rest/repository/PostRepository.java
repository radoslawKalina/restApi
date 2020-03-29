package wrss.rest.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import wrss.rest.entity.PostEntity;
import wrss.rest.entity.UserEntity;

@Repository
public interface PostRepository extends PagingAndSortingRepository<PostEntity, Integer> {
	
	PostEntity findById(int id);
	Page<PostEntity> findAllByUser(UserEntity user, Pageable pageable);

}
