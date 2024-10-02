package com.nghianguyen.scnetwork.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nghianguyen.scnetwork.dtos.RelationshipDTO;
import com.nghianguyen.scnetwork.models.relationship.FriendsAllViewModel;
import com.nghianguyen.scnetwork.services.RelationshipService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
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
}
