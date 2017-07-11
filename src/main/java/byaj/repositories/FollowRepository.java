package byaj.repositories;

/**
 * Created by student on 6/20/17.
 */

import byaj.models.Follow;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FollowRepository extends CrudRepository<Follow, Integer> {
    public List<Follow> findAllByFollowUser(int num);
}