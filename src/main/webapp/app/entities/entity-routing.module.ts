import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'questions',
        data: { pageTitle: 'Questions' },
        loadChildren: () => import('./questions/questions.module').then(m => m.QuestionsModule),
      },
      {
        path: 'annexure',
        data: { pageTitle: 'Annexures' },
        loadChildren: () => import('./annexure/annexure.module').then(m => m.AnnexureModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
