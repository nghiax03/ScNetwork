package com.nghianguyen.scnetwork.models.relationship;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FriendsAllViewModel {
    private String id;
    @JsonProperty("full_name")
    private String fullName;
    private String username;
    @JsonProperty("profile_picture")
    private String profilePicture;
    private boolean isOnline;
}
