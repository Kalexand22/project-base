package com.axelor.apps.openauctionbase.validate;

import com.axelor.apps.base.db.Partner;
import com.axelor.apps.base.db.TradingName;
import com.axelor.apps.openauction.db.ActivityHeader;
import com.axelor.apps.openauction.db.MissionHeader;
import com.axelor.apps.openauction.db.repo.MissionHeaderRepository;
import com.axelor.apps.openauctionbase.service.MissionLineManagement;
import com.axelor.apps.openauctionbase.service.MissionManagement;
import com.axelor.apps.openauctionbase.service.MissionStatusManagement;
import com.axelor.apps.openauctionbase.service.ToolsMissionChange;
import com.axelor.auth.db.User;
import com.axelor.exception.AxelorException;
import com.axelor.exception.db.repo.TraceBackRepository;
import com.axelor.inject.Beans;

public class MissionHeaderValidate {

  MissionManagement missionManagement = Beans.get(MissionManagement.class);
  MissionStatusManagement missionStatusManagement = Beans.get(MissionStatusManagement.class);
  ToolsMissionChange toolsMissionChange = Beans.get(ToolsMissionChange.class);
  // Auctioneer_Code Code10
  // OnValidateBEGIN
  //// ap57 isat.zw
  // IF "Auctioneer Code" <> '' THEN BEGIN
  //  "Salesperson Code" := "Auctioneer Code";
  // END;
  ////
  public MissionHeader validateAuctioneerCode(MissionHeader missionHeader, User auctioneerCode) {
    missionHeader.setAuctionnerCode(auctioneerCode);
    if (missionHeader.getAuctionnerCode() != null) {
      missionHeader.setSalespersonCode(missionHeader.getAuctionnerCode());
    }
    return missionHeader;
  }

  // Sorting_Method Option
  // OnValidateBEGIN
  // IF "Sorting Method" <> xRec."Sorting Method" THEN BEGIN
  //  MissionLineMgt.SortMissionLine(Rec);
  // END;//
  public MissionHeader validateSortingMethod(MissionHeader missionHeader, String sortingMethod) {
    if (missionHeader.getSortingMethod() != sortingMethod) {
      MissionLineManagement missionLineManagement = Beans.get(MissionLineManagement.class);
      missionLineManagement.sortMissionLine(missionHeader);
    }
    missionHeader.setSortingMethod(sortingMethod);
    return missionHeader;
  }

  // Mission_Status Option
  // OnValidateBEGIN
  //// AP04.isat.PC
  // IF "Mission Status" = "Mission Status"::Finished THEN BEGIN
  //  IF NOT MissionManagement.IfAllMissionServiceInvoiced(Rec) THEN BEGIN
  //    ERROR(Text8011404);
  //  END;
  //  IF NOT MissionManagement.IfAllTodoMissionCompleted(Rec) THEN BEGIN
  //    ERROR(Text8011405);
  //  END;
  // END;
  // IF "Mission Status" = "Mission Status"::Canceled THEN
  //  MissionStatusMgt.CancelMission(Rec)
  // ELSE
  //  MissionStatusMgt.CancelCancelledMission(Rec);
  // MissionManagement.SaveChangeStatus(xRec,Rec);//

  public MissionHeader validateMissionStatus(MissionHeader missionHeader, String missionStatus)
      throws AxelorException {
    missionHeader.setMissionStatus(missionStatus);
    if (missionHeader
        .getMissionStatus()
        .equals(MissionHeaderRepository.MISSIONSTATUS_FINISHED)) {
      if (!missionManagement.ifAllMissionServiceInvoiced(missionHeader)) {
        throw new AxelorException(
            missionHeader,
            TraceBackRepository.CATEGORY_INCONSISTENCY,
            "Toutes les lignes de mission doivent être facturées");
      }
      if (!missionManagement.ifAllTodoMissionCompleted(missionHeader)) {
        throw new AxelorException(
            missionHeader,
            TraceBackRepository.CATEGORY_INCONSISTENCY,
            "All mission services must be invoiced");
      }
    }
    if (missionHeader
        .getMissionStatus()
        .equals(MissionHeaderRepository.MISSIONSTATUS_CANCELED)) {
      missionStatusManagement.cancelMission(missionHeader);
    } else {
      missionStatusManagement.cancelCancelledMission(missionHeader);
    }
    missionManagement.saveChangeStatus(missionHeader, missionHeader);
    return missionHeader;
  }

