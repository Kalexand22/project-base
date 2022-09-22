package com.axelor.apps.openauctionbase.service;

import com.axelor.apps.base.db.Partner;
import com.axelor.apps.base.db.Product;
import com.axelor.apps.openauction.db.ActivityHeader;
import com.axelor.apps.openauction.db.ActivityLine;
import com.axelor.apps.openauction.db.Lot;
import com.axelor.apps.openauction.db.LotInputJournal;
import com.axelor.apps.openauction.db.LotQuickInputJournal;
import com.axelor.apps.openauction.db.LotTemplate;
import com.axelor.apps.openauction.db.MissionHeader;
import com.axelor.apps.openauction.db.MissionLine;
import com.axelor.apps.openauction.db.MissionServiceLine;
import com.axelor.apps.openauction.db.ServiceTemplateLine;
import com.axelor.apps.openauction.db.repo.LotInputJournalRepository;
import com.axelor.apps.openauction.db.repo.LotRepository;
import com.axelor.apps.openauctionbase.repository.LotExt;
import com.axelor.apps.openauctionbase.util.TransferFields;
import com.axelor.exception.AxelorException;
import com.axelor.exception.db.repo.TraceBackRepository;
import com.axelor.inject.Beans;
import com.google.inject.Inject;
import java.math.BigDecimal;
import java.util.Date;

public class LotTemplateManagementImpl implements LotTemplateManagement {
  /*
  * MissionLineManagement@1000000008 : Codeunit 8011367;
   ActivityManagement@1000000011 : Codeunit 8011374;
   ContactLotManagement@1000000005 : Codeunit 8011385;
   StatusManagement@1000000010 : Codeunit 8011380;
  */
  ActivityManagement activityManagement;
  ContactLotManagement contactLotManagement;
  MissionStatusManagement statusManagement;
  LotRepository lotRepository;
  LotInputJournalRepository lotInputJournalRepository;
  Lot lot;

  @Inject
  public LotTemplateManagementImpl(
      LotRepository lotRepository, LotInputJournalRepository lotInputJournalRepository) {
    activityManagement = Beans.get(ActivityManagement.class);
    contactLotManagement = Beans.get(ContactLotManagement.class);
    statusManagement = Beans.get(MissionStatusManagement.class);
    this.lotRepository = lotRepository;
    this.lotInputJournalRepository = lotInputJournalRepository;
    this.lot = new Lot();
  }

