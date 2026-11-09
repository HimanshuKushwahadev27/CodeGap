package com.emi.codegap.adapter;

import java.util.List;

public interface PlatformAdapter {
    List<PlatformSubmission> fetchSubmissions(String handle);
}