  // Activity_Code_To_Header Code20
  // OnValidateBEGIN
  //// AP07.ISAT.SC
  //// Problème de mise à jour indirecte(gestion des statuts)
  // MODIFY(TRUE);
  //// IF xRec."Activity Code To Header" <> "Activity Code To Header" THEN BEGIN
  //  //Désactivé isat.zw ap36
  //  //pas de suppression
  //  //IF xRec."Activity Code To Header" <> '' THEN BEGIN
  //  //  MissionManagement.ChangeActivity(xRec."Activity Code To Header","No.",'','',"Mission
  // Template Code",FALSE,0); //AP16
  //  //END;
  //  IF "Activity Code To Header" <> '' THEN BEGIN
  //    MissionManagement.ChangeActivity("Activity Code To Header","No.",'','',"Mission Template
  // Code",TRUE,0); //AP16
  //  END;
  //// END;//
  public MissionHeader validateActivityCodeToHeader(
      MissionHeader missionHeader, ActivityHeader activityCodeToHeader) {
    missionHeader.setActivityCodeToHeader(activityCodeToHeader);

    if (missionHeader.getActivityCodeToHeader() != null) {
      missionManagement.changeActivity(
          missionHeader.getActivityCodeToHeader(),
          missionHeader,
          null,
          null,
          missionHeader.getMissionTemplateCode(),
          true,
          0);
    }
    return missionHeader;
  }

  // Centralized_Customer_No Code20
  // OnValidateVAR
  public MissionHeader validateCentralizedCustomerNo(
      MissionHeader missionHeader, Partner centralizedCustomerNo) {
    missionHeader.setCentralizedCustomerNo(centralizedCustomerNo);
    // TODO validateCentralizedCustomerNo
    return missionHeader;
  }

  // Master_Contact_No Code20
  // OnValidateVAR
  public MissionHeader validateMasterContactNo(
      MissionHeader missionHeader, Partner masterContactNo) {
    missionHeader.setMasterContactNo(masterContactNo);
    // TODO validateMasterContactNo
    return missionHeader;
  }

  // Responsibility_Center Code10
  // OnValidateBEGIN
  // IF "Responsibility Center" <> xRec."Responsibility Center" THEN BEGIN
  //  IF MissionTools.AllowRespCenterChange(Rec,TRUE) THEN
  //    MissionTools.UpdateRespCenterChange(Rec,"Responsibility Center");
  // END;//
  public MissionHeader validateResponsibilityCenter(
      MissionHeader missionHeader, TradingName responsibilityCenter) {
    missionHeader.setResponsibilityCenter(responsibilityCenter);
    if (missionHeader.getResponsibilityCenter() != missionHeader.getResponsibilityCenter()) {
      if (toolsMissionChange.allowRespCenterChange(missionHeader, true)) {
        missionHeader =
            toolsMissionChange.updateRespCenterChange(
                missionHeader, missionHeader.getResponsibilityCenter());
      }
    }
    return missionHeader;
  }

  // Manager Code20
  // OnValidateBEGIN
  //// ap02 isat.zw
  // ContactLawyerManagement.ValidateContact(ContactType::Manager, Manager);//
  public MissionHeader validateManager(MissionHeader missionHeader, Partner manager) {
    missionHeader.setManager(manager);
    // contactLawyerManagement.validateContact(ContactRepository.CONTACTTYPE_MANAGER,
    // missionHeader.getManager());
    return missionHeader;
  }
}
