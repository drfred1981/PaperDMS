import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { BankStatementDetailComponent } from './bank-statement-detail.component';

describe('BankStatement Management Detail Component', () => {
  let comp: BankStatementDetailComponent;
  let fixture: ComponentFixture<BankStatementDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BankStatementDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./bank-statement-detail.component').then(m => m.BankStatementDetailComponent),
              resolve: { bankStatement: () => of({ id: 32402 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(BankStatementDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BankStatementDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load bankStatement on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', BankStatementDetailComponent);

      // THEN
      expect(instance.bankStatement()).toEqual(expect.objectContaining({ id: 32402 }));
    });
  });

  describe('PreviousState', () => {
    it('should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
