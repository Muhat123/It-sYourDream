package com.maju_mundur.MajuMundur.repository;

import com.maju_mundur.MajuMundur.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, String> {
}
