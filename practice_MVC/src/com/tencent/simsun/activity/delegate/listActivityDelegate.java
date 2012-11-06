package com.tencent.simsun.activity.delegate;

import java.util.List;
import java.util.Map;

public interface listActivityDelegate {
    void onDateReceived(List<Map<String, String>> items);
}
