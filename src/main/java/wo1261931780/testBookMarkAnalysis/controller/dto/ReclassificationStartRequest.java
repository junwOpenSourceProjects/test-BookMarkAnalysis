package wo1261931780.testBookMarkAnalysis.controller.dto;

/** Optional non-secret AI endpoint overrides for a durable reclassification task. */
public record ReclassificationStartRequest(String apiBaseUrl, String modelName) {}
