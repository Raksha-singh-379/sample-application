jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { QuestionsService } from '../service/questions.service';
import { IQuestions, Questions } from '../questions.model';
import { IAnnexure } from 'app/entities/annexure/annexure.model';
import { AnnexureService } from 'app/entities/annexure/service/annexure.service';

import { QuestionsUpdateComponent } from './questions-update.component';

describe('Component Tests', () => {
  describe('Questions Management Update Component', () => {
    let comp: QuestionsUpdateComponent;
    let fixture: ComponentFixture<QuestionsUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let questionsService: QuestionsService;
    let annexureService: AnnexureService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [QuestionsUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(QuestionsUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(QuestionsUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      questionsService = TestBed.inject(QuestionsService);
      annexureService = TestBed.inject(AnnexureService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call annexure query and add missing value', () => {
        const questions: IQuestions = { id: 456 };
        const annexure: IAnnexure = { id: 81567 };
        questions.annexure = annexure;

        const annexureCollection: IAnnexure[] = [{ id: 91614 }];
        jest.spyOn(annexureService, 'query').mockReturnValue(of(new HttpResponse({ body: annexureCollection })));
        const expectedCollection: IAnnexure[] = [annexure, ...annexureCollection];
        jest.spyOn(annexureService, 'addAnnexureToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ questions });
        comp.ngOnInit();

        expect(annexureService.query).toHaveBeenCalled();
        expect(annexureService.addAnnexureToCollectionIfMissing).toHaveBeenCalledWith(annexureCollection, annexure);
        expect(comp.annexuresCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const questions: IQuestions = { id: 456 };
        const annexure: IAnnexure = { id: 20572 };
        questions.annexure = annexure;

        activatedRoute.data = of({ questions });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(questions));
        expect(comp.annexuresCollection).toContain(annexure);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Questions>>();
        const questions = { id: 123 };
        jest.spyOn(questionsService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ questions });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: questions }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(questionsService.update).toHaveBeenCalledWith(questions);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Questions>>();
        const questions = new Questions();
        jest.spyOn(questionsService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ questions });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: questions }));
        saveSubject.complete();

        // THEN
        expect(questionsService.create).toHaveBeenCalledWith(questions);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Questions>>();
        const questions = { id: 123 };
        jest.spyOn(questionsService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ questions });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(questionsService.update).toHaveBeenCalledWith(questions);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackAnnexureById', () => {
        it('Should return tracked Annexure primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackAnnexureById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
