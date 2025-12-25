import dayjs from 'dayjs/esm';
import { IFolder } from 'app/entities/documentService/folder/folder.model';
import { IDocumentType } from 'app/entities/documentService/document-type/document-type.model';

export interface IDocument {
  id: number;
  title?: string | null;
  fileName?: string | null;
  fileSize?: number | null;
  mimeType?: string | null;
  sha256?: string | null;
  s3Key?: string | null;
  s3Bucket?: string | null;
  s3Region?: string | null;
  s3Etag?: string | null;
  thumbnailS3Key?: string | null;
  thumbnailSha256?: string | null;
  webpPreviewS3Key?: string | null;
  webpPreviewSha256?: string | null;
  uploadDate?: dayjs.Dayjs | null;
  isPublic?: boolean | null;
  downloadCount?: number | null;
  viewCount?: number | null;
  detectedLanguage?: string | null;
  manualLanguage?: string | null;
  languageConfidence?: number | null;
  pageCount?: number | null;
  createdDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  folder?: Pick<IFolder, 'id'> | null;
  documentType?: Pick<IDocumentType, 'id'> | null;
}

export type NewDocument = Omit<IDocument, 'id'> & { id: null };
