package byaj.repositories;

/**
 * Created by student on 6/20/17.
 */

import byaj.models.PostBuilder;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PostBuilderRepository extends CrudRepository<PostBuilder, Integer> {
    public List<PostBuilder> findAllByPostBuilderUser(int num);
}