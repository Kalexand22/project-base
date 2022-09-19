package com.axelor.apps.openauctionbase.service;

import com.axelor.apps.openauction.db.Lot;
import com.axelor.apps.openauction.db.LotInputJournal;
import com.axelor.apps.openauction.db.LotQuickInputJournal;
import com.axelor.apps.openauction.db.MissionHeader;
import com.axelor.apps.openauction.db.repo.LotRepository;
import com.axelor.inject.Beans;
import com.google.inject.Inject;
import java.time.LocalDate;

public class LotInputJournalPostLineImpl implements LotInputJournalPostLine {
  // LotInputJournal@1000000000 : Record 8011498;
  LotInputJournal lotInputJournal;
  LotRepository lotRepo;

  @Inject
  public LotInputJournalPostLineImpl(LotRepository lotRepo) {
    this.lotRepo = lotRepo;
  }

  @Override
  public LotInputJournal runWithCheck(LotInputJournal pLotInputJournal) {

    lotInputJournal = pLotInputJournal;
    code(true);
    pLotInputJournal = lotInputJournal;
    return pLotInputJournal;
  }

  @Override
  public LotInputJournal runWithoutCheck(LotInputJournal pLotInputJournal) {
    lotInputJournal = pLotInputJournal;
    code(false);
    pLotInputJournal = lotInputJournal;
    return pLotInputJournal;
  }

  private void code(Boolean check) {
    /*
       * lMissionHeader@1000000002 : Record 8011402;
    lTempLotQuickInputJournal@1000000003 : TEMPORARY Record 8011467;
    lAPLotTemplateMgt@1000000004 : Codeunit 8011377;
    lTextWarehouse@1000000012 : Record 8011363;
    lLotNoCreated@1000000011 : Code[20];
    lOutStream@1000000013 : OutStream;
       */
    MissionHeader lMissionHeader = lotInputJournal.getDocumentNo();
    LotQuickInputJournal lTempLotQuickInputJournal = new LotQuickInputJournal();
    LotTemplateManagement lAPLotTemplateMgt = Beans.get(LotTemplateManagement.class);

    lTempLotQuickInputJournal.setMissionNo(lotInputJournal.getDocumentNo());
    lTempLotQuickInputJournal.setMissionLineNo(lotInputJournal.getDocumentLineNo());
    lTempLotQuickInputJournal.setValuationAtBest(lotInputJournal.getValuationAtBest());
    lTempLotQuickInputJournal.setLotTemplateCode(lotInputJournal.getLotTemplateCode());
    lTempLotQuickInputJournal.setDescription(lotInputJournal.getDescription());
    lTempLotQuickInputJournal.setQuantity(lotInputJournal.getQuantity());
    lTempLotQuickInputJournal.setAppraisalValue(lotInputJournal.getValue());
    lTempLotQuickInputJournal.setMinAppraisalValue(lotInputJournal.getMinimumValue());
    lTempLotQuickInputJournal.setMaxAppraisalValue(lotInputJournal.getMaximumValue());
    lTempLotQuickInputJournal.setOriginCountryCode(lotInputJournal.getOriginCountryCode());
    lTempLotQuickInputJournal.setValueType(lotInputJournal.getValueType());
    lTempLotQuickInputJournal.setGrossReservePrice(lotInputJournal.getGrossReservePrice());
    lTempLotQuickInputJournal.setNetReservePrice(lotInputJournal.getNetReservePrice());
    lTempLotQuickInputJournal.setMinAuctionEstimValue(lotInputJournal.getMinAuctionEstimValue());
    lTempLotQuickInputJournal.setMaxAuctionEstimValue(lotInputJournal.getMaxAuctionEstimValue());
    // TODO WORKDATE
    lTempLotQuickInputJournal.setPostingDate(LocalDate.now());
    lTempLotQuickInputJournal.setDocumentDate(LocalDate.now());
    lTempLotQuickInputJournal.setDescription(lotInputJournal.getDescription());
    lTempLotQuickInputJournal.setCurrencyCode(lotInputJournal.getCurrencyCode());
    lTempLotQuickInputJournal.setCurrencyFactor(lotInputJournal.getCurrencyFactor());
    lTempLotQuickInputJournal.setIndentation(lotInputJournal.getIndentation());
    lTempLotQuickInputJournal.setLineType(lotInputJournal.getLineType());
    lTempLotQuickInputJournal.setLocationCode(lotInputJournal.getLocationCode());
    lTempLotQuickInputJournal.setBinCode(lotInputJournal.getBinCode());
    lTempLotQuickInputJournal.setAuctionRoomCode(lotInputJournal.getAuctionRoomCode());
    lTempLotQuickInputJournal.setOriginCountryCode(lotInputJournal.getOriginCountryCode());
    lTempLotQuickInputJournal.setResponsibilityCenter(lMissionHeader.getResponsibilityCenter());
    lTempLotQuickInputJournal.setMainTextEntryNo(lotInputJournal.getMainTextEntryNo());
    lTempLotQuickInputJournal.setTemporaryBlob(lotInputJournal.getTemporaryBlob());
    lTempLotQuickInputJournal.setLotNo(lotInputJournal.getLotNo());
    lTempLotQuickInputJournal.setDateToAuctionFrom(lotInputJournal.getDateToAuctionfrom());
    lTempLotQuickInputJournal.setLotConditionCode(lotInputJournal.getLotConditionCode());
    lTempLotQuickInputJournal.setLotNoMission(lotInputJournal.getLotNoMission());
    lTempLotQuickInputJournal.setExternalNo(lotInputJournal.getExternalNo());
    lTempLotQuickInputJournal.setInterlocutor(lotInputJournal.getInterlocutor());
    /*
     * lAPLotTemplateMgt.CreateLotFromMission(lTempLotQuickInputJournal,lMissionHeader);
    lAPLotTemplateMgt.GetLotNoCreated(lLotNoCreated);
    LotInputJournal."Created Lot No." := lLotNoCreated; //Ap03 isat.zw
     */
    Lot lot = lAPLotTemplateMgt.CreateLotFromMission(lTempLotQuickInputJournal, lMissionHeader);

    lot = lAPLotTemplateMgt.GetLotNoCreated();
    lotInputJournal.setCreatedLotNo(lot.getNo());
    lot = transferFields(lotInputJournal, lot);
    lotRepo.save(lot);
  }

