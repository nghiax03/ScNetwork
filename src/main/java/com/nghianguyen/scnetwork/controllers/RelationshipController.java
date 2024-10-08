package com.nghianguyen.scnetwork.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nghianguyen.scnetwork.dtos.RelationshipDTO;
import com.nghianguyen.scnetwork.exception.CustomException;
import com.nghianguyen.scnetwork.models.relationship.FriendsAllViewModel;
import com.nghianguyen.scnetwork.models.relationship.FriendsCandidatesViewModel;
import com.nghianguyen.scnetwork.response.ResponseSuccess;
import com.nghianguyen.scnetwork.services.RelationshipService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.prefix}/relationship")
public class RelationshipController {
    @Autowired
    private RelationshipService relationshipService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping(value = "/friends/{id}")
    public List<FriendsAllViewModel> findAllFriends(@PathVariable Long id) throws Exception{
        List<RelationshipDTO> allFriends = this.relationshipService.findAllUserRelationshipsWithStatus(id);
        List<FriendsAllViewModel> friendsAllViewModels = allFriends.stream().map(
                relationshipDTO -> {
                    if(!relationshipDTO.getUserOne().getId().equals(id)){
                        return this.modelMapper.map(relationshipDTO.getUserOne(), FriendsAllViewModel.class);
                    }
                    return this.modelMapper.map(relationshipDTO.getUserTwo(), FriendsAllViewModel.class);
                }).collect(Collectors.toList());
        return friendsAllViewModels;
    }

    @PostMapping(value = "/search", produces = "application/json")
    public List<FriendsCandidatesViewModel> searchUsers(@RequestBody Map<String, Object> body){
        String loggedInUserId = (String) body.get("loggedInUserId");
        Long loggedMap = Long.valueOf(loggedInUserId);
        String search = (String) body.get("keyWord");
        return this.relationshipService.searchUser(loggedMap, search);
    }

    @GetMapping(value = "/findFriends/{id}", produces = "application/json")
    public List<FriendsCandidatesViewModel> findAllNotFriends(@PathVariable Long id) throws Exception {
        return this.relationshipService.findAllFriendCandidates(id);
    }

    @PostMapping(value = "/addFriend")
    public ResponseSuccess addFriend(@RequestBody Map<String, Object> body) throws Exception{
        String logedInUserId = (String) body.get("loggedInUserId");
        Long loggedMap = Long.valueOf(logedInUserId);

        String friendCandidateId = (String) body.get("friendCandidateId");
        Long friendMap =  Long.valueOf(friendCandidateId);

        boolean result = this.relationshipService.createRequestForAddingFriend(loggedMap, friendMap);
        if(result){
            return new ResponseSuccess(HttpStatus.OK,
                    "success friend request submission message"
                    );
        }
        throw new CustomException("cannot add friend");
    }
}