  @Override
  public Lot CreateLot(LotQuickInputJournal pLotQuickInputJournal, Partner pContactNo) {
    // lLot@1000000002 : Record 8011404;
    Lot lLot;
    LotTemplate lLotTemplate;
    lLotTemplate = pLotQuickInputJournal.getLotTemplateCode();
    if (lLotTemplate == null) {
      return null;
    }
    lLot = new Lot();
    lLot.setLotTemplateCode(pLotQuickInputJournal.getLotTemplateCode());
    // TODO lLotTemplate.CheckBeforeUsage;
    lLot = (LotExt) TransferFields.transferFields(lLotTemplate, lLot);
    lLot.setDescription(pLotQuickInputJournal.getDescription());
    lLot.setSearchDescription(lLot.getDescription());
    if (pLotQuickInputJournal.getLotNo() != null) {
      lLot.setNo(pLotQuickInputJournal.getLotNo().getNo());
    }
    lLot.setQuantity(pLotQuickInputJournal.getQuantity());
    lLot.setLotCategorie1Code1(pLotQuickInputJournal.getLotCategorie1Code());
    lLot.setLotCategorie2Code1(pLotQuickInputJournal.getLotCategorie2Code());
    lLot.setLotCategorie3Code1(pLotQuickInputJournal.getLotCategorie3Code());
    lLot.setLotCategorie4Code1(pLotQuickInputJournal.getLotCategorie4Code());
    lLot.setValuationAtBest(pLotQuickInputJournal.getValuationAtBest());
    lLot.setOriginCountryCode(pLotQuickInputJournal.getOriginCountryCode());
    lLot.setAuctionProductFamily(pLotQuickInputJournal.getAuctionProductFamily());
    lLot.setResponsibilityCenter(pLotQuickInputJournal.getResponsibilityCenter());

    lLot.setLawyerBusNo(pLotQuickInputJournal.getLawyerBusNo());
    lLot.setInvLocation(pLotQuickInputJournal.getInvLocation());
    lLot.setInvSubLocation(pLotQuickInputJournal.getInvSubLocation());
    lLot.setInvClassification(pLotQuickInputJournal.getInvClassification());
    lLot.setInvSubClassification(pLotQuickInputJournal.getInvSubClassification());
    lLot.setInvMissionNo(pLotQuickInputJournal.getInvMissionNo());
    lLot.setInvMissionLineNo(pLotQuickInputJournal.getInvMissionLineNo());
    lLot.setLeasingContactNo(pLotQuickInputJournal.getLeasingContactNo());
    lLot.setLeasingContractNo(pLotQuickInputJournal.getLeasingContractNo());
    lLot.setLotConditionCode(pLotQuickInputJournal.getLotConditionCode());
    lLot.setExternalNo(pLotQuickInputJournal.getExternalNo());
    lLot.setInterlocutor(pLotQuickInputJournal.getInterlocutor());
    //   //>> AP14 isat.sf

    //   IF lLot.Vehicle THEN
    //     lLot.VALIDATE("Lot Inventory Status", lLot."Lot Inventory Status"::"To Pick");
    if (lLot.getVehicle()) {
      lLot.setLotInventoryStatus(LotRepository.LOTINVENTORYSTATUS_SELECT_TO_PICK);
    }

    //   lLot."Date To Auction From" := pLotQuickInputJournal."Date To Auction From";   //AP15
    // isat.zw
    lLot.setDateToAuctionFrom(pLotQuickInputJournal.getDateToAuctionFrom());
    //   //error('++'+format(lLot));

    //   lLot.INSERT(TRUE);
    lotRepository.save(lLot);
    //   LotNo := lLot."No.";

    // TODO LotUnitofMeasure
    //   lLotUnitofMeasure.SETRANGE("No.",lLotTemplate.Code);
    //   IF lLotUnitofMeasure.FINDSET THEN BEGIN
    //     REPEAT
    //       lLotUnitofMeasure2."No." := lLot."No.";
    //       lLotUnitofMeasure2.Code := lLotUnitofMeasure.Code;
    //       lLotUnitofMeasure2."Qty. per Unit of Measure" := lLotUnitofMeasure."Qty. per Unit of
    // Measure";
    //       IF lLotUnitofMeasure2.INSERT(TRUE) THEN;  // debug 290110 isat.sf
    //     UNTIL lLotUnitofMeasure.NEXT = 0;
    //   END;

    contactLotManagement.insertSellerContactbyLot(pContactNo, lLot);

    lot = lLot;

    return lLot;
  }

  @Override
  public void CreateLotFromContact(LotQuickInputJournal pLotQuickInputJournal, Partner pContact) {
    Lot lLot = new Lot();
    lLot = this.CreateLot(pLotQuickInputJournal, pContact);
    this.CreateLotValueEntry(
        pLotQuickInputJournal, lLot, lLot.getCurrentMissionNo(), lLot.getMissionLine());
  }

  @Override
  public Lot CreateLotFromMission(
      LotQuickInputJournal pLotQuickInputJournal, MissionHeader pMissionHeader) {
    
    PostLotQuickInputFromMission(pLotQuickInputJournal, pMissionHeader);
    // IF LotNo <> '' THEN BEGIN
    if (lot != null) {

      if (!lot.getLotGeneralStatus().equals(LotRepository.LOTGENERALSTATUS_SELECT_ONMISSION)) {
        //TODO OnValidate
        lot.setLotGeneralStatus(LotRepository.LOTGENERALSTATUS_SELECT_ONMISSION);
        lotRepository.save(lot);
      }
    }
    return lot;
  }

  @Override
  public void PostLotQuickInputFromMission(
      LotQuickInputJournal pLotQuickInputJournal, MissionHeader pMissionHeader) {
    // TODO Auto-generated method stub

  }

  @Override
  public String CreateLotFromAuction(LotQuickInputJournal pLotQuickInputJournal) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void CreateLotTemplate(Lot pLotTemplate, String pTypeOrigin) {
    // TODO Auto-generated method stub

  }

  @Override
  public void CreateLotValueEntry(
      LotQuickInputJournal pLotQuickInputJournal,
      Lot pLot,
      MissionHeader pMissionHeader,
      MissionLine pMissionLine) {
    // TODO Auto-generated method stub

  }

