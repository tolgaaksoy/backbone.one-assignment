package one.backbone.messagingassignment.init;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class InitService {

    private final RoleInitializer roleInitializer;
    private final FakeUserInitializer fakeUserInitializer;

    public InitService(RoleInitializer roleInitializer, FakeUserInitializer fakeUserInitializer) {
        this.roleInitializer = roleInitializer;
        this.fakeUserInitializer = fakeUserInitializer;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void applicationStart() {
        log.info("Application started");
        roleInitializer.roleInit();
        fakeUserInitializer.fakeUserInit();
        log.info("Application finished");
    }
}
