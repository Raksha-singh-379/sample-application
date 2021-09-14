import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IQuestions, Questions } from '../questions.model';
import { QuestionsService } from '../service/questions.service';
import { IAnnexure } from 'app/entities/annexure/annexure.model';
import { AnnexureService } from 'app/entities/annexure/service/annexure.service';

@Component({
  selector: 'jhi-questions-update',
  templateUrl: './questions-update.component.html',
})
export class QuestionsUpdateComponent implements OnInit {
  isSaving = false;

  annexuresCollection: IAnnexure[] = [];

  editForm = this.fb.group({
    id: [],
    formId: [],
    type: [],
    subType: [],
    description: [],
    annexure: [],
  });

  constructor(
    protected questionsService: QuestionsService,
    protected annexureService: AnnexureService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ questions }) => {
      this.updateForm(questions);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const questions = this.createFromForm();
    if (questions.id !== undefined) {
      this.subscribeToSaveResponse(this.questionsService.update(questions));
    } else {
      this.subscribeToSaveResponse(this.questionsService.create(questions));
    }
  }

  trackAnnexureById(index: number, item: IAnnexure): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IQuestions>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(questions: IQuestions): void {
    this.editForm.patchValue({
      id: questions.id,
      formId: questions.formId,
      type: questions.type,
      subType: questions.subType,
      description: questions.description,
      annexure: questions.annexure,
    });

    this.annexuresCollection = this.annexureService.addAnnexureToCollectionIfMissing(this.annexuresCollection, questions.annexure);
  }

  protected loadRelationshipsOptions(): void {
    this.annexureService
      .query({ filter: 'questions-is-null' })
      .pipe(map((res: HttpResponse<IAnnexure[]>) => res.body ?? []))
      .pipe(
        map((annexures: IAnnexure[]) =>
          this.annexureService.addAnnexureToCollectionIfMissing(annexures, this.editForm.get('annexure')!.value)
        )
      )
      .subscribe((annexures: IAnnexure[]) => (this.annexuresCollection = annexures));
  }

  protected createFromForm(): IQuestions {
    return {
      ...new Questions(),
      id: this.editForm.get(['id'])!.value,
      formId: this.editForm.get(['formId'])!.value,
      type: this.editForm.get(['type'])!.value,
      subType: this.editForm.get(['subType'])!.value,
      description: this.editForm.get(['description'])!.value,
      annexure: this.editForm.get(['annexure'])!.value,
    };
  }
}