  @Override
  public void CreateLotValueEntryReserve(
      LotQuickInputJournal pLotQuickInputJournal,
      Lot pLot,
      MissionHeader pMissionHeader,
      MissionLine pMissionLine) {
    // TODO Auto-generated method stub

  }

  @Override
  public void CreateLotValueEntryEstimate(
      LotQuickInputJournal pLotQuickInputJournal,
      Lot pLot,
      MissionHeader pMissionHeader,
      MissionLine pMissionLine) {
    // TODO Auto-generated method stub

  }

  @Override
  public void CreateLotValueEntryAuctEstim(
      LotQuickInputJournal pLotQuickInputJournal,
      Lot pLot,
      MissionHeader pMissionHeader,
      MissionLine pMissionLine) {
    // TODO Auto-generated method stub

  }

  @Override
  public void CreateLotValueEntryAppraisal(
      LotQuickInputJournal pLotQuickInputJournal,
      Lot pLot,
      MissionHeader pMissionHeader,
      MissionLine pMissionLine) {
    // TODO Auto-generated method stub

  }

  @Override
  public void CreateLotValueEntryQuotation(
      LotQuickInputJournal pLotQuickInputJournal,
      Lot pLot,
      MissionHeader pMissionHeader,
      MissionLine pMissionLine) {
    // TODO Auto-generated method stub

  }

  @Override
  public void CreateLotInventoryEntry(LotQuickInputJournal pLotQuickInputJournal, Lot pLot) {
    // TODO Auto-generated method stub

  }

  @Override
  public void CalcGrossReserveByNetReserve(LotInputJournal pLotInputJournal)
      throws AxelorException {
    if (pLotInputJournal.getLotTemplateCode() == null) {
      throw new AxelorException(
          TraceBackRepository.CATEGORY_INCONSISTENCY,
          "Vous devez spécifier le modèle de lot pour calculer le prix de réserve");
    }
    if (pLotInputJournal.getNetReservePrice() != BigDecimal.ZERO) {
      BigDecimal reserveGrossPrice = pLotInputJournal.getNetReservePrice();
      reserveGrossPrice =
          reserveGrossPrice.add(
              CalcCommissionWithBaseAmount(
                  pLotInputJournal, pLotInputJournal.getNetReservePrice()));
      reserveGrossPrice = reserveGrossPrice.setScale(0, BigDecimal.ROUND_HALF_UP);
      pLotInputJournal.setGrossReservePrice(reserveGrossPrice);
      lotInputJournalRepository.save(pLotInputJournal);
    }
  }

  @Override
  public void CalcNetReserveByGrossReserve(LotInputJournal pLotInputJournal)
      throws AxelorException {
    if (pLotInputJournal.getLotTemplateCode() == null) {
      throw new AxelorException(
          TraceBackRepository.CATEGORY_INCONSISTENCY,
          "Vous devez spécifier le modèle de lot pour calculer le prix de réserve");
    }
    if (pLotInputJournal.getGrossReservePrice() != BigDecimal.ZERO) {
      BigDecimal reserveNetPrice = pLotInputJournal.getGrossReservePrice();
      reserveNetPrice =
          reserveNetPrice.subtract(
              CalcCommissionWithBaseAmount(
                  pLotInputJournal, pLotInputJournal.getGrossReservePrice()));
      reserveNetPrice = reserveNetPrice.setScale(0, BigDecimal.ROUND_HALF_UP);
      pLotInputJournal.setNetReservePrice(reserveNetPrice);
      lotInputJournalRepository.save(pLotInputJournal);
    }
  }

