package com.yjy.matrixplugin.matrixserver.data;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

/**
 * <pre>
 *     @author : yjy
 *     @e-mail : yujunyu12@gmail.com
 *     @date   : 2019/07/30
 *     desc   :
 *     github:yjy239@gitub.com
 * </pre>
 */
public class ParsedIssueContent  implements Parcelable {
    private boolean isEnd;
    private String mContent;
    private int position;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.isEnd ? (byte) 1 : (byte) 0);
        dest.writeString(this.mContent);
        dest.writeInt(position);
    }

    public ParsedIssueContent(String content,boolean isEnd) {
        this.mContent = content;
        this.isEnd = isEnd;
    }

    protected ParsedIssueContent(Parcel in) {
        this.isEnd = in.readByte() != 0;
        this.mContent = in.readString();
        this.position = in.readInt();
    }

    public static final Creator<ParsedIssueContent> CREATOR = new Creator<ParsedIssueContent>() {
        @Override
        public ParsedIssueContent createFromParcel(Parcel source) {
            return new ParsedIssueContent(source);
        }

        @Override
        public ParsedIssueContent[] newArray(int size) {
            return new ParsedIssueContent[size];
        }
    };
}
