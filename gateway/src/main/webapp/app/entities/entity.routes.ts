import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'gatewayApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'document',
    data: { pageTitle: 'gatewayApp.documentServiceDocument.home.title' },
    loadChildren: () => import('./documentService/document/document.routes'),
  },
  {
    path: 'document-service-status',
    data: { pageTitle: 'gatewayApp.documentServiceDocumentServiceStatus.home.title' },
    loadChildren: () => import('./documentService/document-service-status/document-service-status.routes'),
  },
  {
    path: 'meta-folder',
    data: { pageTitle: 'gatewayApp.documentServiceMetaFolder.home.title' },
    loadChildren: () => import('./documentService/meta-folder/meta-folder.routes'),
  },
  {
    path: 'meta-tag',
    data: { pageTitle: 'gatewayApp.documentServiceMetaTag.home.title' },
    loadChildren: () => import('./documentService/meta-tag/meta-tag.routes'),
  },
  {
    path: 'meta-meta-tag-category',
    data: { pageTitle: 'gatewayApp.documentServiceMetaMetaTagCategory.home.title' },
    loadChildren: () => import('./documentService/meta-meta-tag-category/meta-meta-tag-category.routes'),
  },
  {
    path: 'document-tag',
    data: { pageTitle: 'gatewayApp.documentServiceDocumentTag.home.title' },
    loadChildren: () => import('./documentService/document-tag/document-tag.routes'),
  },
  {
    path: 'document-type',
    data: { pageTitle: 'gatewayApp.documentServiceDocumentType.home.title' },
    loadChildren: () => import('./documentService/document-type/document-type.routes'),
  },
  {
    path: 'document-type-field',
    data: { pageTitle: 'gatewayApp.documentServiceDocumentTypeField.home.title' },
    loadChildren: () => import('./documentService/document-type-field/document-type-field.routes'),
  },
  {
    path: 'document-metadata',
    data: { pageTitle: 'gatewayApp.documentServiceDocumentMetadata.home.title' },
    loadChildren: () => import('./documentService/document-metadata/document-metadata.routes'),
  },
  {
    path: 'document-extracted-field',
    data: { pageTitle: 'gatewayApp.documentServiceDocumentExtractedField.home.title' },
    loadChildren: () => import('./documentService/document-extracted-field/document-extracted-field.routes'),
  },
  {
    path: 'document-permission',
    data: { pageTitle: 'gatewayApp.documentServiceDocumentPermission.home.title' },
    loadChildren: () => import('./documentService/document-permission/document-permission.routes'),
  },
  {
    path: 'meta-permission-group',
    data: { pageTitle: 'gatewayApp.documentServiceMetaPermissionGroup.home.title' },
    loadChildren: () => import('./documentService/meta-permission-group/meta-permission-group.routes'),
  },
  {
    path: 'document-audit',
    data: { pageTitle: 'gatewayApp.documentServiceDocumentAudit.home.title' },
    loadChildren: () => import('./documentService/document-audit/document-audit.routes'),
  },
  {
    path: 'document-comment',
    data: { pageTitle: 'gatewayApp.documentServiceDocumentComment.home.title' },
    loadChildren: () => import('./documentService/document-comment/document-comment.routes'),
  },
  {
    path: 'document-relation',
    data: { pageTitle: 'gatewayApp.documentServiceDocumentRelation.home.title' },
    loadChildren: () => import('./documentService/document-relation/document-relation.routes'),
  },
  {
    path: 'document-statistics',
    data: { pageTitle: 'gatewayApp.documentServiceDocumentStatistics.home.title' },
    loadChildren: () => import('./documentService/document-statistics/document-statistics.routes'),
  },
  {
    path: 'document-template',
    data: { pageTitle: 'gatewayApp.documentServiceDocumentTemplate.home.title' },
    loadChildren: () => import('./documentService/document-template/document-template.routes'),
  },
  {
    path: 'meta-saved-search',
    data: { pageTitle: 'gatewayApp.documentServiceMetaSavedSearch.home.title' },
    loadChildren: () => import('./documentService/meta-saved-search/meta-saved-search.routes'),
  },
  {
    path: 'meta-smart-folder',
    data: { pageTitle: 'gatewayApp.documentServiceMetaSmartFolder.home.title' },
    loadChildren: () => import('./documentService/meta-smart-folder/meta-smart-folder.routes'),
  },
  {
    path: 'meta-bookmark',
    data: { pageTitle: 'gatewayApp.documentServiceMetaBookmark.home.title' },
    loadChildren: () => import('./documentService/meta-bookmark/meta-bookmark.routes'),
  },
  {
    path: 'document-version',
    data: { pageTitle: 'gatewayApp.documentServiceDocumentVersion.home.title' },
    loadChildren: () => import('./documentService/document-version/document-version.routes'),
  },
  {
    path: 'document-process',
    data: { pageTitle: 'gatewayApp.documentProcess.home.title' },
    loadChildren: () => import('./document-process/document-process.routes'),
  },
  {
    path: 'ocr-job',
    data: { pageTitle: 'gatewayApp.ocrServiceOcrJob.home.title' },
    loadChildren: () => import('./ocrService/ocr-job/ocr-job.routes'),
  },
  {
    path: 'ocr-result',
    data: { pageTitle: 'gatewayApp.ocrServiceOcrResult.home.title' },
    loadChildren: () => import('./ocrService/ocr-result/ocr-result.routes'),
  },
  {
    path: 'orc-extracted-text',
    data: { pageTitle: 'gatewayApp.ocrServiceOrcExtractedText.home.title' },
    loadChildren: () => import('./ocrService/orc-extracted-text/orc-extracted-text.routes'),
  },
  {
    path: 'ocr-comparison',
    data: { pageTitle: 'gatewayApp.ocrServiceOcrComparison.home.title' },
    loadChildren: () => import('./ocrService/ocr-comparison/ocr-comparison.routes'),
  },
  {
    path: 'ocr-cache',
    data: { pageTitle: 'gatewayApp.ocrServiceOcrCache.home.title' },
    loadChildren: () => import('./ocrService/ocr-cache/ocr-cache.routes'),
  },
  {
    path: 'search-index',
    data: { pageTitle: 'gatewayApp.searchServiceSearchIndex.home.title' },
    loadChildren: () => import('./searchService/search-index/search-index.routes'),
  },
  {
    path: 'search-query',
    data: { pageTitle: 'gatewayApp.searchServiceSearchQuery.home.title' },
    loadChildren: () => import('./searchService/search-query/search-query.routes'),
  },
  {
    path: 'search-semantic',
    data: { pageTitle: 'gatewayApp.searchServiceSearchSemantic.home.title' },
    loadChildren: () => import('./searchService/search-semantic/search-semantic.routes'),
  },
  {
    path: 'search-facet',
    data: { pageTitle: 'gatewayApp.searchServiceSearchFacet.home.title' },
    loadChildren: () => import('./searchService/search-facet/search-facet.routes'),
  },
  {
    path: 'notification',
    data: { pageTitle: 'gatewayApp.notificationServiceNotification.home.title' },
    loadChildren: () => import('./notificationService/notification/notification.routes'),
  },
  {
    path: 'notification-template',
    data: { pageTitle: 'gatewayApp.notificationServiceNotificationTemplate.home.title' },
    loadChildren: () => import('./notificationService/notification-template/notification-template.routes'),
  },
  {
    path: 'notification-preference',
    data: { pageTitle: 'gatewayApp.notificationServiceNotificationPreference.home.title' },
    loadChildren: () => import('./notificationService/notification-preference/notification-preference.routes'),
  },
  {
    path: 'notification-event',
    data: { pageTitle: 'gatewayApp.notificationServiceNotificationEvent.home.title' },
    loadChildren: () => import('./notificationService/notification-event/notification-event.routes'),
  },
  {
    path: 'notification-webhook-subscription',
    data: { pageTitle: 'gatewayApp.notificationServiceNotificationWebhookSubscription.home.title' },
    loadChildren: () => import('./notificationService/notification-webhook-subscription/notification-webhook-subscription.routes'),
  },
  {
    path: 'notification-webhook-log',
    data: { pageTitle: 'gatewayApp.notificationServiceNotificationWebhookLog.home.title' },
    loadChildren: () => import('./notificationService/notification-webhook-log/notification-webhook-log.routes'),
  },
  {
    path: 'workflow',
    data: { pageTitle: 'gatewayApp.workflowServiceWorkflow.home.title' },
    loadChildren: () => import('./workflowService/workflow/workflow.routes'),
  },
  {
    path: 'workflow-step',
    data: { pageTitle: 'gatewayApp.workflowServiceWorkflowStep.home.title' },
    loadChildren: () => import('./workflowService/workflow-step/workflow-step.routes'),
  },
  {
    path: 'workflow-instance',
    data: { pageTitle: 'gatewayApp.workflowServiceWorkflowInstance.home.title' },
    loadChildren: () => import('./workflowService/workflow-instance/workflow-instance.routes'),
  },
  {
    path: 'workflow-task',
    data: { pageTitle: 'gatewayApp.workflowServiceWorkflowTask.home.title' },
    loadChildren: () => import('./workflowService/workflow-task/workflow-task.routes'),
  },
  {
    path: 'workflow-approval-history',
    data: { pageTitle: 'gatewayApp.workflowServiceWorkflowApprovalHistory.home.title' },
    loadChildren: () => import('./workflowService/workflow-approval-history/workflow-approval-history.routes'),
  },
  {
    path: 'ai-auto-tag-job',
    data: { pageTitle: 'gatewayApp.aiServiceAIAutoTagJob.home.title' },
    loadChildren: () => import('./aiService/ai-auto-tag-job/ai-auto-tag-job.routes'),
  },
  {
    path: 'ai-tag-prediction',
    data: { pageTitle: 'gatewayApp.aiServiceAITagPrediction.home.title' },
    loadChildren: () => import('./aiService/ai-tag-prediction/ai-tag-prediction.routes'),
  },
  {
    path: 'ai-type-prediction',
    data: { pageTitle: 'gatewayApp.aiServiceAITypePrediction.home.title' },
    loadChildren: () => import('./aiService/ai-type-prediction/ai-type-prediction.routes'),
  },
  {
    path: 'ai-correspondent-prediction',
    data: { pageTitle: 'gatewayApp.aiServiceAICorrespondentPrediction.home.title' },
    loadChildren: () => import('./aiService/ai-correspondent-prediction/ai-correspondent-prediction.routes'),
  },
  {
    path: 'ai-language-detection',
    data: { pageTitle: 'gatewayApp.aiServiceAILanguageDetection.home.title' },
    loadChildren: () => import('./aiService/ai-language-detection/ai-language-detection.routes'),
  },
  {
    path: 'ai-cache',
    data: { pageTitle: 'gatewayApp.aiServiceAICache.home.title' },
    loadChildren: () => import('./aiService/ai-cache/ai-cache.routes'),
  },
  {
    path: 'similarity-document-comparison',
    data: { pageTitle: 'gatewayApp.similarityServiceSimilarityDocumentComparison.home.title' },
    loadChildren: () => import('./similarityService/similarity-document-comparison/similarity-document-comparison.routes'),
  },
  {
    path: 'similarity-job',
    data: { pageTitle: 'gatewayApp.similarityServiceSimilarityJob.home.title' },
    loadChildren: () => import('./similarityService/similarity-job/similarity-job.routes'),
  },
  {
    path: 'similarity-document-fingerprint',
    data: { pageTitle: 'gatewayApp.similarityServiceSimilarityDocumentFingerprint.home.title' },
    loadChildren: () => import('./similarityService/similarity-document-fingerprint/similarity-document-fingerprint.routes'),
  },
  {
    path: 'similarity-cluster',
    data: { pageTitle: 'gatewayApp.similarityServiceSimilarityCluster.home.title' },
    loadChildren: () => import('./similarityService/similarity-cluster/similarity-cluster.routes'),
  },
  {
    path: 'archive-job',
    data: { pageTitle: 'gatewayApp.archiveServiceArchiveJob.home.title' },
    loadChildren: () => import('./archiveService/archive-job/archive-job.routes'),
  },
  {
    path: 'archive-document',
    data: { pageTitle: 'gatewayApp.archiveServiceArchiveDocument.home.title' },
    loadChildren: () => import('./archiveService/archive-document/archive-document.routes'),
  },
  {
    path: 'export-job',
    data: { pageTitle: 'gatewayApp.exportServiceExportJob.home.title' },
    loadChildren: () => import('./exportService/export-job/export-job.routes'),
  },
  {
    path: 'export-pattern',
    data: { pageTitle: 'gatewayApp.exportServiceExportPattern.home.title' },
    loadChildren: () => import('./exportService/export-pattern/export-pattern.routes'),
  },
  {
    path: 'export-result',
    data: { pageTitle: 'gatewayApp.exportServiceExportResult.home.title' },
    loadChildren: () => import('./exportService/export-result/export-result.routes'),
  },
  {
    path: 'email-import-document',
    data: { pageTitle: 'gatewayApp.emailImportDocumentServiceEmailImportDocument.home.title' },
    loadChildren: () => import('./EmailImportDocumentService/email-import-document/email-import-document.routes'),
  },
  {
    path: 'email-import-email-attachment',
    data: { pageTitle: 'gatewayApp.emailImportDocumentServiceEmailImportEmailAttachment.home.title' },
    loadChildren: () => import('./EmailImportDocumentService/email-import-email-attachment/email-import-email-attachment.routes'),
  },
  {
    path: 'email-import-import-rule',
    data: { pageTitle: 'gatewayApp.emailImportDocumentServiceEmailImportImportRule.home.title' },
    loadChildren: () => import('./EmailImportDocumentService/email-import-import-rule/email-import-import-rule.routes'),
  },
  {
    path: 'email-import-import-mapping',
    data: { pageTitle: 'gatewayApp.emailImportDocumentServiceEmailImportImportMapping.home.title' },
    loadChildren: () => import('./EmailImportDocumentService/email-import-import-mapping/email-import-import-mapping.routes'),
  },
  {
    path: 'monitoring-alert',
    data: { pageTitle: 'gatewayApp.monitoringServiceMonitoringAlert.home.title' },
    loadChildren: () => import('./monitoringService/monitoring-alert/monitoring-alert.routes'),
  },
  {
    path: 'monitoring-alert-rule',
    data: { pageTitle: 'gatewayApp.monitoringServiceMonitoringAlertRule.home.title' },
    loadChildren: () => import('./monitoringService/monitoring-alert-rule/monitoring-alert-rule.routes'),
  },
  {
    path: 'monitoring-document-watch',
    data: { pageTitle: 'gatewayApp.monitoringServiceMonitoringDocumentWatch.home.title' },
    loadChildren: () => import('./monitoringService/monitoring-document-watch/monitoring-document-watch.routes'),
  },
  {
    path: 'monitoring-maintenance-task',
    data: { pageTitle: 'gatewayApp.monitoringServiceMonitoringMaintenanceTask.home.title' },
    loadChildren: () => import('./monitoringService/monitoring-maintenance-task/monitoring-maintenance-task.routes'),
  },
  {
    path: 'monitoring-system-health',
    data: { pageTitle: 'gatewayApp.monitoringServiceMonitoringSystemHealth.home.title' },
    loadChildren: () => import('./monitoringService/monitoring-system-health/monitoring-system-health.routes'),
  },
  {
    path: 'monitoring-service-status',
    data: { pageTitle: 'gatewayApp.monitoringServiceMonitoringServiceStatus.home.title' },
    loadChildren: () => import('./monitoringService/monitoring-service-status/monitoring-service-status.routes'),
  },
  {
    path: 'transform-conversion-job',
    data: { pageTitle: 'gatewayApp.transformServiceTransformConversionJob.home.title' },
    loadChildren: () => import('./transformService/transform-conversion-job/transform-conversion-job.routes'),
  },
  {
    path: 'transform-watermark-job',
    data: { pageTitle: 'gatewayApp.transformServiceTransformWatermarkJob.home.title' },
    loadChildren: () => import('./transformService/transform-watermark-job/transform-watermark-job.routes'),
  },
  {
    path: 'transform-redaction-job',
    data: { pageTitle: 'gatewayApp.transformServiceTransformRedactionJob.home.title' },
    loadChildren: () => import('./transformService/transform-redaction-job/transform-redaction-job.routes'),
  },
  {
    path: 'transform-compression-job',
    data: { pageTitle: 'gatewayApp.transformServiceTransformCompressionJob.home.title' },
    loadChildren: () => import('./transformService/transform-compression-job/transform-compression-job.routes'),
  },
  {
    path: 'transform-merge-job',
    data: { pageTitle: 'gatewayApp.transformServiceTransformMergeJob.home.title' },
    loadChildren: () => import('./transformService/transform-merge-job/transform-merge-job.routes'),
  },
  {
    path: 'reporting-dashboard',
    data: { pageTitle: 'gatewayApp.reportingServiceReportingDashboard.home.title' },
    loadChildren: () => import('./reportingService/reporting-dashboard/reporting-dashboard.routes'),
  },
  {
    path: 'reporting-dashboard-widget',
    data: { pageTitle: 'gatewayApp.reportingServiceReportingDashboardWidget.home.title' },
    loadChildren: () => import('./reportingService/reporting-dashboard-widget/reporting-dashboard-widget.routes'),
  },
  {
    path: 'reporting-scheduled-report',
    data: { pageTitle: 'gatewayApp.reportingServiceReportingScheduledReport.home.title' },
    loadChildren: () => import('./reportingService/reporting-scheduled-report/reporting-scheduled-report.routes'),
  },
  {
    path: 'reporting-execution',
    data: { pageTitle: 'gatewayApp.reportingServiceReportingExecution.home.title' },
    loadChildren: () => import('./reportingService/reporting-execution/reporting-execution.routes'),
  },
  {
    path: 'reporting-system-metric',
    data: { pageTitle: 'gatewayApp.reportingServiceReportingSystemMetric.home.title' },
    loadChildren: () => import('./reportingService/reporting-system-metric/reporting-system-metric.routes'),
  },
  {
    path: 'reporting-performance-metric',
    data: { pageTitle: 'gatewayApp.reportingServiceReportingPerformanceMetric.home.title' },
    loadChildren: () => import('./reportingService/reporting-performance-metric/reporting-performance-metric.routes'),
  },
  {
    path: 'image-pdf-conversion-request',
    data: { pageTitle: 'gatewayApp.pdfToImageServiceImagePdfConversionRequest.home.title' },
    loadChildren: () => import('./pdfToImageService/image-pdf-conversion-request/image-pdf-conversion-request.routes'),
  },
  {
    path: 'image-generated-image',
    data: { pageTitle: 'gatewayApp.pdfToImageServiceImageGeneratedImage.home.title' },
    loadChildren: () => import('./pdfToImageService/image-generated-image/image-generated-image.routes'),
  },
  {
    path: 'image-conversion-batch',
    data: { pageTitle: 'gatewayApp.pdfToImageServiceImageConversionBatch.home.title' },
    loadChildren: () => import('./pdfToImageService/image-conversion-batch/image-conversion-batch.routes'),
  },
  {
    path: 'image-conversion-config',
    data: { pageTitle: 'gatewayApp.pdfToImageServiceImageConversionConfig.home.title' },
    loadChildren: () => import('./pdfToImageService/image-conversion-config/image-conversion-config.routes'),
  },
  {
    path: 'image-conversion-history',
    data: { pageTitle: 'gatewayApp.pdfToImageServiceImageConversionHistory.home.title' },
    loadChildren: () => import('./pdfToImageService/image-conversion-history/image-conversion-history.routes'),
  },
  {
    path: 'image-conversion-statistics',
    data: { pageTitle: 'gatewayApp.pdfToImageServiceImageConversionStatistics.home.title' },
    loadChildren: () => import('./pdfToImageService/image-conversion-statistics/image-conversion-statistics.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
