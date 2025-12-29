mvnArray=("paperdms-common")
msArray=("gateway"  "documentService" "ocrService" "searchService" "notificationService" "workflowService" "aiService" "similarityService" "archiveService" "exportService" "emailImportService"
 "transformService"  "reportingService" "monitoringService" )

for str in ${mvnArray[@]}; do
  cd $str
  mvn clean install 
  cd ..
done

for str in ${msArray[@]}; do
  cd $str
  ./mvnw -DskipTests -Pprod package jib:dockerBuild
  cd ..
done

 



