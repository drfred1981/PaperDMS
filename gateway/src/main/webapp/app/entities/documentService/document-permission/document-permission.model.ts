import dayjs from 'dayjs/esm';
import { IPermissionGroup } from 'app/entities/documentService/permission-group/permission-group.model';
import { PrincipalType } from 'app/entities/enumerations/principal-type.model';
import { PermissionType } from 'app/entities/enumerations/permission-type.model';

export interface IDocumentPermission {
  id: number;
  documentId?: number | null;
  principalType?: keyof typeof PrincipalType | null;
  principalId?: string | null;
  permission?: keyof typeof PermissionType | null;
  canDelegate?: boolean | null;
  grantedBy?: string | null;
  grantedDate?: dayjs.Dayjs | null;
  permissionGroup?: Pick<IPermissionGroup, 'id'> | null;
}

export type NewDocumentPermission = Omit<IDocumentPermission, 'id'> & { id: null };
