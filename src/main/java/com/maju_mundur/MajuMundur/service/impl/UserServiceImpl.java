package com.maju_mundur.MajuMundur.service.impl;

import com.maju_mundur.MajuMundur.repository.UserRepository;
import com.maju_mundur.MajuMundur.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserById(String id) {
        return userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("Invalid credential user"));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Invalid credential user"));
    }
}
