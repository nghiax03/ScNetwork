package com.nghianguyen.scnetwork.services;

import com.nghianguyen.scnetwork.dtos.RelationshipDTO;
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
}
