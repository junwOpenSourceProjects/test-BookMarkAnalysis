package wo1261931780.testBookMarkAnalysis.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ReclassificationConstantsTest {

    @Test
    void usesConfirmedThresholdAndRecoveryStates() {
        assertEquals(5, ReclassificationConstants.LARGE_DOMAIN_MIN_BOOKMARKS);
        assertEquals("RECOVERABLE", ReclassificationConstants.TASK_STATUS_RECOVERABLE);
        assertEquals(
                "SMALL_POOL_CANONICALIZE_FOLDERS",
                ReclassificationConstants.UNIT_SMALL_POOL_CANONICALIZE_FOLDERS);
    }
}
