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
    path: 'folder',
    data: { pageTitle: 'gatewayApp.documentServiceFolder.home.title' },
    loadChildren: () => import('./documentService/folder/folder.routes'),
  },
  {
    path: 'tag',
    data: { pageTitle: 'gatewayApp.documentServiceTag.home.title' },
    loadChildren: () => import('./documentService/tag/tag.routes'),
  },
  {
    path: 'tag-category',
    data: { pageTitle: 'gatewayApp.documentServiceTagCategory.home.title' },
    loadChildren: () => import('./documentService/tag-category/tag-category.routes'),
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
    path: 'extracted-field',
    data: { pageTitle: 'gatewayApp.documentServiceExtractedField.home.title' },
    loadChildren: () => import('./documentService/extracted-field/extracted-field.routes'),
  },
  {
    path: 'document-permission',
    data: { pageTitle: 'gatewayApp.documentServiceDocumentPermission.home.title' },
    loadChildren: () => import('./documentService/document-permission/document-permission.routes'),
  },
  {
    path: 'permission-group',
    data: { pageTitle: 'gatewayApp.documentServicePermissionGroup.home.title' },
    loadChildren: () => import('./documentService/permission-group/permission-group.routes'),
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
    path: 'saved-search',
    data: { pageTitle: 'gatewayApp.documentServiceSavedSearch.home.title' },
    loadChildren: () => import('./documentService/saved-search/saved-search.routes'),
  },
  {
    path: 'smart-folder',
    data: { pageTitle: 'gatewayApp.documentServiceSmartFolder.home.title' },
    loadChildren: () => import('./documentService/smart-folder/smart-folder.routes'),
  },
  {
    path: 'bookmark',
    data: { pageTitle: 'gatewayApp.documentServiceBookmark.home.title' },
    loadChildren: () => import('./documentService/bookmark/bookmark.routes'),
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
    path: 'extracted-text',
    data: { pageTitle: 'gatewayApp.ocrServiceExtractedText.home.title' },
    loadChildren: () => import('./ocrService/extracted-text/extracted-text.routes'),
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
    path: 'semantic-search',
    data: { pageTitle: 'gatewayApp.searchServiceSemanticSearch.home.title' },
    loadChildren: () => import('./searchService/semantic-search/semantic-search.routes'),
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
    path: 'webhook-subscription',
    data: { pageTitle: 'gatewayApp.notificationServiceWebhookSubscription.home.title' },
    loadChildren: () => import('./notificationService/webhook-subscription/webhook-subscription.routes'),
  },
  {
    path: 'webhook-log',
    data: { pageTitle: 'gatewayApp.notificationServiceWebhookLog.home.title' },
    loadChildren: () => import('./notificationService/webhook-log/webhook-log.routes'),
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
    path: 'approval-history',
    data: { pageTitle: 'gatewayApp.workflowServiceApprovalHistory.home.title' },
    loadChildren: () => import('./workflowService/approval-history/approval-history.routes'),
  },
  {
    path: 'auto-tag-job',
    data: { pageTitle: 'gatewayApp.aiServiceAutoTagJob.home.title' },
    loadChildren: () => import('./aiService/auto-tag-job/auto-tag-job.routes'),
  },
  {
    path: 'tag-prediction',
    data: { pageTitle: 'gatewayApp.aiServiceTagPrediction.home.title' },
    loadChildren: () => import('./aiService/tag-prediction/tag-prediction.routes'),
  },
  {
    path: 'correspondent-extraction',
    data: { pageTitle: 'gatewayApp.aiServiceCorrespondentExtraction.home.title' },
    loadChildren: () => import('./aiService/correspondent-extraction/correspondent-extraction.routes'),
  },
  {
    path: 'correspondent',
    data: { pageTitle: 'gatewayApp.aiServiceCorrespondent.home.title' },
    loadChildren: () => import('./aiService/correspondent/correspondent.routes'),
  },
  {
    path: 'language-detection',
    data: { pageTitle: 'gatewayApp.aiServiceLanguageDetection.home.title' },
    loadChildren: () => import('./aiService/language-detection/language-detection.routes'),
  },
  {
    path: 'ai-cache',
    data: { pageTitle: 'gatewayApp.aiServiceAiCache.home.title' },
    loadChildren: () => import('./aiService/ai-cache/ai-cache.routes'),
  },
  {
    path: 'document-similarity',
    data: { pageTitle: 'gatewayApp.similarityServiceDocumentSimilarity.home.title' },
    loadChildren: () => import('./similarityService/document-similarity/document-similarity.routes'),
  },
  {
    path: 'similarity-job',
    data: { pageTitle: 'gatewayApp.similarityServiceSimilarityJob.home.title' },
    loadChildren: () => import('./similarityService/similarity-job/similarity-job.routes'),
  },
  {
    path: 'document-fingerprint',
    data: { pageTitle: 'gatewayApp.similarityServiceDocumentFingerprint.home.title' },
    loadChildren: () => import('./similarityService/document-fingerprint/document-fingerprint.routes'),
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
    path: 'email-import',
    data: { pageTitle: 'gatewayApp.emailImportServiceEmailImport.home.title' },
    loadChildren: () => import('./emailImportService/email-import/email-import.routes'),
  },
  {
    path: 'email-attachment',
    data: { pageTitle: 'gatewayApp.emailImportServiceEmailAttachment.home.title' },
    loadChildren: () => import('./emailImportService/email-attachment/email-attachment.routes'),
  },
  {
    path: 'import-rule',
    data: { pageTitle: 'gatewayApp.emailImportServiceImportRule.home.title' },
    loadChildren: () => import('./emailImportService/import-rule/import-rule.routes'),
  },
  {
    path: 'import-mapping',
    data: { pageTitle: 'gatewayApp.emailImportServiceImportMapping.home.title' },
    loadChildren: () => import('./emailImportService/import-mapping/import-mapping.routes'),
  },
  {
    path: 'scan-job',
    data: { pageTitle: 'gatewayApp.scanServiceScanJob.home.title' },
    loadChildren: () => import('./scanService/scan-job/scan-job.routes'),
  },
  {
    path: 'scan-batch',
    data: { pageTitle: 'gatewayApp.scanServiceScanBatch.home.title' },
    loadChildren: () => import('./scanService/scan-batch/scan-batch.routes'),
  },
  {
    path: 'scanner-configuration',
    data: { pageTitle: 'gatewayApp.scanServiceScannerConfiguration.home.title' },
    loadChildren: () => import('./scanService/scanner-configuration/scanner-configuration.routes'),
  },
  {
    path: 'scanned-page',
    data: { pageTitle: 'gatewayApp.scanServiceScannedPage.home.title' },
    loadChildren: () => import('./scanService/scanned-page/scanned-page.routes'),
  },
  {
    path: 'conversion-job',
    data: { pageTitle: 'gatewayApp.transformServiceConversionJob.home.title' },
    loadChildren: () => import('./transformService/conversion-job/conversion-job.routes'),
  },
  {
    path: 'watermark-job',
    data: { pageTitle: 'gatewayApp.transformServiceWatermarkJob.home.title' },
    loadChildren: () => import('./transformService/watermark-job/watermark-job.routes'),
  },
  {
    path: 'redaction-job',
    data: { pageTitle: 'gatewayApp.transformServiceRedactionJob.home.title' },
    loadChildren: () => import('./transformService/redaction-job/redaction-job.routes'),
  },
  {
    path: 'compression-job',
    data: { pageTitle: 'gatewayApp.transformServiceCompressionJob.home.title' },
    loadChildren: () => import('./transformService/compression-job/compression-job.routes'),
  },
  {
    path: 'merge-job',
    data: { pageTitle: 'gatewayApp.transformServiceMergeJob.home.title' },
    loadChildren: () => import('./transformService/merge-job/merge-job.routes'),
  },
  {
    path: 'comparison-job',
    data: { pageTitle: 'gatewayApp.transformServiceComparisonJob.home.title' },
    loadChildren: () => import('./transformService/comparison-job/comparison-job.routes'),
  },
  {
    path: 'invoice',
    data: { pageTitle: 'gatewayApp.businessDocServiceInvoice.home.title' },
    loadChildren: () => import('./businessDocService/invoice/invoice.routes'),
  },
  {
    path: 'invoice-line',
    data: { pageTitle: 'gatewayApp.businessDocServiceInvoiceLine.home.title' },
    loadChildren: () => import('./businessDocService/invoice-line/invoice-line.routes'),
  },
  {
    path: 'contract',
    data: { pageTitle: 'gatewayApp.businessDocServiceContract.home.title' },
    loadChildren: () => import('./businessDocService/contract/contract.routes'),
  },
  {
    path: 'contract-clause',
    data: { pageTitle: 'gatewayApp.businessDocServiceContractClause.home.title' },
    loadChildren: () => import('./businessDocService/contract-clause/contract-clause.routes'),
  },
  {
    path: 'bank-statement',
    data: { pageTitle: 'gatewayApp.businessDocServiceBankStatement.home.title' },
    loadChildren: () => import('./businessDocService/bank-statement/bank-statement.routes'),
  },
  {
    path: 'bank-transaction',
    data: { pageTitle: 'gatewayApp.businessDocServiceBankTransaction.home.title' },
    loadChildren: () => import('./businessDocService/bank-transaction/bank-transaction.routes'),
  },
  {
    path: 'manual',
    data: { pageTitle: 'gatewayApp.businessDocServiceManual.home.title' },
    loadChildren: () => import('./businessDocService/manual/manual.routes'),
  },
  {
    path: 'manual-chapter',
    data: { pageTitle: 'gatewayApp.businessDocServiceManualChapter.home.title' },
    loadChildren: () => import('./businessDocService/manual-chapter/manual-chapter.routes'),
  },
  {
    path: 'dashboard',
    data: { pageTitle: 'gatewayApp.reportingServiceDashboard.home.title' },
    loadChildren: () => import('./reportingService/dashboard/dashboard.routes'),
  },
  {
    path: 'dashboard-widget',
    data: { pageTitle: 'gatewayApp.reportingServiceDashboardWidget.home.title' },
    loadChildren: () => import('./reportingService/dashboard-widget/dashboard-widget.routes'),
  },
  {
    path: 'scheduled-report',
    data: { pageTitle: 'gatewayApp.reportingServiceScheduledReport.home.title' },
    loadChildren: () => import('./reportingService/scheduled-report/scheduled-report.routes'),
  },
  {
    path: 'report-execution',
    data: { pageTitle: 'gatewayApp.reportingServiceReportExecution.home.title' },
    loadChildren: () => import('./reportingService/report-execution/report-execution.routes'),
  },
  {
    path: 'performance-metric',
    data: { pageTitle: 'gatewayApp.reportingServicePerformanceMetric.home.title' },
    loadChildren: () => import('./reportingService/performance-metric/performance-metric.routes'),
  },
  {
    path: 'system-metric',
    data: { pageTitle: 'gatewayApp.reportingServiceSystemMetric.home.title' },
    loadChildren: () => import('./reportingService/system-metric/system-metric.routes'),
  },
  {
    path: 'alert',
    data: { pageTitle: 'gatewayApp.monitoringServiceAlert.home.title' },
    loadChildren: () => import('./monitoringService/alert/alert.routes'),
  },
  {
    path: 'alert-rule',
    data: { pageTitle: 'gatewayApp.monitoringServiceAlertRule.home.title' },
    loadChildren: () => import('./monitoringService/alert-rule/alert-rule.routes'),
  },
  {
    path: 'document-watch',
    data: { pageTitle: 'gatewayApp.monitoringServiceDocumentWatch.home.title' },
    loadChildren: () => import('./monitoringService/document-watch/document-watch.routes'),
  },
  {
    path: 'maintenance-task',
    data: { pageTitle: 'gatewayApp.monitoringServiceMaintenanceTask.home.title' },
    loadChildren: () => import('./monitoringService/maintenance-task/maintenance-task.routes'),
  },
  {
    path: 'system-health',
    data: { pageTitle: 'gatewayApp.monitoringServiceSystemHealth.home.title' },
    loadChildren: () => import('./monitoringService/system-health/system-health.routes'),
  },
  {
    path: 'service-status',
    data: { pageTitle: 'gatewayApp.monitoringServiceServiceStatus.home.title' },
    loadChildren: () => import('./monitoringService/service-status/service-status.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
