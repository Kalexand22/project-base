package com.axelor.apps.openauctionbase.service;

import com.axelor.apps.openauction.db.Lot;
import com.axelor.apps.openauction.db.LotQuickInputJournal;
import com.axelor.apps.openauction.db.MissionHeader;
import com.axelor.apps.openauction.db.MissionLine;

public class MissionLineManagementImpl implements MissionLineManagement {

    
 
    @Override
    public void SendLotToAuction(MissionLine pMissionLine) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Integer LotInsertMission(MissionHeader pMissionHeader, Lot pLot, MissionLine pMissionLine,
            LotQuickInputJournal pLotQuickInputJournal) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void DuplicateMissionLine(MissionLine pMissionLine, Boolean pSameLotNo) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Boolean IsAffectedLotInMission(MissionHeader pMissionHeader, Lot pLot) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Boolean DeleteMissionLine(MissionLine pMissionLine) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void SortMissionLine(MissionHeader pMission) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public MissionLine CreateMissionLine(MissionHeader pMissionHeader, Lot pLot,
            LotQuickInputJournal pLotQuickInputJournal) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void CreateCommentMissionLine(MissionHeader pMissionHeader, LotQuickInputJournal pLotQuickInputJournal) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public String GetMissionLineNo(MissionHeader pMissionHeaderNo) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String ExistPostedBuyerInvoice(MissionHeader pMissionNo, Lot pLotNo) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String ExistPostedSellerInvoice(MissionHeader pMissionNo, Lot pLotNo) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String ExistPostedMissionInvoice(MissionHeader pMissionNo, Lot pLotNo) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Integer LotInsertMissionNotAuction(MissionHeader pMissionHeader, Lot pLot, MissionLine pMissionLine,
            LotQuickInputJournal pLotQuickInputJournal) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Boolean CreateMissionLineNotAuction(MissionHeader pMissionHeader, MissionLine pMissionLine, Lot pLot,
            LotQuickInputJournal pLotQuickInputJournal) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Integer GetNewLotMissionNo(MissionHeader pMissionNo) {
        // TODO Auto-generated method stub
        return null;
    }
}
