package one.backbone.messagingassignment.init;

import lombok.extern.slf4j.Slf4j;
import one.backbone.messagingassignment.model.entity.RoleType;
import one.backbone.messagingassignment.model.entity.UserRole;
import one.backbone.messagingassignment.repository.RoleRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class RoleInitializer {

    private final RoleRepository roleRepository;

    public RoleInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public void roleInit() {
        if (roleRepository.count() == 0) {
            log.info("Role initialization started");
            roleRepository.saveAll(List.of(
                    UserRole.builder().roleType(RoleType.ROLE_ADMIN).build(),
                    UserRole.builder().roleType(RoleType.ROLE_USER).build()
            ));
            log.info("Role initialization finished");
        } else {
            log.info("Role initialization skipped");
        }
    }

}
