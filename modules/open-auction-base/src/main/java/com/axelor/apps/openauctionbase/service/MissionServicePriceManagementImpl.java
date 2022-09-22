package com.axelor.apps.openauctionbase.service;

import com.axelor.apps.base.db.Product;
import com.axelor.apps.openauction.db.Lot;
import com.axelor.apps.openauction.db.MissionHeader;
import com.axelor.apps.openauction.db.MissionServiceLine;
import java.util.Date;

public class MissionServicePriceManagementImpl implements MissionServicePriceManagement {

  @Override
  public MissionServiceLine findMissionServicePrice(MissionServiceLine pMissionServiceLine, Boolean pEstimated) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void findMissServPWithBaseAmount(
      MissionServiceLine pMissionServiceLine, Double pBaseAmount, Boolean pEstimated) {
    // TODO Auto-generated method stub

  }

  @Override
  public Boolean testPrice(
      MissionServiceLine pServiceMissionPrice,
      String DocTemplate,
      String DocNo,
      String LotGroup,
      String LotCode,
      String ContactGroup,
      String ContactCode) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Double getLotAmount(MissionHeader pMissionHeaderNo, Lot pLotNo) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Double getMissionAmount(MissionHeader pMissionHeaderNo) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void getTempServiceMissionPrice(MissionServiceLine pMissionServicePrice) {
    // TODO Auto-generated method stub

  }

  @Override
  public Double getTariffScaleAmount(Double pAmount, String pTariffScale, String pAccesBuffer) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void getTariffScaleDetail(String pAccesBuffer) {
    // TODO Auto-generated method stub

  }

  @Override
  public Double getBaseAMount() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void searchBidLine(
      MissionHeader pMissionHeaderNo, Lot pLotNo, MissionServiceLine pBidMissionServiceLine) {
    // TODO Auto-generated method stub

  }

  @Override
  public void modifyPrice(MissionServiceLine pRec, MissionServiceLine pXRec, String pAction) {
    // TODO Auto-generated method stub

  }

  @Override
  public void applyNewPrice(MissionServiceLine pMissionServicePrice, String pAction) {
    // TODO Auto-generated method stub

  }

  @Override
  public void getUsedServPriceForMission(
      MissionHeader pMissionNo, MissionServiceLine pMissionServicePrice) {
    // TODO Auto-generated method stub

  }

  @Override
  public void markMissionServPrice(
      MissionServiceLine pMissionServiceLine, MissionServiceLine pMissionServicePrice) {
    // TODO Auto-generated method stub

  }

  @Override
  public void getServPriceByMissServLine(
      MissionServiceLine pMissionServiceLine, MissionServiceLine pMissionServicePrice) {
    // TODO Auto-generated method stub

  }

  @Override
  public void getServPriceByLotNoItemNo(
      Lot pLotNo, Product pItemNo, MissionServiceLine pMissionServicePrice) {
    // TODO Auto-generated method stub

  }

  @Override
  public Date getPriceDate(MissionServiceLine pMissServLine) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void VATConvert(Product pItem, MissionServiceLine pMissServLine) {
    // TODO Auto-generated method stub

  }

  @Override
  public String getSellerComByAuctionLine(
      MissionServiceLine pAuctionLine, String pCalcType, Double pService, Double pUnitPrice) {
    // TODO Auto-generated method stub
    return null;
  }
}
