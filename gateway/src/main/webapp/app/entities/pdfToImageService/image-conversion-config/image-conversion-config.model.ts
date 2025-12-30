import dayjs from 'dayjs/esm';
import { ImageQuality } from 'app/entities/enumerations/image-quality.model';
import { ImageFormat } from 'app/entities/enumerations/image-format.model';
import { ConversionType } from 'app/entities/enumerations/conversion-type.model';

export interface IImageConversionConfig {
  id: number;
  configName?: string | null;
  description?: string | null;
  defaultQuality?: keyof typeof ImageQuality | null;
  defaultFormat?: keyof typeof ImageFormat | null;
  defaultDpi?: number | null;
  defaultConversionType?: keyof typeof ConversionType | null;
  defaultPriority?: number | null;
  isActive?: boolean | null;
  isDefault?: boolean | null;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
}

export type NewImageConversionConfig = Omit<IImageConversionConfig, 'id'> & { id: null };