  @Override
  public BigDecimal CalcCommissionWithBaseAmount(
      LotInputJournal pLotInputJournal, BigDecimal pBaseAmount) {
    MissionServiceLine lMissionServiceLine;
    MissionHeader lMission = pLotInputJournal.getDocumentNo();
    LotTemplate lLotTemplate = pLotInputJournal.getLotTemplateCode();
    ActivityHeader lActivityHeader = lMission.getActivityCodeToHeader();

    Product lItem;
    MissionServicePriceManagement lMissionServPriceMgt =
        Beans.get(MissionServicePriceManagement.class);
    for (ActivityLine lActivityLine : lActivityHeader.getActivityLineList()) {
      if (lActivityLine.getServiceTemplateCode() != null) {
        for (ServiceTemplateLine lMissionServiceTemplateLine :
            lActivityLine.getServiceTemplateCode().getTemplateLineList()) {
          if (lMissionServiceTemplateLine.getProduct() != null) {
            lItem = lMissionServiceTemplateLine.getProduct();
            // IF lItem."Service Type" = lItem."Service Type"::Commission THEN BEGIN

          }

          //  WITH lMissionServiceLine DO BEGIN
          //                 INIT;
          //                 VALIDATE("Mission No.", pLotInputJournal."Document No.");
          //                 VALIDATE("Transaction Type", "Transaction Type"::Mission);

          //                 //VALIDATE("Lot No.",pLotNo);
          //                 "Lot Template Code" := pLotInputJournal."Lot Template Code";
          //                 "Responsibility Center" := lMission."Responsibility Center";
          //                 CASE "Transaction Type" OF
          //                   "Transaction Type"::Mission : BEGIN
          //                      "Lot Price Group" := lLotTemplate."Lot Mission Price Group";
          //                      //"Transaction Line No." := Lot."Current Mission Line No.";
          //                   END;
          //                 END;
          //                 VALIDATE(Type,lMissionServiceTemplateLine.Type);
          //                 VALIDATE("No.",lMissionServiceTemplateLine."No.");
          //                 IF lItem."Use Lot VAT Posting Group" THEN
          //                   VALIDATE("VAT Prod. Posting Group", lLotTemplate."VAT Prod. Posting
          // Group");
          // //                VALIDATE("Chargeable Contact No.",lMission."Master Contact No.");
          //                 IF lMissionServiceTemplateLine."Unit of Measure Code" <> '' THEN BEGIN
          //                   "Unit of Measure Code" := lMissionServiceTemplateLine."Unit of
          // Measure Code";
          //                 END;
          //                 "Mis. Service Template Code" := lMissionServiceTemplateLine."Service
          // Template Code";
          //                 VALIDATE(Quantity,1);
          //                 "Accept To Invoice" := TRUE;
          //                 "Activity Header" := lActivityLine."Activity Code";
          //                 "Activity Line" := lActivityLine."Line No.";
          //                 lMissionServPriceMgt.FindMissServPWithBaseAmount(lMissionServiceLine,
          // pBaseAmount, FALSE);
          //                 rAmountCommission += lMissionServiceLine."Amount Incl. VAT";
          //               END;
          //             END;
        }
      }
    }

    // Ap09 isat.zw 17/11/08

    // IF NOT lActivityHeader.GET(lMission."Activity Code To Lines") THEN
    //   CLEAR(lActivityHeader);
    // lActivityLine.SETRANGE(lActivityLine."Activity Code", lActivityHeader.Code);
    // IF lActivityLine.FINDSET(FALSE, FALSE) THEN BEGIN
    //   REPEAT
    //     IF lActivityLine."Service Template Code" <> '' THEN BEGIN
    //       lMissionServiceTemplateLine.RESET;

    //       lMissionServiceTemplateLine.SETRANGE("Service Template Code",lActivityLine."Service
    // Template Code");
    //       IF lMissionServiceTemplateLine.FIND('-') THEN BEGIN
    //         REPEAT
    //
    //           END;
    //         UNTIL lMissionServiceTemplateLine.NEXT = 0;
    //       END;
    //     END;
    //   UNTIL lActivityLine.NEXT = 0;
    // END;
    return null;
  }

  @Override
  public String CreateLotFromJudicialMission(
      MissionLine pOriginMissionLine, LotQuickInputJournal pLotQuickInputJournal) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void PostLotQuickInputFromJuMission(
      LotQuickInputJournal pLotQuickInputJournal, MissionLine pOriginMissionLine) {
    // TODO Auto-generated method stub

  }

  @Override
  public void CreateLotInvValueEntry(MissionLine pMissionLine) {
    // TODO Auto-generated method stub

  }

  @Override
  public void CreateLotValueEntryFromLot(
      String pLotNo,
      Date pPostingDate,
      String pEntryType,
      BigDecimal pAmount,
      BigDecimal pMinAmount,
      BigDecimal pMaxAmount,
      Partner pContactNo) {
    // TODO Auto-generated method stub

  }

  @Override
  public void SetToRegistIntegrationLot(
      Boolean pToRegistIntegrationLot, Integer pRegistIntegrationEntryNo) {
    // TODO Auto-generated method stub

  }

  @Override
  public Lot GetLotNoCreated() {
    return lot;
  }
}
