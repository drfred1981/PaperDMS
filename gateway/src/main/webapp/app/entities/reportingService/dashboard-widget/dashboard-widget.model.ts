import { IDashboard } from 'app/entities/reportingService/dashboard/dashboard.model';
import { WidgetType } from 'app/entities/enumerations/widget-type.model';

export interface IDashboardWidget {
  id: number;
  dashboardId?: number | null;
  widgetType?: keyof typeof WidgetType | null;
  title?: string | null;
  configuration?: string | null;
  dataSource?: string | null;
  position?: number | null;
  sizeX?: number | null;
  sizeY?: number | null;
  dashboard?: Pick<IDashboard, 'id'> | null;
}

export type NewDashboardWidget = Omit<IDashboardWidget, 'id'> & { id: null };
