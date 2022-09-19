package com.axelor.apps.openauctionbase.service;

import com.axelor.apps.base.db.Partner;
import com.axelor.apps.openauction.db.ActivityHeader;
import com.axelor.apps.openauction.db.MissionHeader;
import com.axelor.apps.openauction.db.MissionTemplate;

public interface MissionTemplateManagement {
  public MissionHeader CreateMissionFromMission(
      MissionHeader pMissionHeader,
      MissionTemplate pMissionTemplate,
      Boolean pJudicialFilter,
      String pLawyerBusNo);

  public Boolean CreateMissionFromContact(Partner pContact);

  public void GetCustomerNo();

  public void SetAuctionFromInv(Boolean pTrue);

  public void SetSkipActivityCreation(Boolean pSkipActivity);

  public void CreateActivity(MissionHeader pMissionHeader, ActivityHeader pActivityCodeToHeader);
}
