import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ISmartFolder } from '../smart-folder.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../smart-folder.test-samples';

import { RestSmartFolder, SmartFolderService } from './smart-folder.service';

const requireRestSample: RestSmartFolder = {
  ...sampleWithRequiredData,
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
};

describe('SmartFolder Service', () => {
  let service: SmartFolderService;
  let httpMock: HttpTestingController;
  let expectedResult: ISmartFolder | ISmartFolder[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(SmartFolderService);
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

    it('should create a SmartFolder', () => {
      const smartFolder = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(smartFolder).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SmartFolder', () => {
      const smartFolder = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(smartFolder).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SmartFolder', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SmartFolder', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a SmartFolder', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addSmartFolderToCollectionIfMissing', () => {
      it('should add a SmartFolder to an empty array', () => {
        const smartFolder: ISmartFolder = sampleWithRequiredData;
        expectedResult = service.addSmartFolderToCollectionIfMissing([], smartFolder);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(smartFolder);
      });

      it('should not add a SmartFolder to an array that contains it', () => {
        const smartFolder: ISmartFolder = sampleWithRequiredData;
        const smartFolderCollection: ISmartFolder[] = [
          {
            ...smartFolder,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSmartFolderToCollectionIfMissing(smartFolderCollection, smartFolder);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SmartFolder to an array that doesn't contain it", () => {
        const smartFolder: ISmartFolder = sampleWithRequiredData;
        const smartFolderCollection: ISmartFolder[] = [sampleWithPartialData];
        expectedResult = service.addSmartFolderToCollectionIfMissing(smartFolderCollection, smartFolder);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(smartFolder);
      });

      it('should add only unique SmartFolder to an array', () => {
        const smartFolderArray: ISmartFolder[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const smartFolderCollection: ISmartFolder[] = [sampleWithRequiredData];
        expectedResult = service.addSmartFolderToCollectionIfMissing(smartFolderCollection, ...smartFolderArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const smartFolder: ISmartFolder = sampleWithRequiredData;
        const smartFolder2: ISmartFolder = sampleWithPartialData;
        expectedResult = service.addSmartFolderToCollectionIfMissing([], smartFolder, smartFolder2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(smartFolder);
        expect(expectedResult).toContain(smartFolder2);
      });

      it('should accept null and undefined values', () => {
        const smartFolder: ISmartFolder = sampleWithRequiredData;
        expectedResult = service.addSmartFolderToCollectionIfMissing([], null, smartFolder, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(smartFolder);
      });

      it('should return initial array if no SmartFolder is added', () => {
        const smartFolderCollection: ISmartFolder[] = [sampleWithRequiredData];
        expectedResult = service.addSmartFolderToCollectionIfMissing(smartFolderCollection, undefined, null);
        expect(expectedResult).toEqual(smartFolderCollection);
      });
    });

    describe('compareSmartFolder', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSmartFolder(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 14548 };
        const entity2 = null;

        const compareResult1 = service.compareSmartFolder(entity1, entity2);
        const compareResult2 = service.compareSmartFolder(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 14548 };
        const entity2 = { id: 19985 };

        const compareResult1 = service.compareSmartFolder(entity1, entity2);
        const compareResult2 = service.compareSmartFolder(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 14548 };
        const entity2 = { id: 14548 };

        const compareResult1 = service.compareSmartFolder(entity1, entity2);
        const compareResult2 = service.compareSmartFolder(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
