import { IReportingDashboard } from 'app/entities/reportingService/reporting-dashboard/reporting-dashboard.model';
import { WidgetType } from 'app/entities/enumerations/widget-type.model';

export interface IReportingDashboardWidget {
  id: number;
  widgetType?: keyof typeof WidgetType | null;
  title?: string | null;
  configuration?: string | null;
  dataSource?: string | null;
  position?: number | null;
  sizeX?: number | null;
  sizeY?: number | null;
  dashboar?: Pick<IReportingDashboard, 'id'> | null;
}

export type NewReportingDashboardWidget = Omit<IReportingDashboardWidget, 'id'> & { id: null };
