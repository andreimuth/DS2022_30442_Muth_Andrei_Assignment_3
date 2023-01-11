package com.example.assignment1;

import com.example.assignment1.entities.ERole;
import com.example.assignment1.entities.Role;
import com.example.assignment1.entities.User;
import com.example.assignment1.repositories.RoleRepository;
import com.example.assignment1.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Bootstrapper implements ApplicationListener<ApplicationReadyEvent> {

    private final RoleRepository roleRepository;

    private final UserRepository userRepository;

    private final PasswordEncoder encoder;

    @Value("${app.bootstrap}")
    private Boolean bootstrap;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if(bootstrap) {
            roleRepository.deleteAll();
            userRepository.deleteAll();
            for (ERole value : ERole.values()) {
                roleRepository.save(
                        Role.builder()
                                .name(value)
                                .build()
                );
            }
            Role adminRole = roleRepository.findByName(ERole.ADMIN).orElseThrow(() -> new RuntimeException("Admin role not found"));
            User adminUser = User.builder()
                    .username("admin")
                    .password(encoder.encode("admin"))
                    .role(adminRole)
                    .build();
            userRepository.save(adminUser);
        }
    }

}
