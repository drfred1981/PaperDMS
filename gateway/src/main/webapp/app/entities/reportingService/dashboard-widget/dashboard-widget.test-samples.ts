import { IDashboardWidget, NewDashboardWidget } from './dashboard-widget.model';

export const sampleWithRequiredData: IDashboardWidget = {
  id: 17343,
  dashboardId: 15618,
  widgetType: 'CHART_BAR',
  title: 'clamour yellow athwart',
  configuration: '../fake-data/blob/hipster.txt',
  position: 21488,
  sizeX: 17002,
  sizeY: 27120,
};

export const sampleWithPartialData: IDashboardWidget = {
  id: 15701,
  dashboardId: 19597,
  widgetType: 'USER_ACTIVITY',
  title: 'doodle',
  configuration: '../fake-data/blob/hipster.txt',
  dataSource: 'across absent till',
  position: 28991,
  sizeX: 26837,
  sizeY: 15073,
};

export const sampleWithFullData: IDashboardWidget = {
  id: 12596,
  dashboardId: 25759,
  widgetType: 'STORAGE_USAGE',
  title: 'ascribe independence',
  configuration: '../fake-data/blob/hipster.txt',
  dataSource: 'hydrocarbon politely via',
  position: 20947,
  sizeX: 29156,
  sizeY: 21939,
};

export const sampleWithNewData: NewDashboardWidget = {
  dashboardId: 22044,
  widgetType: 'STORAGE_USAGE',
  title: 'madly',
  configuration: '../fake-data/blob/hipster.txt',
  position: 2184,
  sizeX: 21595,
  sizeY: 27643,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
