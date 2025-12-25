import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IMaintenanceTask } from '../maintenance-task.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../maintenance-task.test-samples';

import { MaintenanceTaskService, RestMaintenanceTask } from './maintenance-task.service';

const requireRestSample: RestMaintenanceTask = {
  ...sampleWithRequiredData,
  lastRun: sampleWithRequiredData.lastRun?.toJSON(),
  nextRun: sampleWithRequiredData.nextRun?.toJSON(),
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
};

describe('MaintenanceTask Service', () => {
  let service: MaintenanceTaskService;
  let httpMock: HttpTestingController;
  let expectedResult: IMaintenanceTask | IMaintenanceTask[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(MaintenanceTaskService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a MaintenanceTask', () => {
      const maintenanceTask = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(maintenanceTask).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a MaintenanceTask', () => {
      const maintenanceTask = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(maintenanceTask).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a MaintenanceTask', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of MaintenanceTask', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a MaintenanceTask', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addMaintenanceTaskToCollectionIfMissing', () => {
      it('should add a MaintenanceTask to an empty array', () => {
        const maintenanceTask: IMaintenanceTask = sampleWithRequiredData;
        expectedResult = service.addMaintenanceTaskToCollectionIfMissing([], maintenanceTask);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(maintenanceTask);
      });

      it('should not add a MaintenanceTask to an array that contains it', () => {
        const maintenanceTask: IMaintenanceTask = sampleWithRequiredData;
        const maintenanceTaskCollection: IMaintenanceTask[] = [
          {
            ...maintenanceTask,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMaintenanceTaskToCollectionIfMissing(maintenanceTaskCollection, maintenanceTask);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a MaintenanceTask to an array that doesn't contain it", () => {
        const maintenanceTask: IMaintenanceTask = sampleWithRequiredData;
        const maintenanceTaskCollection: IMaintenanceTask[] = [sampleWithPartialData];
        expectedResult = service.addMaintenanceTaskToCollectionIfMissing(maintenanceTaskCollection, maintenanceTask);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(maintenanceTask);
      });

      it('should add only unique MaintenanceTask to an array', () => {
        const maintenanceTaskArray: IMaintenanceTask[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const maintenanceTaskCollection: IMaintenanceTask[] = [sampleWithRequiredData];
        expectedResult = service.addMaintenanceTaskToCollectionIfMissing(maintenanceTaskCollection, ...maintenanceTaskArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const maintenanceTask: IMaintenanceTask = sampleWithRequiredData;
        const maintenanceTask2: IMaintenanceTask = sampleWithPartialData;
        expectedResult = service.addMaintenanceTaskToCollectionIfMissing([], maintenanceTask, maintenanceTask2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(maintenanceTask);
        expect(expectedResult).toContain(maintenanceTask2);
      });

      it('should accept null and undefined values', () => {
        const maintenanceTask: IMaintenanceTask = sampleWithRequiredData;
        expectedResult = service.addMaintenanceTaskToCollectionIfMissing([], null, maintenanceTask, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(maintenanceTask);
      });

      it('should return initial array if no MaintenanceTask is added', () => {
        const maintenanceTaskCollection: IMaintenanceTask[] = [sampleWithRequiredData];
        expectedResult = service.addMaintenanceTaskToCollectionIfMissing(maintenanceTaskCollection, undefined, null);
        expect(expectedResult).toEqual(maintenanceTaskCollection);
      });
    });

    describe('compareMaintenanceTask', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMaintenanceTask(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 23869 };
        const entity2 = null;

        const compareResult1 = service.compareMaintenanceTask(entity1, entity2);
        const compareResult2 = service.compareMaintenanceTask(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 23869 };
        const entity2 = { id: 10127 };

        const compareResult1 = service.compareMaintenanceTask(entity1, entity2);
        const compareResult2 = service.compareMaintenanceTask(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 23869 };
        const entity2 = { id: 23869 };

        const compareResult1 = service.compareMaintenanceTask(entity1, entity2);
        const compareResult2 = service.compareMaintenanceTask(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
