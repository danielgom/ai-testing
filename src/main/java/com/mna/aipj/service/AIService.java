package com.mna.aipj.service;

import com.mna.aipj.dto.TriggerUpdateResponse;

public interface AIService {

    void readExcel();

    TriggerUpdateResponse triggerUpdate(String queryID);
}
