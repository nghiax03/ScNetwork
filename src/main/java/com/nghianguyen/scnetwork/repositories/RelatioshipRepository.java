package com.nghianguyen.scnetwork.repositories;

import com.nghianguyen.scnetwork.models.relationship.Relationship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RelatioshipRepository extends JpaRepository<Relationship, Long> {

    List<Relationship> findAllByUserOneIdAndStatus(Long id, String status);

    List<Relationship> findAllByUserOneIdAndStatusOrUserTwoIdAndStatus(Long id1, String status1, Long id2, String status2);

    List<Relationship> findAllByUserOneIdOrUserTwoIdAndStatusNot(Long id1, Long id2, String status);

    Relationship findByUserOneIdAndUserTwoId(Long userOneId, Long userTwoId);

    List<Relationship> findAllByUserOneIdOrUserTwoId(Long userOneId, Long userTwoId);

    @Query(value = "" +
            "SELECT r FROM Relationship AS r " +
            "WHERE ((r.userOne.id = :id1 AND r.userTwo.id = :id2) " +
            "OR ( r.userTwo.id = :id1 AND r.userOne.id = :id2)) " +
            "AND r.status = :status")
    Relationship findRelationshipWithFriendWithStatus(@Param(value = "id1") Long userOneId,
                                                      @Param(value = "id2") Long userTwoId,
                                                      @Param(value = "status") String status);

    @Query(
            value = "" +
                    "SELECT r FROM Relationship AS r "
            + "WHERE (r.userOne.id = :id OR r.userTwo.id = :id) "
            + "AND r.status NOT IN ('0', '2') "
    )
    List<Relationship> findAllUserNotFriends(@Param(value = "id") Long loggedInUserId);


    @Query(
            value = "" +
                    "SELECT r FROM Relationship AS r "
            + "WHERE (r.userOne.id = :id OR r.userTwo.id = :id) "
            + "AND r.status = :status"
    )
    List<Relationship> findRelationshipByUserIdAndStatus(
            @Param(value = "id") Long loggedInUserId,
            @Param(value = "status") String status);

    @Query(
            "SELECT r FROM Relationship AS r "
            +"WHERE ((r.userOne.id = :id1 AND r.userTwo.id = :id2) "
            +"OR (r.userTwo.id = :id1 AND r.userOne.id = :id2)) "
    )
    Relationship findRelationshipByUserOneIdAndUserTwoId(
            @Param(value = "id1") Long userOneId,
            @Param(value = "id2") Long userTwoId);
}
