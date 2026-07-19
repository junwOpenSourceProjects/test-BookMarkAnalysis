package wo1261931780.testBookMarkAnalysis.service;

public final class ReclassificationConstants {
    public static final int LARGE_DOMAIN_MIN_BOOKMARKS = 5;
    public static final int BOOKMARK_ANALYSIS_BATCH_SIZE = 25;
    public static final int SMALL_POOL_CLUSTER_DRAFT_SIZE = 80;

    public static final String TASK_STATUS_QUEUED = "QUEUED";
    public static final String TASK_STATUS_RUNNING = "RUNNING";
    public static final String TASK_STATUS_PAUSED = "PAUSED";
    public static final String TASK_STATUS_RECOVERABLE = "RECOVERABLE";
    public static final String TASK_STATUS_COMPLETED = "COMPLETED";
    public static final String TASK_STATUS_FAILED = "FAILED";

    public static final String TASK_PHASE_PREPARING = "PREPARING";
    public static final String TASK_PHASE_LARGE_DOMAINS = "LARGE_DOMAINS";
    public static final String TASK_PHASE_SMALL_ANALYSIS = "SMALL_ANALYSIS";
    public static final String TASK_PHASE_SMALL_CLUSTER_DRAFTS = "SMALL_CLUSTER_DRAFTS";
    public static final String TASK_PHASE_SMALL_CANONICALIZATION = "SMALL_CANONICALIZATION";
    public static final String TASK_PHASE_APPLYING = "APPLYING";

    public static final String WORK_UNIT_STATUS_PENDING = "PENDING";
    public static final String WORK_UNIT_STATUS_RUNNING = "RUNNING";
    public static final String WORK_UNIT_STATUS_SUCCEEDED = "SUCCEEDED";
    public static final String WORK_UNIT_STATUS_RETRYABLE_FAILED = "RETRYABLE_FAILED";
    public static final String WORK_UNIT_STATUS_PAUSED = "PAUSED";

    public static final String UNIT_LARGE_DOMAIN_FOLDER_NAME = "LARGE_DOMAIN_FOLDER_NAME";
    public static final String UNIT_LARGE_DOMAIN_BOOKMARK_ANALYSIS =
            "LARGE_DOMAIN_BOOKMARK_ANALYSIS";
    public static final String UNIT_SMALL_POOL_BOOKMARK_ANALYSIS =
            "SMALL_POOL_BOOKMARK_ANALYSIS";
    public static final String UNIT_SMALL_POOL_CLUSTER_DRAFT = "SMALL_POOL_CLUSTER_DRAFT";
    public static final String UNIT_SMALL_POOL_CANONICALIZE_FOLDERS =
            "SMALL_POOL_CANONICALIZE_FOLDERS";

    public static final String SNAPSHOT_POOL_LARGE_DOMAIN = "LARGE_DOMAIN";
    public static final String SNAPSHOT_POOL_SMALL_POOL = "SMALL_POOL";

    private ReclassificationConstants() {}
}
