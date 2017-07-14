package byaj.repositories;

import org.springframework.data.repository.CrudRepository;

import byaj.models.Photo;

public interface PhotoRepository extends CrudRepository<Photo, Long> {

}
