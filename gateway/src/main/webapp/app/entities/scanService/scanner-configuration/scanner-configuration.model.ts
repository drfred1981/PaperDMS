import dayjs from 'dayjs/esm';

import { ColorMode } from 'app/entities/enumerations/color-mode.model';
import { ScanFormat } from 'app/entities/enumerations/scan-format.model';
import { ScannerType } from 'app/entities/enumerations/scanner-type.model';

export interface IScannerConfiguration {
  id: number;
  name?: string | null;
  scannerType?: keyof typeof ScannerType | null;
  ipAddress?: string | null;
  port?: number | null;
  protocol?: string | null;
  manufacturer?: string | null;
  model?: string | null;
  defaultColorMode?: keyof typeof ColorMode | null;
  defaultResolution?: number | null;
  defaultFormat?: keyof typeof ScanFormat | null;
  capabilities?: string | null;
  isActive?: boolean | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedDate?: dayjs.Dayjs | null;
}

export type NewScannerConfiguration = Omit<IScannerConfiguration, 'id'> & { id: null };
