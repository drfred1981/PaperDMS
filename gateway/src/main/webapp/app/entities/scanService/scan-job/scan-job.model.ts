import dayjs from 'dayjs/esm';

import { ColorMode } from 'app/entities/enumerations/color-mode.model';
import { ScanFormat } from 'app/entities/enumerations/scan-format.model';
import { ScanStatus } from 'app/entities/enumerations/scan-status.model';
import { IScanBatch } from 'app/entities/scanService/scan-batch/scan-batch.model';
import { IScannerConfiguration } from 'app/entities/scanService/scanner-configuration/scanner-configuration.model';

export interface IScanJob {
  id: number;
  name?: string | null;
  description?: string | null;
  scannerConfigId?: number | null;
  batchId?: number | null;
  documentTypeId?: number | null;
  folderId?: number | null;
  pageCount?: number | null;
  status?: keyof typeof ScanStatus | null;
  colorMode?: keyof typeof ColorMode | null;
  resolution?: number | null;
  fileFormat?: keyof typeof ScanFormat | null;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  errorMessage?: string | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  scannerConfig?: Pick<IScannerConfiguration, 'id'> | null;
  batch?: Pick<IScanBatch, 'id'> | null;
}

export type NewScanJob = Omit<IScanJob, 'id'> & { id: null };