  private Lot transferFields(LotInputJournal pLotInputJournal, Lot pLot) {
    pLot.setAgeCode(pLotInputJournal.getAgeCode());
    pLot.setAttributionType(pLotInputJournal.getAttributionType());
    pLot.setCentury(pLotInputJournal.getCentury());
    pLot.setCatalogDescription(pLotInputJournal.getCatalogDescription());
    pLot.setBibliography(pLotInputJournal.getBibliography());
    pLot.setAuthorName(pLotInputJournal.getAuthorName());
    pLot.setAuthorCode(pLotInputJournal.getAuthorCode());

    pLot.setCollection(pLotInputJournal.getCollection());
    pLot.setComment1(pLotInputJournal.getComment1());
    pLot.setCreationYear(pLotInputJournal.getCreationYear());
    pLot.setCubage(pLotInputJournal.getCubage());
    pLot.setCubageUnit(pLotInputJournal.getCubageUnit());
    pLot.setExhibition(pLotInputJournal.getExhibition());
    pLot.setFrom1(pLotInputJournal.getFrom1());
    pLot.setHeight(pLotInputJournal.getHeight());

    pLot.setHistoricalPeriod(pLotInputJournal.getHistoricalPeriod());
    pLot.setLength(pLotInputJournal.getLength());
    pLot.setLotNatureCode(pLotInputJournal.getLotNatureCode());
    pLot.setReproduction(pLotInputJournal.getReproduction());
    pLot.setSchoolCode(pLotInputJournal.getSchoolCode());
    pLot.setSchoolType(pLotInputJournal.getSchoolType());
    pLot.setStyleCode(pLotInputJournal.getStyleCode());
    pLot.setTechnicalDescription(pLotInputJournal.getTechnicalDescription());
    pLot.setTitle(pLotInputJournal.getTitle());
    pLot.setUnitofMeasure(pLotInputJournal.getUnitofMeasure());
    pLot.setWeight(pLotInputJournal.getWeight());
    pLot.setWeightUnit(pLotInputJournal.getWeightUnit());
    pLot.setWidth(pLotInputJournal.getWidth());

    pLot.setAlarm(pLotInputJournal.getAlarm());
    pLot.setAntiStartupCode(pLotInputJournal.getAntiStartupCode());
    pLot.setBatteryProblem(pLotInputJournal.getBatteryProblem());
    pLot.setBreakdownLorryCharge(pLotInputJournal.getBreakdownLorryCharge());

    pLot.setCarRadioFront(pLotInputJournal.getCarRadioFront());
    pLot.setColor(pLotInputJournal.getColor());
    pLot.setDamaged(pLotInputJournal.getDamaged());
    pLot.setDateOfFirstRegistration(pLotInputJournal.getDateOfFirstRegistration());
    pLot.setEmptyWeight(pLotInputJournal.getEmptyWeight());
    pLot.setEnergy(pLotInputJournal.getEnergy());
    pLot.setEngineTrade(pLotInputJournal.getEngineTrade());
    pLot.setEstimateKilometrage(pLotInputJournal.getEstimateKilometrage());
    pLot.setExternalReferenceCode(pLotInputJournal.getExternalReferenceCode());

    pLot.setGaged(pLotInputJournal.getGaged());
    pLot.setGrossTrailerWeight(pLotInputJournal.getGrossTrailerWeight());
    pLot.setGrossVehicleWeight(pLotInputJournal.getGrossVehicleWeight());
    pLot.setHorsePower(pLotInputJournal.getHorsePower());
    pLot.setKeys1(pLotInputJournal.getKeys1());
    pLot.setKilometrage(pLotInputJournal.getKilometrage());
    pLot.setKind(pLotInputJournal.getKind());
    pLot.setLotNatureCode(pLotInputJournal.getLotNatureCode());
    pLot.setMaintenanceNoteBook(pLotInputJournal.getMaintenanceNoteBook());

    pLot.setRegistration(pLotInputJournal.getRegistration());
    pLot.setRegistrationDocument(pLotInputJournal.getRegistrationdocument());
    pLot.setRehabilitationcosts(pLotInputJournal.getRehabilitationcosts());
    pLot.setRegrouping(pLotInputJournal.getRegrouping());
    pLot.setSeatingCapacity(pLotInputJournal.getSeatingCapacity());
    pLot.setSpareWheel(pLotInputJournal.getSpareWheel());
    pLot.setTechnicalDescription(pLotInputJournal.getTechnicalDescription());
    pLot.setUnroadworthy(pLotInputJournal.getUnroadworthy());
    pLot.setvIN(pLotInputJournal.getvIN());

    pLot.setYear(pLotInputJournal.getYear());

    pLot.setAppellation(pLotInputJournal.getAppellation());
    pLot.setCap(pLotInputJournal.getCap());
    pLot.setVineyard(pLotInputJournal.getVineyard());

    pLot.setCapacity(pLotInputJournal.getCapacity());
    pLot.setDiameter(pLotInputJournal.getDiameter());
    pLot.setGrapeHarvest(pLotInputJournal.getGrapeHarvest());
    pLot.setGrapeVariety(pLotInputJournal.getGrapeVariety());
    pLot.setLabel(pLotInputJournal.getLabel());
    pLot.setLevel1(pLotInputJournal.getLevel1());
    pLot.setName(pLotInputJournal.getName());
    pLot.setPotentialofAgeing(pLotInputJournal.getPotentialofAgeing());
    pLot.setPreserving(pLotInputJournal.getPreserving());

    pLot.setTechnicalDescription(pLotInputJournal.getTechnicalDescription());
    pLot.setVintage(pLotInputJournal.getVintage());

    pLot.setAknowledgmentofReceipt(pLotInputJournal.getAknowledgmentofReceipt());
    pLot.setEngineTrade(pLotInputJournal.getEngineTrade());
    pLot.setEquipmentBrandNo(pLotInputJournal.getEquipmentBrandNo());
    pLot.setFolderType(pLotInputJournal.getFolderType());
    pLot.setKindofMaterial(pLotInputJournal.getKindofMaterial());

    pLot.setReference(pLotInputJournal.getReference());
    pLot.setSerialNo(pLotInputJournal.getSerialNo());
    return pLot;
  }
}
