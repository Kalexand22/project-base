package com.axelor.apps.openauctiontemplate.service;

import com.axelor.apps.base.db.Partner;
import com.axelor.apps.openauction.db.ContactTemplate;

public interface ContactTemplateService {

  public Partner createContactFromTemplate(ContactTemplate contactTemplate, Partner tmpPartner);
}
