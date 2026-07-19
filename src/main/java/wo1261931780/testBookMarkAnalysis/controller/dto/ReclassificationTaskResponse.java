package wo1261931780.testBookMarkAnalysis.controller.dto;

/** Database-backed progress snapshot for a resumable bookmark rebuild. */
public record ReclassificationTaskResponse(
        String taskId,
        String status,
        String phase,
        int totalBookmarks,
        int totalWorkUnits,
        int completedWorkUnits,
        int largeDomainGroups,
        int smallPoolBookmarks,
        int createdFolders,
        int movedBookmarks,
        int updatedTitles,
        int recoveryCount,
        String treeClearedAt,
        String errorMessage) {}
