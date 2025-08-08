//package com.example.ecommerce.component;
//
//import com.company.profile.ProfileRepository;
//import com.company.profile.model.Profile;
//import com.company.profile.model.Role;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class AppRunner {
//
//    @Bean
//    public CommandLineRunner createDefaultAdmin(ProfileRepository profileRepository) {
//        return args -> {
//            String email = "mazgiadmin@gmail.uz";
//            String fullName = "Mazgiadmin";
//            String psswd = "mazgiadmin123";
//
//            if (profileRepository.findByEmailAndVisibilityTrue(email).isEmpty()) {
//                Profile profile = Profile
//                        .builder()
//                        .email(email)
//                        .fullName("Mazgiyor")
//                        .role(Role.ADMIN)
//                        .password(psswd)
//                        .build();
//
//                profileRepository.save(profile);
//
//                System.out.println("Default admin user created");
//            } else {
//                System.out.println("Admin already exists");
//            }
//        };
//    }
//}
