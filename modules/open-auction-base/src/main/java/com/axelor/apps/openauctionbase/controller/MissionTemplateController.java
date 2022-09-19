package com.axelor.apps.openauctionbase.controller;

import com.axelor.apps.base.db.TradingName;
import com.axelor.apps.base.db.repo.TradingNameRepository;
import com.axelor.apps.openauction.db.ActivityHeader;
import com.axelor.apps.openauction.db.MissionHeader;
import com.axelor.apps.openauction.db.MissionTemplate;
import com.axelor.apps.openauction.db.repo.ActivityHeaderRepository;
import com.axelor.apps.openauction.db.repo.MissionTemplateRepository;
import com.axelor.apps.openauctionbase.service.MissionTemplateManagement;
import com.axelor.inject.Beans;
import com.axelor.meta.schema.actions.ActionView;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.axelor.rpc.Context;
import java.util.HashMap;

public class MissionTemplateController {
  public void createMissionFromTemplate(ActionRequest request, ActionResponse response) {
    Context context = request.getContext();
    HashMap<String, Object> missionTemplateMap =
        (HashMap<String, Object>) context.get("missionTemplate");
    MissionTemplate missionTemplate =
        Beans.get(MissionTemplateRepository.class)
            .find(Long.parseLong(missionTemplateMap.get("id").toString()));

    MissionHeader pMissionHeader = new MissionHeader();
    pMissionHeader.setMissionTemplateCode(missionTemplate);
    pMissionHeader.setDescription((String) context.get("missionTitle"));

    HashMap<String, Object> activityCodeToHeaderMap =
        (HashMap<String, Object>) context.get("activityCodeToHeader");
    ActivityHeader activityCodeToHeader =
        Beans.get(ActivityHeaderRepository.class)
            .find(Long.parseLong(activityCodeToHeaderMap.get("id").toString()));
    pMissionHeader.setActivityCodeToHeader(activityCodeToHeader);

    HashMap<String, Object> activityCodeToLineMap =
        (HashMap<String, Object>) context.get("activityCodeToLine");
    ActivityHeader activityCodeToLine =
        Beans.get(ActivityHeaderRepository.class)
            .find(Long.parseLong(activityCodeToHeaderMap.get("id").toString()));
    pMissionHeader.setActivityCodeToLines(activityCodeToHeader);

    HashMap<String, Object> responsibilityCenterMap =
        (HashMap<String, Object>) context.get("responsibilityCenter");
    TradingName responsibilityCenter =
        Beans.get(TradingNameRepository.class)
            .find(Long.parseLong(responsibilityCenterMap.get("id").toString()));
    pMissionHeader.setResponsibilityCenter(responsibilityCenter);

    // TODO gestion des Partner

    MissionTemplateManagement missionTemplateManagement =
        Beans.get(MissionTemplateManagement.class);
    pMissionHeader =
        missionTemplateManagement.CreateMissionFromMission(
            pMissionHeader, missionTemplate, false, "");

    if (pMissionHeader != null) {
      // Open the generated mission in a new tab
      response.setView(
          ActionView.define("Mission")
              .model(MissionHeader.class.getName())
              .add("grid", "mission-header-grid")
              .add("form", "mission-header-form")
              .param("forceEdit", "true")
              .context("_showRecord", String.valueOf(pMissionHeader.getId()))
              .map());
      response.setCanClose(true);
    }
  }
}
