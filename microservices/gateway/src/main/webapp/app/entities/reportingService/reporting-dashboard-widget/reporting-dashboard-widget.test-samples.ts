import { IReportingDashboardWidget, NewReportingDashboardWidget } from './reporting-dashboard-widget.model';

export const sampleWithRequiredData: IReportingDashboardWidget = {
  id: 180,
  widgetType: 'HEATMAP',
  title: 'for',
  configuration: '../fake-data/blob/hipster.txt',
  position: 7990,
  sizeX: 492,
  sizeY: 16203,
};

export const sampleWithPartialData: IReportingDashboardWidget = {
  id: 29024,
  widgetType: 'GAUGE',
  title: 'suspension',
  configuration: '../fake-data/blob/hipster.txt',
  position: 6940,
  sizeX: 12444,
  sizeY: 17143,
};

export const sampleWithFullData: IReportingDashboardWidget = {
  id: 265,
  widgetType: 'DOCUMENT_COUNT',
  title: 'ack ah',
  configuration: '../fake-data/blob/hipster.txt',
  dataSource: 'underneath considering',
  position: 4296,
  sizeX: 6143,
  sizeY: 31461,
};

export const sampleWithNewData: NewReportingDashboardWidget = {
  widgetType: 'USER_ACTIVITY',
  title: 'ha but',
  configuration: '../fake-data/blob/hipster.txt',
  position: 15260,
  sizeX: 2529,
  sizeY: 1694,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
