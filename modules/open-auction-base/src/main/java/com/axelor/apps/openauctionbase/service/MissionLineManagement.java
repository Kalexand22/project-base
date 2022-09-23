package com.axelor.apps.openauctionbase.service;

import com.axelor.apps.base.db.Product;
import com.axelor.apps.openauction.db.AuctionLine;
import com.axelor.apps.openauction.db.Lot;
import com.axelor.apps.openauction.db.LotQuickInputJournal;
import com.axelor.apps.openauction.db.MissionHeader;
import com.axelor.apps.openauction.db.MissionLine;
import com.axelor.apps.openauction.db.MissionServiceLine;
import com.axelor.exception.AxelorException;

public interface MissionLineManagement {
    
    //PROCEDURE MultipleLotInsertMission@1000000000(VAR pMissionHeader@1000000008 : Record 8011402);
    //public void MultipleLotInsertMission(MissionHeader pMissionHeader);
    //PROCEDURE SendLotToAuction@1000000005(VAR pMissionLine@1000000000 : Record 8011403);
    public void SendLotToAuction(MissionLine pMissionLine);
    //PROCEDURE LotInsertMission@1000000001(VAR pMissionHeader@1000000000 : Record 8011402;VAR pLot@1000000001 : Record 8011404;VAR pMissionLine@1000000002 : Record 8011403;pLotQuickInputJournal@1000000003 : Record 8011467) rValue : Integer;
    public Integer LotInsertMission(MissionHeader pMissionHeader,Lot pLot, MissionLine pMissionLine, LotQuickInputJournal pLotQuickInputJournal);
    //PROCEDURE DuplicateMissionLine@1000000002(VAR pMissionLine@1000000000 : Record 8011403;pSameLotNo@1000000006 : Boolean);
    public void DuplicateMissionLine(MissionLine pMissionLine, Boolean pSameLotNo);
    //PROCEDURE IsAffectedLotInMission@1000000008(pMissionHeader@1000000002 : Record 8011402;pLot@1000000001 : Record 8011404) rIsAffected : Boolean;
    public Boolean IsAffectedLotInMission(MissionHeader pMissionHeader, Lot pLot);
    //PROCEDURE DeleteMissionLine@1000000007(pMissionLine@1000000001 : Record 8011403) rValue : Boolean;
    public Boolean DeleteMissionLine(MissionLine pMissionLine);
    //PROCEDURE SortMissionLine@1000000003(pMission@1000000000 : Record 8011402);
    public void SortMissionLine(MissionHeader pMission);
    //PROCEDURE CreateMissionLine@1000000004(VAR pMissionHeader@1000000000 : Record 8011402;VAR pMissionLine@1000000002 : Record 8011403;VAR pLot@1000000001 : Record 8011404;pLotQuickInputJournal@1000000003 : Record 8011467) rValue : Boolean;
    public MissionLine CreateMissionLine(MissionHeader pMissionHeader, Lot pLot, LotQuickInputJournal pLotQuickInputJournal) throws AxelorException;
    //PROCEDURE CreateCommentMissionLine@1000000006(pMissionHeader@1000000000 : Record 8011402;pLotQuickInputJournal@1000000002 : Record 8011467);
    public void CreateCommentMissionLine(MissionHeader pMissionHeader, LotQuickInputJournal pLotQuickInputJournal);
    //PROCEDURE GetMissionLineNo@1000000009(pMissionHeaderNo@1000000001 : Code[20]);
    public String GetMissionLineNo(MissionHeader pMissionHeaderNo);
    //PROCEDURE ExistPostedBuyerInvoice@1100481000(VAR pMissionNo@1100481000 : Code[20];VAR pLotNo@1100481001 : Code[20]) rExist : Code[20];
    public String ExistPostedBuyerInvoice(MissionHeader pMissionNo, Lot pLotNo);
    //PROCEDURE ExistPostedSellerInvoice@1100481005(VAR pMissionNo@1100481000 : Code[20];VAR pLotNo@1100481001 : Code[20]) rExist : Code[20];
    public String ExistPostedSellerInvoice(MissionHeader pMissionNo, Lot pLotNo);
    //PROCEDURE ExistPostedMissionInvoice@1100481006(VAR pMissionNo@1100481000 : Code[20];VAR pLotNo@1100481001 : Code[20]) rExist : Code[20];
    public String ExistPostedMissionInvoice(MissionHeader pMissionNo, Lot pLotNo);
    //PROCEDURE LotInsertMissionNotAuction@1100481002(VAR pMissionHeader@1000000000 : Record 8011402;VAR pLot@1000000001 : Record 8011404;VAR pMissionLine@1000000002 : Record 8011403;pLotQuickInputJournal@1000000003 : Record 8011467) rValue : Integer;
    public Integer LotInsertMissionNotAuction(MissionHeader pMissionHeader, Lot pLot, MissionLine pMissionLine, LotQuickInputJournal pLotQuickInputJournal);
    //PROCEDURE CreateMissionLineNotAuction@1100481001(VAR pMissionHeader@1000000000 : Record 8011402;VAR pMissionLine@1000000002 : Record 8011403;VAR pLot@1000000001 : Record 8011404;pLotQuickInputJournal@1000000003 : Record 8011467) rValue : Boolean;
    public Boolean CreateMissionLineNotAuction(MissionHeader pMissionHeader, MissionLine pMissionLine, Lot pLot, LotQuickInputJournal pLotQuickInputJournal);
    //PROCEDURE GetNewLotMissionNo@1100481007(pMissionNo@1100481000 : Code[20]) : Integer;
    public Integer GetNewLotMissionNo(MissionHeader pMissionNo);
}
