/*
 * Tencent is pleased to support the open source community by making wechat-matrix available.
 * Copyright (C) 2018 THL A29 Limited, a Tencent company. All rights reserved.
 * Licensed under the BSD 3-Clause License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://opensource.org/licenses/BSD-3-Clause
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yjy.matrixplugin.issue;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tencent.matrix.report.Issue;

import org.json.JSONObject;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ParseIssueUtil {

    public static String parseIssue(Issue issue, boolean onlyShowContent) {

        StringBuilder stringBuilder = new StringBuilder();
        if (!onlyShowContent) {
            stringBuilder.append(Issue.ISSUE_REPORT_TAG).append(" : ").append(issue.getTag()).append("\n");
            stringBuilder.append(Issue.ISSUE_REPORT_TYPE).append(" : ").append(issue.getType()).append("\n");
            stringBuilder.append("key").append(" : ").append(issue.getKey()).append("\n");
        }

        stringBuilder.append("content :").append("\n");

        return pauseJsonObj(stringBuilder, issue.getContent()).toString();

    }

    public static String parseIssue(PluginIssue issue, boolean onlyShowContent) {

        StringBuilder stringBuilder = new StringBuilder();
        if (!onlyShowContent) {
            stringBuilder.append(Issue.ISSUE_REPORT_TAG).append(" : ").append(issue.getTag()).append("\n");
            stringBuilder.append(Issue.ISSUE_REPORT_TYPE).append(" : ").append(issue.getType()).append("\n");
            stringBuilder.append("key").append(" : ").append(issue.getKey()).append("\n");
        }

        stringBuilder.append("content :").append("\n");

        return pauseJsonObj(stringBuilder, issue.getContent()).toString();

    }

    public static StringBuilder pauseJsonObj(StringBuilder builder, String content) {
        JsonParser parser = new JsonParser();
        JsonObject object = parser.parse(content).getAsJsonObject();
        Set<Map.Entry<String, JsonElement>> set = object.entrySet();
        Iterator<Map.Entry<String, JsonElement>> iterator =  set.iterator();
        while (iterator.hasNext()) {

            Map.Entry<String, JsonElement> entry = iterator.next();
            String key = entry.getKey();
            String val = entry.getValue().toString();
            builder.append("\t").append(key).append(" : ").append(val).append("\n");
        }

        return builder;
    }


    public static StringBuilder pauseJsonObj(StringBuilder builder, JSONObject object) {
        Iterator<String> iterator = object.keys();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String val = object.optString(key);
            builder.append("\t").append(key).append(" : ").append(val).append("\n");
        }

        return builder;
    }




}
