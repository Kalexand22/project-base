package com.axelor.apps.openauctiontemplate.module;

import com.axelor.app.AxelorModule;
import com.axelor.apps.openauctiontemplate.service.ContactTemplateService;
import com.axelor.apps.openauctiontemplate.service.ContactTemplateServiceImpl;

public class OpenAuctionTemplateModule extends AxelorModule {

  @Override
  protected void configure() {
    bind(ContactTemplateService.class).to(ContactTemplateServiceImpl.class);
  }
}
