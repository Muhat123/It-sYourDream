package com.maju_mundur.MajuMundur.service.impl;

import com.maju_mundur.MajuMundur.entity.Role;
import com.maju_mundur.MajuMundur.repository.RoleRepository;
import com.maju_mundur.MajuMundur.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public Role getOrSave(Role.UserRole role) {
        Optional<Role> theRoles = roleRepository.findByName(role);
        if(theRoles.isPresent()){
            return theRoles.get();
        }
        Role currentRole = Role.builder()
                .name(role)
                .build();
        return roleRepository.saveAndFlush(currentRole);
    }
}
