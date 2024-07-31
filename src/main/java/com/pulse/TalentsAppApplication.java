package com.pulse;

import com.pulse.enumeration.VerificationStatus;
import com.pulse.model.Admin;
import com.pulse.model.Role;
import com.pulse.model.User;
import com.pulse.repository.RoleRepository;
import com.pulse.repository.UserRepository;
import jakarta.servlet.ServletContext;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@EnableAsync
public class TalentsAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(TalentsAppApplication.class, args);
	}

	@Bean
	public CommandLineRunner CommandLineRunner(
			UserRepository userRepository,
			PasswordEncoder passwordEncoder,
			RoleRepository roleRepository,
			ServletContext servletContext
	){
		return new CommandLineRunner() {
			@Override
			public void run(String... args) throws Exception {
				Role roleAdmin = Role.builder().name("ADMIN").build();
				Role roleClient = Role.builder().name("CLIENT").build();
				Role roleTalent = Role.builder().name("TALENT").build();
				Role roleSuperAdmin= Role.builder().name("SUPER_ADMIN").build();
				if(roleRepository.findByName(roleSuperAdmin.getName()).isEmpty())
					roleRepository.save(roleSuperAdmin);
				if(roleRepository.findByName(roleAdmin.getName()).isEmpty())
					roleRepository.save(roleAdmin);
				if(roleRepository.findByName(roleClient.getName()).isEmpty())
					roleRepository.save(roleClient);
				if(roleRepository.findByName(roleTalent.getName()).isEmpty())
					roleRepository.save(roleTalent);
				Admin admin = Admin.builder()
						.email("admin@mohmal.in")
						.password(passwordEncoder.encode("123456789"))
						.firstName("Yassir")
						.lastName("Merfouk")
						.phone("0642848255")
						.status(VerificationStatus.VERIFIED)
						.enabled(true)
						.locked(false)
						.roles(List.of(roleAdmin))
						.createdAt(LocalDateTime.now())
						.build();
/*				Admin superAdmin = Admin.builder()
						.email("superadmin@mohmal.in")
						.password(passwordEncoder.encode("123456789"))
						.firstName("Yassir")
						.lastName("Merfouk")
						.phone("0642848255")
						.status(VerificationStatus.VERIFIED)
						.enabled(true)
						.locked(false)
						.roles(List.of(roleAdmin, roleSuperAdmin))
						.build();*/

				if(userRepository.findByEmail("admin@mohmal.in").isEmpty())
					userRepository.save(admin);
/*				if(userRepository.findByEmail("superadmin@mohmal.in").isEmpty())
					userRepository.save(superAdmin);*/
			}
		};
	}
}
