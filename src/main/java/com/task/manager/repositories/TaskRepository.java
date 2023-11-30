package com.task.manager.repositories;

import java.util.ArrayList;
import java.util.Optional;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.task.manager.model.Task;
import com.task.manager.model.Task.Status;

@Repository
public interface TaskRepository
                extends PagingAndSortingRepository<Task, Long>{

        static Specification<Task> hasStatus(Status status) {
                return (task, cq, cb) -> cb.equal(task.get("status"), status);
        }

        static Specification<Task> filteredBy(Optional<Status> status) {
                var specs = new ArrayList<Specification<Task>>();
                if (status.isPresent()) {
                        specs.add(Specification.where(hasStatus(status.get())));
                }
                if (specs.size() == 0)
                        return null;
                var spec = specs.get(0);
                for (var spec2 : specs) {
                        spec = spec.and(spec2);
                }
                return spec;
        }

        @Modifying
        @Query("UPDATE Task t SET t.subject = :subject, t.description = :description WHERE t.id = :id")
        int updateTask(@Param("id") long id, @Param("subject") String subject,
                        @Param("description") String description);


}
