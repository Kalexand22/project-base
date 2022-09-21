package com.axelor.apps.openauctionbase.validatefield;

import com.axelor.apps.account.db.FiscalPosition;
import com.axelor.apps.base.db.Product;
import com.axelor.apps.base.db.Unit;
import com.axelor.apps.openauction.db.AuctionHeader;
import com.axelor.apps.openauction.db.Lot;
import com.axelor.apps.openauction.db.MissionHeader;
import com.axelor.apps.openauction.db.MissionServiceLine;
import com.axelor.apps.openauction.db.repo.MissionServiceLineRepository;
import com.axelor.exception.AxelorException;
import com.axelor.exception.db.repo.TraceBackRepository;
import java.math.BigDecimal;
import java.time.LocalDate;

public class MissionServiceLineValidatefield {

  // Mission_No Code20
  // OnValidateBEGIN
  // GetMission;
  // "Mission Template Code" := MissionHeader."Mission Template Code";
  // MissionHeader.TESTFIELD("VAT Business Posting Group");
  //// <<AP35.ST
  //
  // IF "Lot No." = '' THEN //AP21.ST
  //  VALIDATE("Seller VAT Bus. Posting Group", MissionHeader."VAT Business Posting Group");
  //// "Margin Based VAT" := MissionHeader."Margin Based VAT"; AP21.ST désactivé
  //
  // GetMissVATBus;
  //// >>AP35.ST
  // IF "No." <> '' THEN
  //  VALIDATE("Contact Imputation Type");

  // IF MissionHeader."Responsibility Center" <> '' THEN
  //  "Responsibility Center" := MissionHeader."Responsibility Center"; // AP11 isat.sf

  //// ChangeDocumentNo; //ap46 isat.zw//
  public MissionServiceLine validateFieldMissionNo(
      MissionServiceLine missionServiceLine, MissionHeader missionNo) throws AxelorException {
    missionServiceLine.setMissionTemplateCode(missionNo.getMissionTemplateCode());
    if (missionNo.getFiscalPosition() == null) {
      throw new AxelorException(
          TraceBackRepository.CATEGORY_INCONSISTENCY, "La position fiscale est obligatoire");
    }
    missionServiceLine = this.GetMissVATBus(missionServiceLine);
    if (missionServiceLine.getProductNo() != null) {
      missionServiceLine =
          this.validateFieldContactImputationType(
              missionServiceLine, missionServiceLine.getContactImputationType());
    }

    if (missionNo.getResponsibilityCenter() != null) {
      missionServiceLine.setResponsibilityCenter(missionNo.getResponsibilityCenter());
    }
    return missionServiceLine;
  }

  private MissionServiceLine GetMissVATBus(MissionServiceLine missionServiceLine)
      throws AxelorException {
    FiscalPosition fiscalPosition = null;
    if (missionServiceLine.getLotNo() != null) {
      fiscalPosition = missionServiceLine.getLotNo().getFiscalposition();
    }
    if (fiscalPosition == null && missionServiceLine.getMissionNo() != null) {
      fiscalPosition = missionServiceLine.getMissionNo().getFiscalPosition();
    }
    if (fiscalPosition == null
        && missionServiceLine.getContactImputationType()
            == MissionServiceLineRepository.CONTACTIMPUTATIONTYPE_SELECT_SELLER
        && missionServiceLine.getChargeableContactNo() != null) {
      fiscalPosition = missionServiceLine.getChargeableContactNo().getFiscalPosition();
    }
    if (fiscalPosition != missionServiceLine.getSellerFiscalPosition()) {
      missionServiceLine =
          this.validateFieldSellerFiscalePosition(missionServiceLine, fiscalPosition);
    }

    return missionServiceLine;
  }

  private MissionServiceLine validateFieldSellerFiscalePosition(
      MissionServiceLine missionServiceLine, FiscalPosition fiscalPosition) {
    return null;
  }

  // Auction_No Code20
  // OnValidateVAR
  // lAuctionHeader@1000000000 : Record 8011400;
  // BEGIN
  // IF "Auction No." <> '' THEN BEGIN
  //  lAuctionHeader.GET("Auction No.");

  //  "Auction Template Code" := lAuctionHeader."Auction Template Code";
  //  "Auction Incl. VAT" := lAuctionHeader."Prices Including VAT";
  //// <<AP24.ST
  //  IF ("Transaction Type" = "Transaction Type"::Auction) AND ("Price Date" <>
  // lAuctionHeader."Auction Date") THEN
  //    VALIDATE("Price Date", lAuctionHeader."Auction Date");
  //// >>AP24.ST
  //// << AP19 isat.SF
  // END ELSE BEGIN
  //  "Auction Template Code" := '';
  //  "Auction Incl. VAT" := FALSE;
  //// <<AP24.ST
  //  IF ("Transaction Type" = "Transaction Type"::Auction) THEN
  //    "Price Date" := WORKDATE;
  //// >>AP24.ST
  // END;
  //// >> AP19 isat.SF//
  public MissionServiceLine validateFieldAuctionNo(
      MissionServiceLine missionServiceLine, AuctionHeader auctionNo) {
    missionServiceLine.setAuctionTemplateCode(auctionNo.getAuctionTemplate());
    missionServiceLine.setAuctionInclVAT(auctionNo.getPricesIncludingVAT());
    if (missionServiceLine
            .getTransactionType()
            .equals(MissionServiceLineRepository.TRANSACTIONTYPE_SELECT_VENTE)
        && missionServiceLine.getPriceDate() != auctionNo.getAuctionDate()) {
      missionServiceLine =
          this.validateFieldPriceDate(missionServiceLine, auctionNo.getAuctionDate());
    } else {
      missionServiceLine.setAuctionTemplateCode(null);
      missionServiceLine.setAuctionInclVAT(false);
      if (missionServiceLine
          .getTransactionType()
          .equals(MissionServiceLineRepository.TRANSACTIONTYPE_SELECT_VENTE)) {
        missionServiceLine.setPriceDate(LocalDate.now());
        // TODO Workdate
      }
    }
    return missionServiceLine;
  }

