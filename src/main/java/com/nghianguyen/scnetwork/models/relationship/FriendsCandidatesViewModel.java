package com.nghianguyen.scnetwork.models.relationship;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FriendsCandidatesViewModel {
    private Long id;

    @JsonProperty("full_name")
    private String fullName;

    private String username;

    @JsonProperty("profile_picture")
    private String profilePicture;

    private String status;
}
