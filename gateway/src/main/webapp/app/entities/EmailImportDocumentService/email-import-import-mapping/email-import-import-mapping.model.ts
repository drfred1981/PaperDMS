import { IEmailImportImportRule } from 'app/entities/EmailImportDocumentService/email-import-import-rule/email-import-import-rule.model';
import { EmailField } from 'app/entities/enumerations/email-field.model';
import { MappingTransformation } from 'app/entities/enumerations/mapping-transformation.model';

export interface IEmailImportImportMapping {
  id: number;
  emailField?: keyof typeof EmailField | null;
  documentField?: string | null;
  transformation?: keyof typeof MappingTransformation | null;
  transformationConfig?: string | null;
  isRequired?: boolean | null;
  defaultValue?: string | null;
  validationRegex?: string | null;
  rule?: Pick<IEmailImportImportRule, 'id'> | null;
}

export type NewEmailImportImportMapping = Omit<IEmailImportImportMapping, 'id'> & { id: null };
