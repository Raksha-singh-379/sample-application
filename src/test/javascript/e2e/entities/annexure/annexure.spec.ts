import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { AnnexureComponentsPage, AnnexureDeleteDialog, AnnexureUpdatePage } from './annexure.page-object';

const expect = chai.expect;

describe('Annexure e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let annexureComponentsPage: AnnexureComponentsPage;
  let annexureUpdatePage: AnnexureUpdatePage;
  let annexureDeleteDialog: AnnexureDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Annexures', async () => {
    await navBarPage.goToEntity('annexure');
    annexureComponentsPage = new AnnexureComponentsPage();
    await browser.wait(ec.visibilityOf(annexureComponentsPage.title), 5000);
    expect(await annexureComponentsPage.getTitle()).to.eq('Annexures');
    await browser.wait(ec.or(ec.visibilityOf(annexureComponentsPage.entities), ec.visibilityOf(annexureComponentsPage.noResult)), 1000);
  });

  it('should load create Annexure page', async () => {
    await annexureComponentsPage.clickOnCreateButton();
    annexureUpdatePage = new AnnexureUpdatePage();
    expect(await annexureUpdatePage.getPageTitle()).to.eq('Create or edit a Annexure');
    await annexureUpdatePage.cancel();
  });

  it('should create and save Annexures', async () => {
    const nbButtonsBeforeCreate = await annexureComponentsPage.countDeleteButtons();

    await annexureComponentsPage.clickOnCreateButton();

    await promise.all([annexureUpdatePage.getAnswerInput().click(), annexureUpdatePage.setCommentInput('comment')]);

    await annexureUpdatePage.save();
    expect(await annexureUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await annexureComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Annexure', async () => {
    const nbButtonsBeforeDelete = await annexureComponentsPage.countDeleteButtons();
    await annexureComponentsPage.clickOnLastDeleteButton();

    annexureDeleteDialog = new AnnexureDeleteDialog();
    expect(await annexureDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Annexure?');
    await annexureDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(annexureComponentsPage.title), 5000);

    expect(await annexureComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
