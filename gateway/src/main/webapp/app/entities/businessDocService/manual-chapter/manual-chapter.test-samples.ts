import { IManualChapter, NewManualChapter } from './manual-chapter.model';

export const sampleWithRequiredData: IManualChapter = {
  id: 21969,
  manualId: 16469,
  chapterNumber: 'tricky in sans',
  title: 'nervously',
  level: 13882,
};

export const sampleWithPartialData: IManualChapter = {
  id: 11642,
  manualId: 19443,
  chapterNumber: 'afore round pull',
  title: 'tremendously',
  pageStart: 8458,
  level: 7296,
  displayOrder: 6191,
};

export const sampleWithFullData: IManualChapter = {
  id: 18421,
  manualId: 31659,
  chapterNumber: 'role sunny',
  title: 'round',
  content: '../fake-data/blob/hipster.txt',
  pageStart: 26957,
  pageEnd: 4365,
  level: 3271,
  displayOrder: 30214,
};

export const sampleWithNewData: NewManualChapter = {
  manualId: 4705,
  chapterNumber: 'gee hold',
  title: 'hollow improbable',
  level: 2641,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
