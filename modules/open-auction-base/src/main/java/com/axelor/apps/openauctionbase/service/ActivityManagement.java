package com.axelor.apps.openauctionbase.service;

import com.axelor.apps.base.db.Partner;
import com.axelor.apps.openauction.db.ActivityHeader;
import com.axelor.apps.openauction.db.AuctionHeader;
import com.axelor.apps.openauction.db.Lot;
import com.axelor.apps.openauction.db.LotTemplate;
import com.axelor.apps.openauction.db.MissionHeader;
import com.axelor.apps.openauction.db.MissionLine;

public interface ActivityManagement {
  public void CreateActivityLines(
      ActivityHeader pActivityHeader,
      AuctionHeader pAuctionHeader,
      MissionHeader pMissionHeader,
      Lot pLotNo,
      LotTemplate pLotTemplate,
      Boolean pIsAuction,
      Boolean pIsActionOnly,
      Integer pTransactionMineNo);

  public void RemoveActivityLines(
      ActivityHeader pActivityHeader,
      AuctionHeader pAuctionHeader,
      MissionHeader pMissionHeader,
      Lot pLotNo,
      Integer pTransactionMineNo);

  public void CreateTodo(
      ActivityHeader pActivityHeader,
      AuctionHeader pAuctionHeader,
      MissionHeader pMissionHeader,
      Lot pLotNo,
      Integer pTransactionMineNo,
      Partner pContact,
      Partner pSalesPerson);

  public void CreateActivityLineFromMission(
      ActivityHeader pActivityHeader,
      MissionHeader pMissionHeader,
      MissionLine pMissionLine,
      Boolean pActionOnly);
}
