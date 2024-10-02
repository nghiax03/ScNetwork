package com.nghianguyen.scnetwork.repositories;

import com.nghianguyen.scnetwork.models.relationship.Relationship;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RelatioshipRepository extends JpaRepository<Relationship, Long> {
    List<Relationship> findAllByUserOneIdAndStatus(Long id, String status);
}
