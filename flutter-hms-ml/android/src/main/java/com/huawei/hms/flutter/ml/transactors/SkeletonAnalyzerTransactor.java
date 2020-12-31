/*
    Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.

    Licensed under the Apache License, Version 2.0 (the "License")
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        https://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/

package com.huawei.hms.flutter.ml.transactors;

import android.app.Activity;
import android.util.SparseArray;

import com.huawei.hms.flutter.ml.logger.HMSLogger;
import com.huawei.hms.flutter.ml.utils.EventHandler;
import com.huawei.hms.flutter.ml.utils.tojson.SkeletonToJson;
import com.huawei.hms.mlsdk.common.MLAnalyzer;
import com.huawei.hms.mlsdk.skeleton.MLSkeleton;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SkeletonAnalyzerTransactor implements MLAnalyzer.MLTransactor<MLSkeleton> {

    private Activity activity;

    public SkeletonAnalyzerTransactor(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void transactResult(MLAnalyzer.Result<MLSkeleton> result) {
        Map<String, Object> m = new HashMap<>();

        SparseArray<MLSkeleton> faceSparseArray = result.getAnalyseList();
        MLSkeleton skeleton = faceSparseArray.get(0);

        m.put("result", SkeletonToJson.skeletonJSON(skeleton));
        m.put("isAnalyzerAvailable", result.isAnalyzerAvaliable());
        sendEvent(new JSONObject(m).toString());
    }

    private void sendEvent(final String event) {
        HMSLogger.getInstance(activity.getApplicationContext()).sendPeriodicEvent("liveSkeletonAnalyze");
        EventHandler.getInstance().getUiHandler().post(() -> EventHandler.getInstance().getEventSink().success(event));
    }

    @Override
    public void destroy() {

    }
}
