package com.nghianguyen.scnetwork.services;

import com.nghianguyen.scnetwork.dtos.RelationshipDTO;
import com.nghianguyen.scnetwork.models.User;
import com.nghianguyen.scnetwork.models.relationship.FriendsCandidatesViewModel;
import com.nghianguyen.scnetwork.models.relationship.Relationship;
import com.nghianguyen.scnetwork.repositories.RelatioshipRepository;
import com.nghianguyen.scnetwork.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


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
}
