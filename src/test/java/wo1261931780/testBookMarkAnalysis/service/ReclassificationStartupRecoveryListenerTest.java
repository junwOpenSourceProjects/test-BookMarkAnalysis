package wo1261931780.testBookMarkAnalysis.service;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReclassificationStartupRecoveryListenerTest {

    @Mock private ReclassificationTaskControlService controlService;

    @Test
    void marksInterruptedTasksRecoverableWhenTheApplicationIsReady() {
        ReclassificationStartupRecoveryListener listener =
                new ReclassificationStartupRecoveryListener(controlService);

        listener.onApplicationReady();

        verify(controlService).recoverInterruptedTasks();
    }
}
