package com.axelor.apps.openauctionbase.service;

import com.axelor.apps.openauction.db.Lot;
import com.axelor.apps.openauction.db.repo.LotRepository;

public class LotStatusMgtImpl implements LotStatusMgt {

  @Override
  public Lot CheckGeneralStatus(Lot pLot) {
    /*
     * 
        CASE "Lot General Status" OF
          "Lot General Status"::Return : BEGIN
            "Auction Status" := "Auction Status"::NoAuction;  //ap08 isat.zw
          END;
          "Lot General Status"::Canceled : BEGIN
            "Auction Status" := "Auction Status"::NoAuction;  //ap08 isat.zw
          END;
        END;

        Closed := "Lot General Status" IN ["Lot General Status"::Sold, "Lot General Status"::Return, "Lot General Status"::Canceled];
        IF Closed THEN
          UpdRemainAuctionLines(CurrLot."No."); // mise à jour des lignes en invendable
     */
    switch (pLot.getLotGeneralStatus()) {
      case LotRepository.LOTGENERALSTATUS_RETURN:
        pLot.setAuctionStatus(LotRepository.AUCTIONSTATUS_NOAUCTION);
        break;

      case LotRepository.LOTGENERALSTATUS_CANCELED:
        pLot.setAuctionStatus(LotRepository.AUCTIONSTATUS_NOAUCTION);
        break;
    }
    boolean closed =
        pLot.getLotGeneralStatus()
            .equals(LotRepository.LOTGENERALSTATUS_SOLD)
            || pLot.getLotGeneralStatus()
                .equals(LotRepository.LOTGENERALSTATUS_RETURN)
            || pLot.getLotGeneralStatus().equals(LotRepository.LOTGENERALSTATUS_CANCELED);
    if (closed) {
      updRemainAuctionLines(pLot);
    }
    return pLot;
  }

  private void updRemainAuctionLines(Lot no) {
  }

  @Override
  public Lot UpdLotInventoryStatus(Lot pLot) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Lot UpdLotOperationStatus(Lot pLot) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Lot UpdLotMissionStatus(Lot pLot) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Lot UpdLotAuctionStatus(Lot pLot) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Lot UpdateLotStatusByTodo() {
    // TODO Auto-generated method stub
    return null;
  }
}
