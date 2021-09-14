import { element, by, ElementFinder } from 'protractor';

export class QuestionsComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-questions div table .btn-danger'));
  title = element.all(by.css('jhi-questions div h2#page-heading span')).first();
  noResult = element(by.id('no-result'));
  entities = element(by.id('entities'));

  async clickOnCreateButton(): Promise<void> {
    await this.createButton.click();
  }

  async clickOnLastDeleteButton(): Promise<void> {
    await this.deleteButtons.last().click();
  }

  async countDeleteButtons(): Promise<number> {
    return this.deleteButtons.count();
  }

  async getTitle(): Promise<string> {
    return this.title.getText();
  }
}

export class QuestionsUpdatePage {
  pageTitle = element(by.id('jhi-questions-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  formIdInput = element(by.id('field_formId'));
  typeInput = element(by.id('field_type'));
  subTypeInput = element(by.id('field_subType'));
  descriptionInput = element(by.id('field_description'));

  annexureSelect = element(by.id('field_annexure'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setFormIdInput(formId: string): Promise<void> {
    await this.formIdInput.sendKeys(formId);
  }

  async getFormIdInput(): Promise<string> {
    return await this.formIdInput.getAttribute('value');
  }

  async setTypeInput(type: string): Promise<void> {
    await this.typeInput.sendKeys(type);
  }

  async getTypeInput(): Promise<string> {
    return await this.typeInput.getAttribute('value');
  }

  async setSubTypeInput(subType: string): Promise<void> {
    await this.subTypeInput.sendKeys(subType);
  }

  async getSubTypeInput(): Promise<string> {
    return await this.subTypeInput.getAttribute('value');
  }

  async setDescriptionInput(description: string): Promise<void> {
    await this.descriptionInput.sendKeys(description);
  }

  async getDescriptionInput(): Promise<string> {
    return await this.descriptionInput.getAttribute('value');
  }

  async annexureSelectLastOption(): Promise<void> {
    await this.annexureSelect.all(by.tagName('option')).last().click();
  }

  async annexureSelectOption(option: string): Promise<void> {
    await this.annexureSelect.sendKeys(option);
  }

  getAnnexureSelect(): ElementFinder {
    return this.annexureSelect;
  }

  async getAnnexureSelectedOption(): Promise<string> {
    return await this.annexureSelect.element(by.css('option:checked')).getText();
  }

  async save(): Promise<void> {
    await this.saveButton.click();
  }

  async cancel(): Promise<void> {
    await this.cancelButton.click();
  }

  getSaveButton(): ElementFinder {
    return this.saveButton;
  }
}

export class QuestionsDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-questions-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-questions'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
