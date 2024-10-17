package com.nghianguyen.scnetwork.repositories;

import com.nghianguyen.scnetwork.models.PictureMain;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AvatarRepository extends JpaRepository<PictureMain, Long> {
}
