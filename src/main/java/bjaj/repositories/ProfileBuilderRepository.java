package byaj.repositories;

/**
 * Created by student on 6/20/17.
 */

import byaj.models.ProfileBuilder;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProfileBuilderRepository extends CrudRepository<ProfileBuilder, Integer> {
    public List<ProfileBuilder> findAllByProfileBuilderUser(int num);
}