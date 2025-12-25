import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IEmailImport } from '../email-import.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../email-import.test-samples';

import { EmailImportService, RestEmailImport } from './email-import.service';

const requireRestSample: RestEmailImport = {
  ...sampleWithRequiredData,
  receivedDate: sampleWithRequiredData.receivedDate?.toJSON(),
  processedDate: sampleWithRequiredData.processedDate?.toJSON(),
};

describe('EmailImport Service', () => {
  let service: EmailImportService;
  let httpMock: HttpTestingController;
  let expectedResult: IEmailImport | IEmailImport[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(EmailImportService);
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

    it('should create a EmailImport', () => {
      const emailImport = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(emailImport).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a EmailImport', () => {
      const emailImport = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(emailImport).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a EmailImport', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of EmailImport', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a EmailImport', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addEmailImportToCollectionIfMissing', () => {
      it('should add a EmailImport to an empty array', () => {
        const emailImport: IEmailImport = sampleWithRequiredData;
        expectedResult = service.addEmailImportToCollectionIfMissing([], emailImport);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(emailImport);
      });

      it('should not add a EmailImport to an array that contains it', () => {
        const emailImport: IEmailImport = sampleWithRequiredData;
        const emailImportCollection: IEmailImport[] = [
          {
            ...emailImport,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addEmailImportToCollectionIfMissing(emailImportCollection, emailImport);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a EmailImport to an array that doesn't contain it", () => {
        const emailImport: IEmailImport = sampleWithRequiredData;
        const emailImportCollection: IEmailImport[] = [sampleWithPartialData];
        expectedResult = service.addEmailImportToCollectionIfMissing(emailImportCollection, emailImport);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(emailImport);
      });

      it('should add only unique EmailImport to an array', () => {
        const emailImportArray: IEmailImport[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const emailImportCollection: IEmailImport[] = [sampleWithRequiredData];
        expectedResult = service.addEmailImportToCollectionIfMissing(emailImportCollection, ...emailImportArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const emailImport: IEmailImport = sampleWithRequiredData;
        const emailImport2: IEmailImport = sampleWithPartialData;
        expectedResult = service.addEmailImportToCollectionIfMissing([], emailImport, emailImport2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(emailImport);
        expect(expectedResult).toContain(emailImport2);
      });

      it('should accept null and undefined values', () => {
        const emailImport: IEmailImport = sampleWithRequiredData;
        expectedResult = service.addEmailImportToCollectionIfMissing([], null, emailImport, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(emailImport);
      });

      it('should return initial array if no EmailImport is added', () => {
        const emailImportCollection: IEmailImport[] = [sampleWithRequiredData];
        expectedResult = service.addEmailImportToCollectionIfMissing(emailImportCollection, undefined, null);
        expect(expectedResult).toEqual(emailImportCollection);
      });
    });

    describe('compareEmailImport', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareEmailImport(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 3064 };
        const entity2 = null;

        const compareResult1 = service.compareEmailImport(entity1, entity2);
        const compareResult2 = service.compareEmailImport(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 3064 };
        const entity2 = { id: 14958 };

        const compareResult1 = service.compareEmailImport(entity1, entity2);
        const compareResult2 = service.compareEmailImport(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 3064 };
        const entity2 = { id: 3064 };

        const compareResult1 = service.compareEmailImport(entity1, entity2);
        const compareResult2 = service.compareEmailImport(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
