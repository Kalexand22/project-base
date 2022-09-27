package com.axelor.apps.openauctionbase.service;

import com.axelor.apps.base.db.Partner;
import com.axelor.apps.openauction.db.ActivityHeader;
import com.axelor.apps.openauction.db.MissionHeader;
import com.axelor.apps.openauction.db.MissionTemplate;

public interface MissionTemplateManagement {
  public MissionHeader createMissionFromMission(
      MissionHeader pMissionHeader,
      MissionTemplate pMissionTemplate,
      Boolean pJudicialFilter,
      String pLawyerBusNo);

  public Boolean createMissionFromContact(Partner pContact);

  public void getCustomerNo();

  public void setAuctionFromInv(Boolean pTrue);

  public void setSkipActivityCreation(Boolean pSkipActivity);

  public void createActivity(MissionHeader pMissionHeader, ActivityHeader pActivityCodeToHeader);
}
