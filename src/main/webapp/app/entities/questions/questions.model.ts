import { IAnnexure } from 'app/entities/annexure/annexure.model';

export interface IQuestions {
  id?: number;
  formId?: string | null;
  type?: string | null;
  subType?: string | null;
  description?: string | null;
  annexure?: IAnnexure | null;
}

export class Questions implements IQuestions {
  constructor(
    public id?: number,
    public formId?: string | null,
    public type?: string | null,
    public subType?: string | null,
    public description?: string | null,
    public annexure?: IAnnexure | null
  ) {}
}

export function getQuestionsIdentifier(questions: IQuestions): number | undefined {
  return questions.id;
}
