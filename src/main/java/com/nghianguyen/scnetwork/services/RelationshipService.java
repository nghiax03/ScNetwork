package com.nghianguyen.scnetwork.services;

import com.nghianguyen.scnetwork.dtos.RelationshipDTO;
import com.nghianguyen.scnetwork.exception.DataNotFoundException;
import com.nghianguyen.scnetwork.models.User;
import com.nghianguyen.scnetwork.models.relationship.FriendsCandidatesViewModel;
import com.nghianguyen.scnetwork.models.relationship.Relationship;
import com.nghianguyen.scnetwork.repositories.RelatioshipRepository;
import com.nghianguyen.scnetwork.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RelationshipService {
    @Autowired
    private RelatioshipRepository relatioshipRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;

    public List<RelationshipDTO> findAllUserRelationshipsWithStatus(Long userId){
        List<Relationship> relationships = this.relatioshipRepository
                .findAllByUserOneIdAndStatus(userId, "1");
        return relationships.stream()
                .map(relationship -> this.modelMapper.map(relationship, RelationshipDTO.class))
                .collect(Collectors.toList());
    }

    public List<FriendsCandidatesViewModel> searchUser(Long loggedUserId, String keyWord){
        List<User> users = userRepository.findAllUsersLike(loggedUserId, keyWord);

        List<Relationship> currentUserRelationShip = this.relatioshipRepository
                .findAllByUserOneIdOrUserTwoId(loggedUserId, loggedUserId);

        return users.stream()
                .map(currentUser -> this.modelMapper.map(currentUser, FriendsCandidatesViewModel.class))
                .map(user -> mapUser(user, currentUserRelationShip))
                .collect(Collectors.toList());
    }

    //findAllFriendCandidates
    public List<FriendsCandidatesViewModel> findAllFriendCandidates(Long loggedInUserId) throws Exception{
        List<User> users = this.userRepository.findAll();

        List<Relationship> notFriend = this.relatioshipRepository.findAllUserNotFriends(loggedInUserId);
        List<Relationship> relationshipWithStatusZero =
                this.relatioshipRepository.findRelationshipByUserIdAndStatus(loggedInUserId, "0");

        List<User> usersWithRelationship = new ArrayList<>();
        notFriend.forEach(relationship -> {
            if(!relationship.getUserOne().getId().equals(loggedInUserId)){
                usersWithRelationship.add(relationship.getUserOne());
            }
            else{
                usersWithRelationship.add(relationship.getUserTwo());
            }
        });

        List<FriendsCandidatesViewModel> notFriendsUserList =
                users.stream().filter(
                        user -> !usersWithRelationship.contains(user) &&
                                !user.getId().equals(loggedInUserId)
                ).map(user -> this.modelMapper.map(user, FriendsCandidatesViewModel.class))
                        .collect(Collectors.toList());

        return notFriendsUserList.stream()
                .map(user -> mapUser(user, relationshipWithStatusZero))
                .collect(Collectors.toList());
    }

    public boolean createRequestForAddingFriend(Long loggedInUserId, Long friendCandidateId)
        throws Exception{
        User userLogged = userRepository.findById(loggedInUserId)
                .orElseThrow(()-> new DataNotFoundException("User cannot find"));

        User friendCandidateUser = this.userRepository.findById(friendCandidateId)
                .orElseThrow(()-> new DataNotFoundException("Cannot find Friend candidate"));

        Relationship relationshipFromDb = this.relatioshipRepository
                .findRelationshipByUserOneIdAndUserTwoId(loggedInUserId, friendCandidateId);
        if(relationshipFromDb == null){
            Relationship relationship = new Relationship();
            relationship.setActionUser(userLogged);
            relationship.setUserOne(userLogged);
            relationship.setUserTwo(friendCandidateUser);
            relationship.setStatus("0");
            relationship.setTime(LocalDateTime.now());
            return this.relatioshipRepository.save(relationship) != null;
        } else {
            relationshipFromDb.setActionUser(userLogged);
            relationshipFromDb.setStatus("0");
            relationshipFromDb.setTime(LocalDateTime.now());
            return this.relatioshipRepository.save(relationshipFromDb) != null;
        }
    }

    public boolean acceptFriend(Long loggedInUserId, Long friendToAcceptId){
        try {
            return this.changeStatusAndSave(loggedInUserId, friendToAcceptId, "0", "1");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean removeFriend(Long loggedInUserId, Long friendToRemoveId){
        try {
            return this.changeStatusAndSave(loggedInUserId, friendToRemoveId, "1", "2");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean cancelFriendShipRequest(Long loggedInUserId, Long friendToRejectId){
        try {
            return this.changeStatusAndSave(loggedInUserId, friendToRejectId, "0", "2");
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
    private FriendsCandidatesViewModel mapUser(FriendsCandidatesViewModel user,
                                               List<Relationship> relationshipList){
        Relationship relationshipWithCurrentUser =
                relationshipList.stream()
                        .filter(relationship ->
                                relationship.getUserOne().getId().equals(user.getId()) ||
                                relationship.getUserTwo().getId().equals(user.getId()))
                        .findFirst().orElse(null);
        if(relationshipWithCurrentUser != null){
            user.setStatus(relationshipWithCurrentUser.getStatus());
        }
        return user;
    }

    //userId, friendId, statusFrom, statusTO
    private boolean changeStatusAndSave(Long loggedInUserId, Long friendId, String statusFrom, String statusTo)
        throws Exception{
        User loggedUser = this.userRepository.findById(loggedInUserId)
                .orElseThrow(()->
                        new DataNotFoundException("Cannot found logged in user id: " + loggedInUserId));

        User userFriend = this.userRepository.findById(friendId)
                .orElseThrow(()->
                        new DataNotFoundException("Cannot found friend user id: " + friendId));
        Relationship relationship = this.relatioshipRepository
                .findRelationshipWithFriendWithStatus(loggedInUserId, friendId, statusFrom);
                //'ll set action, status(tostatus), time
        if(relationship == null){
            throw new Exception();
        }

        relationship.setActionUser(loggedUser);
        relationship.setStatus(statusTo);
        relationship.setTime(LocalDateTime.now());
        return relatioshipRepository.save(relationship) != null;
    }
}
