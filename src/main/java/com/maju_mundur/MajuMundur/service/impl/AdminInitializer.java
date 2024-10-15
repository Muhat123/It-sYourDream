//package com.maju_mundur.MajuMundur.service.impl;
//
//import com.maju_mundur.MajuMundur.entity.Role;
//import com.maju_mundur.MajuMundur.entity.User;
//import com.maju_mundur.MajuMundur.repository.UserRepository;
//import com.maju_mundur.MajuMundur.service.RoleService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//public class AdminInitializer implements CommandLineRunner {
//    private final RoleService roleService;
//    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
//    @Override
//    public void run(String... args) throws Exception {
//        Role role = roleService.getOrSave(Role.UserRole.ADMIN);
//
//        if (userRepository.findByUsername("admin").isEmpty()){
//            User adminUser = User.builder()
//                    .username("admin123")
//                    .email("admin@gmail.com")
//                    .password(passwordEncoder.encode("admin123"))
//                    .role(role)
//                    .build();
//            userRepository.saveAndFlush(adminUser);
//            System.out.println("Admin user created with username: admin and default password: adminPassword");
//        }else {
//            System.out.println("Admin user already exists");
//        }
//    }
//}
