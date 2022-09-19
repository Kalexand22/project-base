package com.axelor.apps.openauctiontemplate.controller;

import com.axelor.apps.base.db.Address;
import com.axelor.apps.base.db.Partner;
import com.axelor.apps.base.db.repo.AddressRepository;
import com.axelor.apps.message.db.EmailAddress;
import com.axelor.apps.openauction.db.ContactTemplate;
import com.axelor.apps.openauction.db.repo.ContactTemplateRepository;
import com.axelor.apps.openauctiontemplate.service.ContactTemplateService;
import com.axelor.inject.Beans;
import com.axelor.meta.schema.actions.ActionView;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.axelor.rpc.Context;
import java.util.HashMap;

public class ContactTemplateController {
  public void createContactFromTemplate(ActionRequest request, ActionResponse response) {

    Context context = request.getContext();

    HashMap<String, Object> contactTemplateMap =
        (HashMap<String, Object>) context.get("contactTemplate");
    ContactTemplate contactTemplate =
        Beans.get(ContactTemplateRepository.class)
            .find(Long.parseLong(contactTemplateMap.get("id").toString()));

    Partner tmpPartner = new Partner();
    tmpPartner.setName((String) context.get("name"));

    HashMap<String, Object> mainAddressMap = (HashMap<String, Object>) context.get("mainAddress");
    Address tmpAddress =
        Beans.get(AddressRepository.class)
            .find(Long.parseLong(mainAddressMap.get("id").toString()));

    tmpPartner.setMainAddress(tmpAddress);
    tmpPartner.setFixedPhone((String) context.get("fixedPhone"));
    tmpPartner.setMobilePhone((String) context.get("mobilePhone"));
    tmpPartner.setFax((String) context.get("fax"));
    // TODO persister l'email en base de données
    EmailAddress emailAddress = new EmailAddress();
    emailAddress.setAddress((String) context.get("emailAddress"));
    tmpPartner.setEmailAddress(emailAddress);

    tmpPartner.setTitleSelect((Integer) context.get("titleSelect"));
    tmpPartner.setFirstName((String) context.get("firstName"));

    ContactTemplateService contactTemplateService = Beans.get(ContactTemplateService.class);
    tmpPartner = contactTemplateService.createContactFromTemplate(contactTemplate, tmpPartner);

    if (tmpPartner != null) {
      // Open the generated invoice in a new tab
      response.setView(
          ActionView.define("Contact")
              .model(Partner.class.getName())
              .add("grid", "partner-grid")
              .add("form", "partner-form")
              .param("forceEdit", "true")
              .context("_showRecord", String.valueOf(tmpPartner.getId()))
              .map());
      response.setCanClose(true);
    }
  }
}
