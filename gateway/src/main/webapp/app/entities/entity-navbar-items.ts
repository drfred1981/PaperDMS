import NavbarItem from 'app/layouts/navbar/navbar-item.model';

export const EntityNavbarItems: NavbarItem[] = [
  {
    name: 'Document',
    route: '/document',
    translationKey: 'global.menu.entities.documentServiceDocument',
  },
  {
    name: 'DocumentServiceStatus',
    route: '/document-service-status',
    translationKey: 'global.menu.entities.documentServiceDocumentServiceStatus',
  },
  {
    name: 'Folder',
    route: '/folder',
    translationKey: 'global.menu.entities.documentServiceFolder',
  },
  {
    name: 'Tag',
    route: '/tag',
    translationKey: 'global.menu.entities.documentServiceTag',
  },
  {
    name: 'TagCategory',
    route: '/tag-category',
    translationKey: 'global.menu.entities.documentServiceTagCategory',
  },
  {
    name: 'DocumentTag',
    route: '/document-tag',
    translationKey: 'global.menu.entities.documentServiceDocumentTag',
  },
  {
    name: 'DocumentType',
    route: '/document-type',
    translationKey: 'global.menu.entities.documentServiceDocumentType',
  },
  {
    name: 'DocumentTypeField',
    route: '/document-type-field',
    translationKey: 'global.menu.entities.documentServiceDocumentTypeField',
  },
  {
    name: 'DocumentMetadata',
    route: '/document-metadata',
    translationKey: 'global.menu.entities.documentServiceDocumentMetadata',
  },
  {
    name: 'ExtractedField',
    route: '/extracted-field',
    translationKey: 'global.menu.entities.documentServiceExtractedField',
  },
  {
    name: 'DocumentPermission',
    route: '/document-permission',
    translationKey: 'global.menu.entities.documentServiceDocumentPermission',
  },
  {
    name: 'PermissionGroup',
    route: '/permission-group',
    translationKey: 'global.menu.entities.documentServicePermissionGroup',
  },
  {
    name: 'DocumentAudit',
    route: '/document-audit',
    translationKey: 'global.menu.entities.documentServiceDocumentAudit',
  },
  {
    name: 'DocumentComment',
    route: '/document-comment',
    translationKey: 'global.menu.entities.documentServiceDocumentComment',
  },
  {
    name: 'DocumentRelation',
    route: '/document-relation',
    translationKey: 'global.menu.entities.documentServiceDocumentRelation',
  },
  {
    name: 'DocumentStatistics',
    route: '/document-statistics',
    translationKey: 'global.menu.entities.documentServiceDocumentStatistics',
  },
  {
    name: 'DocumentTemplate',
    route: '/document-template',
    translationKey: 'global.menu.entities.documentServiceDocumentTemplate',
  },
  {
    name: 'SavedSearch',
    route: '/saved-search',
    translationKey: 'global.menu.entities.documentServiceSavedSearch',
  },
  {
    name: 'SmartFolder',
    route: '/smart-folder',
    translationKey: 'global.menu.entities.documentServiceSmartFolder',
  },
  {
    name: 'Bookmark',
    route: '/bookmark',
    translationKey: 'global.menu.entities.documentServiceBookmark',
  },
  {
    name: 'DocumentVersion',
    route: '/document-version',
    translationKey: 'global.menu.entities.documentServiceDocumentVersion',
  },
  {
    name: 'DocumentProcess',
    route: '/document-process',
    translationKey: 'global.menu.entities.documentProcess',
  },
  {
    name: 'OcrJob',
    route: '/ocr-job',
    translationKey: 'global.menu.entities.ocrServiceOcrJob',
  },
  {
    name: 'OcrResult',
    route: '/ocr-result',
    translationKey: 'global.menu.entities.ocrServiceOcrResult',
  },
  {
    name: 'ExtractedText',
    route: '/extracted-text',
    translationKey: 'global.menu.entities.ocrServiceExtractedText',
  },
  {
    name: 'OcrComparison',
    route: '/ocr-comparison',
    translationKey: 'global.menu.entities.ocrServiceOcrComparison',
  },
  {
    name: 'OcrCache',
    route: '/ocr-cache',
    translationKey: 'global.menu.entities.ocrServiceOcrCache',
  },
  {
    name: 'SearchIndex',
    route: '/search-index',
    translationKey: 'global.menu.entities.searchServiceSearchIndex',
  },
  {
    name: 'SearchQuery',
    route: '/search-query',
    translationKey: 'global.menu.entities.searchServiceSearchQuery',
  },
  {
    name: 'SemanticSearch',
    route: '/semantic-search',
    translationKey: 'global.menu.entities.searchServiceSemanticSearch',
  },
  {
    name: 'SearchFacet',
    route: '/search-facet',
    translationKey: 'global.menu.entities.searchServiceSearchFacet',
  },
  {
    name: 'Notification',
    route: '/notification',
    translationKey: 'global.menu.entities.notificationServiceNotification',
  },
  {
    name: 'NotificationTemplate',
    route: '/notification-template',
    translationKey: 'global.menu.entities.notificationServiceNotificationTemplate',
  },
  {
    name: 'NotificationPreference',
    route: '/notification-preference',
    translationKey: 'global.menu.entities.notificationServiceNotificationPreference',
  },
  {
    name: 'NotificationEvent',
    route: '/notification-event',
    translationKey: 'global.menu.entities.notificationServiceNotificationEvent',
  },
  {
    name: 'WebhookSubscription',
    route: '/webhook-subscription',
    translationKey: 'global.menu.entities.notificationServiceWebhookSubscription',
  },
  {
    name: 'WebhookLog',
    route: '/webhook-log',
    translationKey: 'global.menu.entities.notificationServiceWebhookLog',
  },
  {
    name: 'Workflow',
    route: '/workflow',
    translationKey: 'global.menu.entities.workflowServiceWorkflow',
  },
  {
    name: 'WorkflowStep',
    route: '/workflow-step',
    translationKey: 'global.menu.entities.workflowServiceWorkflowStep',
  },
  {
    name: 'WorkflowInstance',
    route: '/workflow-instance',
    translationKey: 'global.menu.entities.workflowServiceWorkflowInstance',
  },
  {
    name: 'WorkflowTask',
    route: '/workflow-task',
    translationKey: 'global.menu.entities.workflowServiceWorkflowTask',
  },
  {
    name: 'ApprovalHistory',
    route: '/approval-history',
    translationKey: 'global.menu.entities.workflowServiceApprovalHistory',
  },
  {
    name: 'AutoTagJob',
    route: '/auto-tag-job',
    translationKey: 'global.menu.entities.aiServiceAutoTagJob',
  },
  {
    name: 'TagPrediction',
    route: '/tag-prediction',
    translationKey: 'global.menu.entities.aiServiceTagPrediction',
  },
  {
    name: 'CorrespondentExtraction',
    route: '/correspondent-extraction',
    translationKey: 'global.menu.entities.aiServiceCorrespondentExtraction',
  },
  {
    name: 'Correspondent',
    route: '/correspondent',
    translationKey: 'global.menu.entities.aiServiceCorrespondent',
  },
  {
    name: 'LanguageDetection',
    route: '/language-detection',
    translationKey: 'global.menu.entities.aiServiceLanguageDetection',
  },
  {
    name: 'AiCache',
    route: '/ai-cache',
    translationKey: 'global.menu.entities.aiServiceAiCache',
  },
  {
    name: 'DocumentSimilarity',
    route: '/document-similarity',
    translationKey: 'global.menu.entities.similarityServiceDocumentSimilarity',
  },
  {
    name: 'SimilarityJob',
    route: '/similarity-job',
    translationKey: 'global.menu.entities.similarityServiceSimilarityJob',
  },
  {
    name: 'DocumentFingerprint',
    route: '/document-fingerprint',
    translationKey: 'global.menu.entities.similarityServiceDocumentFingerprint',
  },
  {
    name: 'SimilarityCluster',
    route: '/similarity-cluster',
    translationKey: 'global.menu.entities.similarityServiceSimilarityCluster',
  },
  {
    name: 'ArchiveJob',
    route: '/archive-job',
    translationKey: 'global.menu.entities.archiveServiceArchiveJob',
  },
  {
    name: 'ArchiveDocument',
    route: '/archive-document',
    translationKey: 'global.menu.entities.archiveServiceArchiveDocument',
  },
  {
    name: 'ExportJob',
    route: '/export-job',
    translationKey: 'global.menu.entities.exportServiceExportJob',
  },
  {
    name: 'ExportPattern',
    route: '/export-pattern',
    translationKey: 'global.menu.entities.exportServiceExportPattern',
  },
  {
    name: 'ExportResult',
    route: '/export-result',
    translationKey: 'global.menu.entities.exportServiceExportResult',
  },
  {
    name: 'EmailImport',
    route: '/email-import',
    translationKey: 'global.menu.entities.emailImportServiceEmailImport',
  },
  {
    name: 'EmailAttachment',
    route: '/email-attachment',
    translationKey: 'global.menu.entities.emailImportServiceEmailAttachment',
  },
  {
    name: 'ImportRule',
    route: '/import-rule',
    translationKey: 'global.menu.entities.emailImportServiceImportRule',
  },
  {
    name: 'ImportMapping',
    route: '/import-mapping',
    translationKey: 'global.menu.entities.emailImportServiceImportMapping',
  },
  {
    name: 'ScanJob',
    route: '/scan-job',
    translationKey: 'global.menu.entities.scanServiceScanJob',
  },
  {
    name: 'ScanBatch',
    route: '/scan-batch',
    translationKey: 'global.menu.entities.scanServiceScanBatch',
  },
  {
    name: 'ScannerConfiguration',
    route: '/scanner-configuration',
    translationKey: 'global.menu.entities.scanServiceScannerConfiguration',
  },
  {
    name: 'ScannedPage',
    route: '/scanned-page',
    translationKey: 'global.menu.entities.scanServiceScannedPage',
  },
  {
    name: 'ConversionJob',
    route: '/conversion-job',
    translationKey: 'global.menu.entities.transformServiceConversionJob',
  },
  {
    name: 'WatermarkJob',
    route: '/watermark-job',
    translationKey: 'global.menu.entities.transformServiceWatermarkJob',
  },
  {
    name: 'RedactionJob',
    route: '/redaction-job',
    translationKey: 'global.menu.entities.transformServiceRedactionJob',
  },
  {
    name: 'CompressionJob',
    route: '/compression-job',
    translationKey: 'global.menu.entities.transformServiceCompressionJob',
  },
  {
    name: 'MergeJob',
    route: '/merge-job',
    translationKey: 'global.menu.entities.transformServiceMergeJob',
  },
  {
    name: 'ComparisonJob',
    route: '/comparison-job',
    translationKey: 'global.menu.entities.transformServiceComparisonJob',
  },
  {
    name: 'Invoice',
    route: '/invoice',
    translationKey: 'global.menu.entities.businessDocServiceInvoice',
  },
  {
    name: 'InvoiceLine',
    route: '/invoice-line',
    translationKey: 'global.menu.entities.businessDocServiceInvoiceLine',
  },
  {
    name: 'Contract',
    route: '/contract',
    translationKey: 'global.menu.entities.businessDocServiceContract',
  },
  {
    name: 'ContractClause',
    route: '/contract-clause',
    translationKey: 'global.menu.entities.businessDocServiceContractClause',
  },
  {
    name: 'BankStatement',
    route: '/bank-statement',
    translationKey: 'global.menu.entities.businessDocServiceBankStatement',
  },
  {
    name: 'BankTransaction',
    route: '/bank-transaction',
    translationKey: 'global.menu.entities.businessDocServiceBankTransaction',
  },
  {
    name: 'Manual',
    route: '/manual',
    translationKey: 'global.menu.entities.businessDocServiceManual',
  },
  {
    name: 'ManualChapter',
    route: '/manual-chapter',
    translationKey: 'global.menu.entities.businessDocServiceManualChapter',
  },
  {
    name: 'Dashboard',
    route: '/dashboard',
    translationKey: 'global.menu.entities.reportingServiceDashboard',
  },
  {
    name: 'DashboardWidget',
    route: '/dashboard-widget',
    translationKey: 'global.menu.entities.reportingServiceDashboardWidget',
  },
  {
    name: 'ScheduledReport',
    route: '/scheduled-report',
    translationKey: 'global.menu.entities.reportingServiceScheduledReport',
  },
  {
    name: 'ReportExecution',
    route: '/report-execution',
    translationKey: 'global.menu.entities.reportingServiceReportExecution',
  },
  {
    name: 'PerformanceMetric',
    route: '/performance-metric',
    translationKey: 'global.menu.entities.reportingServicePerformanceMetric',
  },
  {
    name: 'SystemMetric',
    route: '/system-metric',
    translationKey: 'global.menu.entities.reportingServiceSystemMetric',
  },
  {
    name: 'Alert',
    route: '/alert',
    translationKey: 'global.menu.entities.monitoringServiceAlert',
  },
  {
    name: 'AlertRule',
    route: '/alert-rule',
    translationKey: 'global.menu.entities.monitoringServiceAlertRule',
  },
  {
    name: 'DocumentWatch',
    route: '/document-watch',
    translationKey: 'global.menu.entities.monitoringServiceDocumentWatch',
  },
  {
    name: 'MaintenanceTask',
    route: '/maintenance-task',
    translationKey: 'global.menu.entities.monitoringServiceMaintenanceTask',
  },
  {
    name: 'SystemHealth',
    route: '/system-health',
    translationKey: 'global.menu.entities.monitoringServiceSystemHealth',
  },
  {
    name: 'ServiceStatus',
    route: '/service-status',
    translationKey: 'global.menu.entities.monitoringServiceServiceStatus',
  },
];
