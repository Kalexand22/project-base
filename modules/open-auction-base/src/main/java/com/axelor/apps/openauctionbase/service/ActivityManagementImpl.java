package com.axelor.apps.openauctionbase.service;

import com.axelor.apps.base.db.Partner;
import com.axelor.apps.openauction.db.ActivityHeader;
import com.axelor.apps.openauction.db.ActivityLine;
import com.axelor.apps.openauction.db.AuctionHeader;
import com.axelor.apps.openauction.db.Lot;
import com.axelor.apps.openauction.db.LotTemplate;
import com.axelor.apps.openauction.db.MissionHeader;
import com.axelor.apps.openauction.db.MissionLine;
import com.axelor.apps.openauction.db.MissionServiceLine;
import com.axelor.apps.openauction.db.ServiceTemplate;
import com.axelor.apps.openauction.db.ServiceTemplateLine;
import com.axelor.apps.openauction.db.repo.ActivityHeaderRepository;
import com.axelor.apps.openauction.db.repo.ActivityLineRepository;
import com.axelor.apps.openauction.db.repo.MissionActivityLineRepository;
import com.axelor.apps.openauction.db.repo.MissionServiceLineRepository;
import com.axelor.apps.openauctionbase.repository.MissionServiceLineRepositoryExt;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import java.lang.invoke.MethodHandles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActivityManagementImpl implements ActivityManagement {
  MissionServiceLineRepositoryExt missionServiceLineRepository;
  MissionActivityLineRepository missionActivityLineRepository;
  private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  @Inject
  public ActivityManagementImpl(
      MissionServiceLineRepositoryExt missionServiceLineRepository,
      MissionActivityLineRepository missionActivityLineRepository) {
    this.missionServiceLineRepository = missionServiceLineRepository;
    this.missionActivityLineRepository = missionActivityLineRepository;
  }

  @Override
  public void CreateActivityLines(
      ActivityHeader pActivityHeader,
      AuctionHeader pAuctionHeader,
      MissionHeader pMissionHeader,
      Lot pLotNo,
      LotTemplate pLotTemplate,
      Boolean pIsAuction,
      Boolean pIsActionOnly,
      Integer pTransactionLineNo) {

    Boolean lLineToTreat = false;
    Partner lcontact = pMissionHeader != null ? pMissionHeader.getMasterContactNo() : null;
    ;

    // TODO pActivityHeader.TestUsage(lDate);

    for (ActivityLine line : pActivityHeader.getActivityLineList()) {
      lLineToTreat = false;
      if (line.getTodoCondition() == null || !line.getTodoCondition().equals("")) {
        log.debug(pActivityHeader.getApplicableOn());
        if (pActivityHeader
            .getApplicableOn()
            .equals(ActivityHeaderRepository.APPLICABLEON_HEADER)) {
          lLineToTreat = true;
        }
        if (pActivityHeader.getApplicableOn().equals(ActivityHeaderRepository.APPLICABLEON_LINE)) {
          lLineToTreat =
              line.getLotTemplateFilter().equals(pLotTemplate)
                  || line.getLotTemplateFilter() == null;
        }
        if (line.getToDoApplicableTo().equals(ActivityLineRepository.TODOAPPLICABLETO_SELLER)) {
          lcontact = pMissionHeader.getMasterContactNo();
        } else {
          lcontact = null;
        }

        if (lLineToTreat) {
          if (line.getServiceTemplateCode() != null)
            CreateMissionService(
                line,
                pAuctionHeader,
                pMissionHeader,
                pLotNo,
                lcontact,
                pIsAuction,
                pTransactionLineNo);
          // TODO gestion Interaction + Todo
          /*
          *
             IF (lActivityLine."Interaction Template Code" <> '') AND (pActionOnly = FALSE) THEN BEGIN
               CreateInteraction(lActivityLine,pMissionNo,pAuctionNo,pLotNo,pTransactionLineNo); //AP09
             END;
             IF lActivityLine."Todo Template Code" <> '' THEN BEGIN
               CreateTodo(lActivityLine,pMissionNo,pAuctionNo,pLotNo,lContactNo,lSalespersonCode,pTransactionLineNo); //AP09
             END;
          */
        }
      }
    }
  }

  @Override
  @Transactional
  public void RemoveActivityLines(
      ActivityHeader pActivityHeader,
      AuctionHeader pAuctionHeader,
      MissionHeader pMissionHeader,
      Lot pLotNo,
      Integer pTransactionMineNo) {
    // TODO Auto-generated method stub
  }

  @Override
  @Transactional
  public void CreateTodo(
      ActivityHeader pActivityHeader,
      AuctionHeader pAuctionHeader,
      MissionHeader pMissionHeader,
      Lot pLotNo,
      Integer pTransactionMineNo,
      Partner pContact,
      Partner pSalesPerson) {
    // TODO Auto-generated method stub

  }

  @Override
  public void CreateActivityLineFromMission(
      ActivityHeader pActivityHeader,
      MissionHeader pMissionHeader,
      MissionLine pMissionLine,
      Boolean pActionOnly) {
    Lot lLot = pMissionLine.getNoLot();
    LotTemplate lLotTemplate = lLot == null ? null : lLot.getLotTemplateCode();
    CreateActivityLines(
        pActivityHeader,
        null,
        pMissionHeader,
        lLot,
        lLotTemplate,
        false,
        pActionOnly,
        pMissionLine.getLineNo());
  }


  //TODO onValidate
  @Transactional
  private void CreateMissionService(
      ActivityLine pActivityLine,
      AuctionHeader pAuctionHeader,
      MissionHeader pMissionHeader,
      Lot pLot,
      Partner pContact,
      Boolean pAuctionAct,
      Integer pTransactionLineNo) {
    ServiceTemplate missionServiceTemplate;
    MissionServiceLine lMissionServiceLine = new MissionServiceLine();

    if (pActivityLine.getServiceTemplateCode() == null) return;

    missionServiceTemplate = pActivityLine.getServiceTemplateCode();
    for (ServiceTemplateLine missionServiceTemplateLine :
        missionServiceTemplate.getTemplateLineList()) {
      lMissionServiceLine.setEntryNo(0);
      lMissionServiceLine.setDocumentNo(0L);
      lMissionServiceLine.setTransactionType(
          pAuctionAct
              ? MissionServiceLineRepository.TRANSACTIONTYPE_VENTE
              : MissionServiceLineRepository.TRANSACTIONTYPE_MISSION);
      lMissionServiceLine.setAuctionNo(pAuctionHeader);
      lMissionServiceLine.setMissionNo(pMissionHeader);
      lMissionServiceLine.setLotNo(pLot);
      lMissionServiceLine.setTransactionLineNo(pTransactionLineNo);
      lMissionServiceLine.setType(missionServiceTemplateLine.getType());
      // TODO setChargeableContactNo
      // lMissionServiceLine.setChargeableContactNo(pContact.getI);
      //TODO eventuellement revoir le insert(true)
      missionServiceLineRepository.save(lMissionServiceLine);
    }
    /*
     * LOCAL PROCEDURE CreateMissionService@1000000019(pActivityLine@1000000000 : Record 8011406;pMissionNo@1000000014 : Code[20];pAuctionNo@1000000013 : Code[20];pLotNo@1000000006 : Code[20];pContactNo@1000000009 : Code[20];pAuctionAct@1100481000 : Boolean;pTransactionLineNo@1100481001 : Integer);
    VAR
      lMissionSetup@1000000007 : Record 8011420;
      lMissionServiceTemplate@1000000001 : Record 8011409;
      lMissionServiceTemplateLine@1000000002 : Record 8011410;
      lMissionServiceLine@1000000004 : Record 8011449;
      lMissionActivityLine@1000000003 : Record 8011457;
      lValidateOK@1000000008 : Boolean;
      NameDataTypeSubtypeLength@1000000005 : Integer;
    BEGIN
      //AP02.ISAT.ST
      IF pActivityLine."Service Template Code" = '' THEN BEGIN
        EXIT;
      END;

      //InitGlobalServiceEntryNo(gLastServiceEntryNo); //ap20 isat.zw


      //<<ISAT.ST avant validation du lot
            IF pAuctionAct THEN
              VALIDATE("Transaction Type", "Transaction Type"::Auction)
            ELSE
              VALIDATE("Transaction Type", "Transaction Type"::Mission);
      //>>ISAT.ST
            VALIDATE("Mission No.", pMissionNo);
            VALIDATE("Auction No.",pAuctionNo);
            VALIDATE("Lot No.",pLotNo);
            VALIDATE("Transaction Line No.",pTransactionLineNo);//AP09
            VALIDATE(Type,lMissionServiceTemplateLine.Type);
            VALIDATE("No.",lMissionServiceTemplateLine."No.");
            VALIDATE("Chargeable Contact No.",pContactNo);
            VALIDATE("Contact Imputation Type"); //AP17.ST
            IF lMissionServiceTemplateLine.Description <> '' THEN BEGIN
              Description := lMissionServiceTemplateLine.Description;
            END;
            IF lMissionServiceTemplateLine."Unit of Measure Code" <> '' THEN BEGIN
              "Unit of Measure Code" := lMissionServiceTemplateLine."Unit of Measure Code";
            END;
            "Mis. Service Template Code" := lMissionServiceTemplateLine."Service Template Code";
            VALIDATE(Quantity,1);
            "Accept To Invoice" := TRUE;
            "Activity Header" := pActivityLine."Activity Code";
            "Activity Line" := pActivityLine."Line No.";
            "Web Site Code" := lMissionServiceTemplateLine."Web Site Code"; // AP22

        //** A revoir : CG -> mission ou auction
        //**    "Responsibility Center" := pMissionHeader."Responsibility Center";
            UpdatePrice;
            //VALIDATE(lMissionServiceLine."Price Includes VAT",TRUE); // isat.sf AP12 //désactivé ap19 isat.zw
            INSERT(TRUE);
          END;

          //InitGlobalActivityEntryNo(gLastActivityEntryNo);   //ap20 isat.zw
          WITH lMissionActivityLine DO BEGIN
            //gLastActivityEntryNo += 1;
            INIT;
            //"Entry No." := gLastActivityEntryNo; //ap20 isat.zw
            "Entry No." := 0; //ap20 isat.zw
            "Document No." := ''; //ap20 isat.zw

            "Mission No." := pMissionNo;
            "Auction No." := pAuctionNo;
            "Lot No." := pLotNo;
            "Transaction Line No." := pTransactionLineNo;//AP09
            "Activity Code" := pActivityLine."Activity Code";
            "Activity Line No." := pActivityLine."Line No.";
            Type := Type::Service;
            "To-do No." := '';
            "Mission Service Entry No." := lMissionServiceLine."Entry No.";
            "Mission Service Doc. No." := lMissionServiceLine."Document No."; //ap20 isat.zw
            "Interaction Template Code" := '';
            "Previous Mission No." := '';
            INSERT(TRUE);
          END;
        UNTIL lMissionServiceTemplateLine.NEXT = 0;
      END;
    END;
     */
  }
}
