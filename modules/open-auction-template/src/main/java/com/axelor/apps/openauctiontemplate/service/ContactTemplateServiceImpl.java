package com.axelor.apps.openauctiontemplate.service;

import com.axelor.apps.base.db.Company;
import com.axelor.apps.base.db.Partner;
import com.axelor.apps.base.db.repo.CompanyRepository;
import com.axelor.apps.base.db.repo.PartnerRepository;
import com.axelor.apps.openauction.db.ContactTemplate;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import java.lang.invoke.MethodHandles;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContactTemplateServiceImpl implements ContactTemplateService {
  private final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  PartnerRepository partnerRepository;
  CompanyRepository companyRepository;

  @Inject
  public ContactTemplateServiceImpl(
      PartnerRepository partnerRepository, CompanyRepository companyRepository) {
    this.partnerRepository = partnerRepository;
    this.companyRepository = companyRepository;
  }

  @Override
  @Transactional(rollbackOn = {Exception.class})
  public Partner createContactFromTemplate(ContactTemplate contactTemplate, Partner tmpPartner) {
    log.debug("Creation d'un contact depuis un modèle");
    tmpPartner.setIsContact(true);
    tmpPartner.setIsCustomer(true);
    Set<Company> set = new HashSet<Company>(companyRepository.all().fetch());
    tmpPartner.setCompanySet(set);
    tmpPartner = initPartnerFromTemplate(contactTemplate, tmpPartner);
    partnerRepository.save(tmpPartner);
    log.debug("Creation d'un contact depuis un modèle OK");
    return tmpPartner;
  }

  private Partner initPartnerFromTemplate(ContactTemplate contactTemplate, Partner tmpPartner) {
    tmpPartner.setFiscalPosition(contactTemplate.getFiscalPosition());
    tmpPartner.setAddedValueType(contactTemplate.getAddedValueType());
    tmpPartner.setContactAuctionPriceGroup(contactTemplate.getContactAuctionPriceGroup());
    tmpPartner.setContactMissionPriceGroup(contactTemplate.getContactMissionPriceGroup());
    tmpPartner.setContactTemplateColor(contactTemplate.getContactTemplateColor());
    tmpPartner.setFreeReasonCode(contactTemplate.getFreeReasonCode());
    tmpPartner.setCurrency(contactTemplate.getCurrencyCode());
    // TODO paramétrer 2 type de mode de paiment dans le modèle de contact
    tmpPartner.setInPaymentMode(contactTemplate.getPaymentTermsCode());
    tmpPartner.setOutPaymentMode(contactTemplate.getPaymentTermsCode());

    tmpPartner.setPreemptingAuthorized(contactTemplate.getPreemptingAuthorized());
    return tmpPartner;
  }
}
