package one.backbone.messagingassignment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface BaseRepository<T, I extends Serializable> extends JpaRepository<T, I> {

    Optional<T> findById(I id);

    Optional<T> findByIdAndDeletedFalse(I id);

    List<T> findAllByDeletedFalse();

    Page<T> findAllByDeletedFalse(Pageable pageable);

}
