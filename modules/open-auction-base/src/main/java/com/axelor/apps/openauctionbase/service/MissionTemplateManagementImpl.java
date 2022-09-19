package com.axelor.apps.openauctionbase.service;

import com.axelor.apps.base.db.Partner;
import com.axelor.apps.openauction.db.ActivityHeader;
import com.axelor.apps.openauction.db.MissionHeader;
import com.axelor.apps.openauction.db.MissionLine;
import com.axelor.apps.openauction.db.MissionTemplate;
import com.axelor.apps.openauction.db.repo.MissionHeaderRepository;
import com.axelor.inject.Beans;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

public class MissionTemplateManagementImpl implements MissionTemplateManagement {

  MissionHeaderRepository missionHeaderRepository;
  MissionTemplate MissionTemplate;
  Boolean HideTemplate;
  Boolean AuctionFromInv;
  Boolean SkipActivity;

  @Inject
  public MissionTemplateManagementImpl(MissionHeaderRepository missionHeaderRepository) {
    this.missionHeaderRepository = missionHeaderRepository;
    SkipActivity = false;
  }

  @Override
  @Transactional
  public MissionHeader CreateMissionFromMission(
      MissionHeader pMissionHeader,
      MissionTemplate pMissionTemplate,
      Boolean pJudicialFilter,
      String pLawyerBusNo) {
    pMissionHeader.setAuctionMission(pMissionTemplate.getAuctionMission());
    pMissionHeader.setEquipmentMission(pMissionTemplate.getEquipmentMission());
    pMissionHeader.setVehicle(pMissionTemplate.getVehicleMission());
    pMissionHeader.setInventory(pMissionTemplate.getInventoryMission());
    pMissionHeader.setMissionType(pMissionTemplate.getMissionType());

    // TODO gestion du judiciaire

    if (!SkipActivity) {
      this.CreateActivity(pMissionHeader, pMissionHeader.getActivityCodeToHeader());
    }

    missionHeaderRepository.save(pMissionHeader);

    return pMissionHeader;
  }

  @Override
  @Transactional
  public Boolean CreateMissionFromContact(Partner pContact) {
    return null;
  }

  @Override
  public void GetCustomerNo() {}

  @Override
  public void SetAuctionFromInv(Boolean pTrue) {
    AuctionFromInv = pTrue;
  }

  @Override
  public void SetSkipActivityCreation(Boolean pSkipActivity) {
    SkipActivity = pSkipActivity;
  }

  @Override
  @Transactional
  public void CreateActivity(MissionHeader pMissionHeader, ActivityHeader pActivityCodeToHeader) {
    MissionLine lMissionLine = new MissionLine();
    ActivityManagement activityManagement = Beans.get(ActivityManagement.class);
    if (pActivityCodeToHeader != null) {
      activityManagement.CreateActivityLineFromMission(
          pActivityCodeToHeader, pMissionHeader, lMissionLine, false);
    } else {
      if (pMissionHeader.getActivityCodeToHeader() != null) {
        activityManagement.CreateActivityLineFromMission(
            pMissionHeader.getActivityCodeToHeader(), pMissionHeader, lMissionLine, false);
      }
    }
  }
}
