package com.maju_mundur.MajuMundur.service;

import com.maju_mundur.MajuMundur.entity.Role;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleService {
    Role getOrSave(Role.UserRole role);
}
