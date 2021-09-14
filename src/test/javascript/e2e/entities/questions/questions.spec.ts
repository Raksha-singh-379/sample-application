import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { QuestionsComponentsPage, QuestionsDeleteDialog, QuestionsUpdatePage } from './questions.page-object';

const expect = chai.expect;

describe('Questions e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let questionsComponentsPage: QuestionsComponentsPage;
  let questionsUpdatePage: QuestionsUpdatePage;
  let questionsDeleteDialog: QuestionsDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Questions', async () => {
    await navBarPage.goToEntity('questions');
    questionsComponentsPage = new QuestionsComponentsPage();
    await browser.wait(ec.visibilityOf(questionsComponentsPage.title), 5000);
    expect(await questionsComponentsPage.getTitle()).to.eq('Questions');
    await browser.wait(ec.or(ec.visibilityOf(questionsComponentsPage.entities), ec.visibilityOf(questionsComponentsPage.noResult)), 1000);
  });

  it('should load create Questions page', async () => {
    await questionsComponentsPage.clickOnCreateButton();
    questionsUpdatePage = new QuestionsUpdatePage();
    expect(await questionsUpdatePage.getPageTitle()).to.eq('Create or edit a Questions');
    await questionsUpdatePage.cancel();
  });

  it('should create and save Questions', async () => {
    const nbButtonsBeforeCreate = await questionsComponentsPage.countDeleteButtons();

    await questionsComponentsPage.clickOnCreateButton();

    await promise.all([
      questionsUpdatePage.setFormIdInput('formId'),
      questionsUpdatePage.setTypeInput('type'),
      questionsUpdatePage.setSubTypeInput('subType'),
      questionsUpdatePage.setDescriptionInput('description'),
      questionsUpdatePage.annexureSelectLastOption(),
    ]);

    await questionsUpdatePage.save();
    expect(await questionsUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await questionsComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Questions', async () => {
    const nbButtonsBeforeDelete = await questionsComponentsPage.countDeleteButtons();
    await questionsComponentsPage.clickOnLastDeleteButton();

    questionsDeleteDialog = new QuestionsDeleteDialog();
    expect(await questionsDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Questions?');
    await questionsDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(questionsComponentsPage.title), 5000);

    expect(await questionsComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
