export interface IAnnexure {
  id?: number;
  answer?: boolean | null;
  comment?: string | null;
}

export class Annexure implements IAnnexure {
  constructor(public id?: number, public answer?: boolean | null, public comment?: string | null) {
    this.answer = this.answer ?? false;
  }
}

export function getAnnexureIdentifier(annexure: IAnnexure): number | undefined {
  return annexure.id;
}
