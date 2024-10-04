package com.nghianguyen.scnetwork.repositories;

import com.nghianguyen.scnetwork.models.relationship.Relationship;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RelatioshipRepository extends JpaRepository<Relationship, Long> {
    List<Relationship> findAllByUserOneIdAndStatus(Long id, String status);

    List<Relationship> findAllByUserOneIdAndStatusOrUserTwoIdAndStatus(Long id1, String status1, Long id2, String status2);

    Relationship findByUserOneIdAndUserTwoId(Long userOneId, Long userTwoId);

    List<Relationship> findAllByUserOneIdOrUserTwoId(Long userOneId, Long userTwoId);


}
