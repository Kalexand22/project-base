package com.axelor.apps.openauctionbase.service;

import com.axelor.apps.base.db.Partner;
import com.axelor.apps.openauction.db.ActivityHeader;
import com.axelor.apps.openauction.db.LawyerBusiness;
import com.axelor.apps.openauction.db.MissionHeader;
import com.axelor.apps.openauction.db.MissionLine;
import com.axelor.apps.openauction.db.MissionTemplate;
import com.axelor.apps.openauction.db.repo.LawyerBusinessRepository;
import com.axelor.apps.openauction.db.repo.MissionHeaderRepository;
import com.axelor.apps.openauctionbase.util.TransferFields;
import com.axelor.apps.openauctionbase.validate.MissionHeaderValidate;
import com.axelor.inject.Beans;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

public class MissionTemplateManagementImpl implements MissionTemplateManagement {

  MissionHeaderRepository missionHeaderRepository;
  MissionTemplate MissionTemplate;
  Boolean HideTemplate;
  Boolean AuctionFromInv;
  Boolean SkipActivity;

  @Inject
  public MissionTemplateManagementImpl(MissionHeaderRepository missionHeaderRepository) {
    this.missionHeaderRepository = missionHeaderRepository;
    SkipActivity = false;
  }

  @Override
  @Transactional
  public MissionHeader createMissionFromMission(
      MissionHeader pMissionHeader,
      MissionTemplate pMissionTemplate,
      Boolean pJudicialFilter,
      String pLawyerBusNo) {
    MissionHeaderValidate missionHeaderValidate = Beans.get(MissionHeaderValidate.class);

    /*
    * IF lMissionTemplate.GET(lMissionTemplateCode) THEN BEGIN
       lMissionHeader.INIT;
       lMissionHeader.VALIDATE("Mission Template Code",lMissionTemplateCode);
       lMissionHeader.TRANSFERFIELDS(lMissionTemplate);
       IF pMissionHeader."No." <> '' THEN
         lMissionHeader."No." := pMissionHeader."No."; // AP14 isat.sf
       lMissionHeader."Mission Type" := lMissionType;
       lMissionHeader."Activity Code To Header" := lMissionTemplate."Activity Code To Header";
       lMissionHeader.INSERT(TRUE);



    */
    MissionHeader lmissionHeader = pMissionHeader;
    if (MissionTemplate != null) {
      lmissionHeader.setMissionTemplateCode(MissionTemplate);
      lmissionHeader =
          (MissionHeader) TransferFields.transferFields(lmissionHeader, pMissionTemplate);
      if (pMissionHeader.getNo() != null) {
        lmissionHeader.setNo(pMissionHeader.getNo());
      }
      lmissionHeader.setMissionType(pMissionHeader.getMissionType());
      lmissionHeader.setActivityCodeToHeader(pMissionTemplate.getActivityCodeToHeader());
      missionHeaderRepository.save(lmissionHeader);

      /*
      * lMissionHeader.VALIDATE("Auctioneer Code",lAuctioneerCode);

       IF lMissionTitle <> '' THEN BEGIN
         lMissionHeader.Description := lMissionTitle;
       END ELSE BEGIN
         lMissionHeader.Description := COPYSTR(lMissionTemplate.Code + ' '
                                       + FinNav.GetContactName(lMissionHeader."Master Contact No."),1,49);
       END;

       IF (lMissionTemplate."Process Type" = lMissionTemplate."Process Type"::Recovery)
          AND (pMissionHeader.Description <> '') THEN BEGIN
         lMissionHeader.Description := pMissionHeader.Description; // AP09 isat.sf
       END;

       lMissionHeader.VALIDATE("Mission Manager Code",lMissionManagerCode);
       IF lActivityCodeToHeader <> '' THEN    // isat.Sf 121109
         lMissionHeader."Activity Code To Header" := lActivityCodeToHeader;
       IF lActivityCodeToLine <> '' THEN   // isat.Sf 121109
         lMissionHeader."Activity Code To Lines" := lActivityCodeToLine;
       lMissionHeader.VALIDATE("Previous Mission No.",lOriginMissionNo);
       lMissionHeader.VALIDATE("Responsibility Center",lMissionResponsabilityCenter);
      */
      lmissionHeader =
          missionHeaderValidate.validateAuctioneerCode(
              lmissionHeader, lmissionHeader.getAuctionnerCode());
      String lMissionTitle = lmissionHeader.getDescription();
      if (lMissionTitle != null && !lMissionTitle.isEmpty()) {
        lmissionHeader.setDescription(lMissionTitle);
      } else {
        lmissionHeader.setDescription(
            pMissionTemplate.getCode() + " " + lmissionHeader.getMasterContactNo().getFullName());
      }
      lmissionHeader =
          missionHeaderValidate.validateResponsibilityCenter(
              lmissionHeader, lmissionHeader.getResponsibilityCenter());

      /*
      * IF lLawyerBus."No." <> '' THEN BEGIN

           //<<  AP11 isat.Sf
           CASE lLawyerBus."Process Type" OF
             lLawyerBus."Process Type"::"Procedure RJ" : BEGIN
               lMissionHeader.VALIDATE("Master Contact No.",lLawyerBus."Debtor Contact No.");
             END;
             //<< AP19 isat.sf
             lLawyerBus."Process Type"::Backup : BEGIN
               lMissionHeader.VALIDATE("Master Contact No.",lLawyerBus."Debtor Contact No.");
             END;
             //>> AP19 isat.sf
             lLawyerBus."Process Type"::"Procedure LJ" : BEGIN
               lMissionHeader.VALIDATE("Master Contact No.",lMasterContactNo);
             END;
             lLawyerBus."Process Type"::Recovery : BEGIN
               lMissionHeader.VALIDATE(Manager,lLawyerBus.Manager);
               lMissionHeader.VALIDATE("Main Interlocutor No.",lLawyerBus."Main Interlocutor No.");

               lMissionHeader.VALIDATE("Master Contact No.",lLawyerBus."Debtor Contact No.");
               lMissionHeader.VALIDATE("Correspondent Contact No.",lLawyerBus."Contact No.");

               lMissionHeader.VALIDATE("Debtor Lawyer",lLawyerBus."Debtor Lawyer");
               lMissionHeader.VALIDATE("Creditors Lawyer",lLawyerBus."Creditors Lawyer");
               lMissionHeader.VALIDATE("Representative of Creditors",lLawyerBus."Representative of Creditors");
             END;
             ELSE BEGIN
               lMissionHeader.VALIDATE("Master Contact No.",lMasterContactNo);
             END;
           END;
      */
      if (pLawyerBusNo != null && !pLawyerBusNo.isEmpty()) {
        LawyerBusiness lLawyerBus =
            Beans.get(LawyerBusinessRepository.class)
                .all()
                .filter("self.no = ?", pLawyerBusNo)
                .fetchOne();
        if (lLawyerBus != null) {
          // TODO REVOIR LE CODE
          switch (lLawyerBus.getProcessType()) {
            case "Procedure RJ":
              lmissionHeader =
                  missionHeaderValidate.validateMasterContactNo(
                      lmissionHeader, lLawyerBus.getDebtorContactNo());
              break;
            case "Backup":
              lmissionHeader =
                  missionHeaderValidate.validateMasterContactNo(
                      lmissionHeader, lLawyerBus.getDebtorContactNo());
              break;
            case "Procedure LJ":
              lmissionHeader =
                  missionHeaderValidate.validateMasterContactNo(
                      lmissionHeader, lmissionHeader.getMasterContactNo());
              break;
            case "Recovery":
              lmissionHeader =
                  missionHeaderValidate.validateManager(lmissionHeader, lLawyerBus.getManager());
              // lmissionHeader = missionHeaderValidate.validateMainInterlocutorNo(lmissionHeader,
              // lLawyerBus.getMainInterlocutorNo());
              lmissionHeader =
                  missionHeaderValidate.validateMasterContactNo(
                      lmissionHeader, lLawyerBus.getDebtorContactNo());
              lmissionHeader.setCorrespondentContactNo(lLawyerBus.getContactNo());
              lmissionHeader.setDebtorLawyer(lLawyerBus.getDebtorLawyer());
              lmissionHeader.setCreditorsLawyer(lLawyerBus.getCreditorsLawyer());
              lmissionHeader.setRepresentativeofCreditors(
                  lLawyerBus.getRepresentativeofCreditors());
              break;
            default:
              lmissionHeader =
                  missionHeaderValidate.validateMasterContactNo(
                      lmissionHeader, lmissionHeader.getMasterContactNo());
              break;
          }
        }
      } else {
        lmissionHeader =
            missionHeaderValidate.validateMasterContactNo(
                lmissionHeader, lmissionHeader.getMasterContactNo());
      }

      // TODO Méthode à compléter?
    }

    if (!SkipActivity) {
      this.createActivity(lmissionHeader, lmissionHeader.getActivityCodeToHeader());
    }
    return pMissionHeader;
  }

  @Override
  @Transactional
  public Boolean createMissionFromContact(Partner pContact) {
    return null;
  }

  @Override
  public void getCustomerNo() {}

  @Override
  public void setAuctionFromInv(Boolean pTrue) {
    AuctionFromInv = pTrue;
  }

  @Override
  public void setSkipActivityCreation(Boolean pSkipActivity) {
    SkipActivity = pSkipActivity;
  }

  @Override
  @Transactional
  public void createActivity(MissionHeader pMissionHeader, ActivityHeader pActivityCodeToHeader) {
    MissionLine lMissionLine = new MissionLine();
    ActivityManagement activityManagement = Beans.get(ActivityManagement.class);
    if (pActivityCodeToHeader != null) {
      activityManagement.CreateActivityLineFromMission(
          pActivityCodeToHeader, pMissionHeader, lMissionLine, false);
    } else {
      if (pMissionHeader.getActivityCodeToHeader() != null) {
        activityManagement.CreateActivityLineFromMission(
            pMissionHeader.getActivityCodeToHeader(), pMissionHeader, lMissionLine, false);
      }
    }
  }
}
