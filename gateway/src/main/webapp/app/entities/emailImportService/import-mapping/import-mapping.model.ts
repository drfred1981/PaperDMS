import { IImportRule } from 'app/entities/emailImportService/import-rule/import-rule.model';
import { EmailField } from 'app/entities/enumerations/email-field.model';
import { MappingTransformation } from 'app/entities/enumerations/mapping-transformation.model';

export interface IImportMapping {
  id: number;
  ruleId?: number | null;
  emailField?: keyof typeof EmailField | null;
  documentField?: string | null;
  transformation?: keyof typeof MappingTransformation | null;
  transformationConfig?: string | null;
  isRequired?: boolean | null;
  defaultValue?: string | null;
  validationRegex?: string | null;
  rule?: Pick<IImportRule, 'id'> | null;
}

export type NewImportMapping = Omit<IImportMapping, 'id'> & { id: null };
