package wo1261931780.testBookMarkAnalysis.service;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ReclassificationStartupRecoveryListener {
    private final ReclassificationTaskControlService controlService;

    public ReclassificationStartupRecoveryListener(ReclassificationTaskControlService controlService) {
        this.controlService = controlService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReadyEvent() {
        onApplicationReady();
    }

    public void onApplicationReady() {
        controlService.recoverInterruptedTasks();
    }
}