  // Lot_No Code20
  // OnValidateBEGIN
  // GetLot;
  // "Lot Template Code" := Lot."Lot Template Code";
  // "Responsibility Center" := Lot."Responsibility Center";
  // GetMissVATBus;//AP21.ST

  // CASE "Transaction Type" OF
  //  "Transaction Type"::Mission : BEGIN
  //     "Lot Price Group" := Lot."Lot Mission Price Group";
  //     "Transaction Line No." := Lot."Current Mission Line No.";
  //  END;
  //  "Transaction Type"::Auction : BEGIN
  //     "Lot Price Group" := Lot."Lot Auction Price Group";
  //     //"Transaction Line No." := Lot."Current Auction Line No."; // isat.sf géré au niveau de
  // l'auction line
  //   END;
  // END;
  //// <<AP03.ISAT.ST
  // IF "No." <> '' THEN
  //  VALIDATE("No.");
  //// >>AP03.ISAT.ST//
  public MissionServiceLine validateFieldLotNo(MissionServiceLine missionServiceLine, Lot lot)
      throws AxelorException {

    missionServiceLine.setLotTemplateCode(lot.getLotTemplateCode());
    missionServiceLine.setResponsibilityCenter(lot.getResponsibilityCenter());
    missionServiceLine = this.GetMissVATBus(missionServiceLine);
    if (missionServiceLine
        .getTransactionType()
        .equals(MissionServiceLineRepository.TRANSACTIONTYPE_SELECT_MISSION)) {
      missionServiceLine.setLotPriceGroup(lot.getLotMissionPriceGroup());
      missionServiceLine.setTransactionLineNo(lot.getCurrentMissionLineNo().getLineNo());
    } else if (missionServiceLine
        .getTransactionType()
        .equals(MissionServiceLineRepository.TRANSACTIONTYPE_SELECT_VENTE)) {
      missionServiceLine.setAuctionLotPriceGroup(lot.getLotAuctionPriceGroup());
    }
    if (missionServiceLine.getProductNo() != null) {
      missionServiceLine =
          this.validateFieldNo(missionServiceLine, missionServiceLine.getProductNo());
    }
    return missionServiceLine;
  }

  //// >>AP03.ISAT.ST
  //    //GetVat2014; // AP50 isat.sf
  //    //"Commission Type" := Item."Commission Type";
  //    VALIDATE("Contact Imputation Type",Item."Contact Imputation Type");
  //    //"Lot Imputation Type" := Item."Lot Imputation Type";
  //    "Service Type" := Item."Service Type";
  //    "Imputation Base" := Item."Imputation Base";
  //    Appreciation := Item."Value Added Item";
  //    VALIDATE("Unit of Measure Code",Item."Sales Unit of Measure"); // Recalcul le prix (via
  // quantité)
  //    "Gen. Prod. Posting Group" := Item."Gen. Prod. Posting Group";
  //    VALIDATE("Unit Cost", Item."Unit Cost");
  //  END;
  //  Type::Resource :  BEGIN
  //    GetResource;
  //    Description := Resource.Name;
  //    VALIDATE("Unit of Measure Code",Resource."Base Unit of Measure");
  //  END;
  // END;
  // VALIDATE("VAT Prod. Posting Group");//
  public MissionServiceLine validateFieldNo(
      MissionServiceLine missionServiceLine, Product productNo) {

    missionServiceLine.setDescription(productNo.getDescription());
    if (missionServiceLine.getAuctionBid()) {
      missionServiceLine.setInvoicingType(
          MissionServiceLineRepository.INVOICINGTYPE_SELECT_BILLABLEONBID);
      missionServiceLine.setChargeable(true);
      missionServiceLine.setQuantity(BigDecimal.ONE);
    } else {
      missionServiceLine.setInvoicingType(productNo.getInvoicingType());
    }

    if (productNo.getUseProductFamilyLot()) {
      missionServiceLine.setProductFamily(missionServiceLine.getLotNo().getAuctionProductFamily());
    } else {
      missionServiceLine.setProductFamily(productNo.getProductFamily());
    }
    missionServiceLine.setCommissionType(productNo.getCommissionType());
    missionServiceLine =
        this.validateFieldContactImputationType(
            missionServiceLine, productNo.getContactImputationType());
    missionServiceLine.setServiceType(productNo.getServiceType());
    missionServiceLine.setImputationBase(productNo.getImputationBase());
    missionServiceLine.setAppreciation(productNo.getValueAddedItem());
    missionServiceLine =
        this.validateFieldUnitOfMeasureCode(missionServiceLine, productNo.getSalesUnit());

    return missionServiceLine;
  }

  public MissionServiceLine validateFieldUnitOfMeasureCode(
      MissionServiceLine missionServiceLine, Unit salesUnit) {
    return missionServiceLine;
  }

  public MissionServiceLine validateFieldPriceDate(
      MissionServiceLine missionServiceLine, LocalDate auctionDate) {
    return missionServiceLine;
  }

  public MissionServiceLine validateFieldContactImputationType(
      MissionServiceLine missionServiceLine, String contactImputationType) {
    return missionServiceLine;
  }
}
