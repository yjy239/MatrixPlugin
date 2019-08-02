package com.yjy.matrixplugin.issue;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.tencent.matrix.plugin.Plugin;
import com.tencent.matrix.report.Issue;

/**
 * <pre>
 *     @author : yjy
 *     @e-mail : yujunyu12@gmail.com
 *     @date   : 2019/07/26
 *     desc   :
 *     github:yjy239@gitub.com
 * </pre>
 */
public class PluginIssue implements Parcelable {
    private Integer    type;
    private String     tag;
    private String     key;
    private String content;
    private long time;

    public static final String ISSUE_REPORT_TYPE    = "type";
    public static final String ISSUE_REPORT_TAG     = "tag";
    public static final String ISSUE_REPORT_PROCESS = "process";
    public static final String ISSUE_REPORT_TIME = "time";

    public PluginIssue() {
    }

    public PluginIssue(int type) {
        this.type = type;
    }

    public PluginIssue(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    @Override
    public String toString() {
        return String.format("tag[%s]type[%d];key[%s];content[%s]", tag, type, key, content);
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Integer getType() {
        return type;
    }

    public String getKey() {
        return key;
    }

    public String getTag() {
        return tag;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public static PluginIssue parseIssue(Issue issue){
        PluginIssue pluginIssue = new PluginIssue();
        pluginIssue.key = issue.getKey();
        pluginIssue.tag = issue.getTag();
        pluginIssue.type = issue.getType();
        pluginIssue.time = Long.parseLong(issue.getContent().optString("time"));
        Gson gson = new Gson();
        //内容全部解析成string
        pluginIssue.content = issue.getContent().toString();
        return pluginIssue;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.type);
        dest.writeString(this.tag);
        dest.writeString(this.key);
        dest.writeString(this.content);
        dest.writeLong(this.time);
    }

    protected PluginIssue(Parcel in) {
        this.type = (Integer) in.readValue(Integer.class.getClassLoader());
        this.tag = in.readString();
        this.key = in.readString();
        this.content = in.readString();
        this.time = in.readLong();
    }

    public static final Parcelable.Creator<PluginIssue> CREATOR = new Parcelable.Creator<PluginIssue>() {
        @Override
        public PluginIssue createFromParcel(Parcel source) {
            return new PluginIssue(source);
        }

        @Override
        public PluginIssue[] newArray(int size) {
            return new PluginIssue[size];
        }
    };
}
