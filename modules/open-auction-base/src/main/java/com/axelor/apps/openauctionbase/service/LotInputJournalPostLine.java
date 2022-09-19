package com.axelor.apps.openauctionbase.service;

import com.axelor.apps.openauction.db.LotInputJournal;

public interface LotInputJournalPostLine {
  public LotInputJournal runWithCheck(LotInputJournal pLotInputJournal);
  // PROCEDURE RunWithoutCheck@1000000000(VAR pLotInputJournal@1000000000 : Record 8011498);
  public LotInputJournal runWithoutCheck(LotInputJournal pLotInputJournal);
}
