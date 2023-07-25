package one.backbone.messagingassignment.init;

import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import one.backbone.messagingassignment.model.dto.request.CreateUserRequest;
import one.backbone.messagingassignment.model.entity.RoleType;
import one.backbone.messagingassignment.repository.RoleRepository;
import one.backbone.messagingassignment.service.AuthService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Profile("!prod") // Activate this configuration for all profiles except "prod"
@Slf4j
@Component
public class FakeUserInitializer {
    private final AuthService authService;
    private final RoleRepository roleRepository;

    public FakeUserInitializer(AuthService authService,
                               RoleRepository roleRepository) {
        this.authService = authService;
        this.roleRepository = roleRepository;
    }

    @Transactional
    public void fakeUserInit() {
        log.info("Fake data initialization started");
        Faker faker = new Faker();

        CreateUserRequest admin = new CreateUserRequest();
        admin.setName("bb1");
        admin.setUsername("bb1");
        admin.setPassword("password");
        admin.setRoles(List.of(RoleType.ROLE_ADMIN));
        authService.createUser(admin);

        for (int i = 0; i < 20; i++) {
            String name = faker.name().fullName();
            String username = name.toLowerCase()
                    .replace(" ", ".")
                    .replace("'", "")
                    .replace("..", ".");
            if (username.endsWith(".")) {
                username = username.substring(0, username.length() - 1);
            }
            CreateUserRequest user = new CreateUserRequest();
            user.setPassword("password");
            user.setUsername(username);
            user.setName(name);

            if (faker.random().nextInt(0, 2) == 1) {
                user.setRoles(List.of(RoleType.ROLE_ADMIN));
            } else {
                user.setRoles(List.of(RoleType.ROLE_USER));
            }
            authService.createUser(user);

        }
        log.info("Fake data initialization finished");
    }

}