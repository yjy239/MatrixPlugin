package com.yjy.matrixplugin;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.tencent.matrix.plugin.DefaultPluginListener;
import com.tencent.matrix.report.Issue;
import com.yjy.matrixplugin.binder.ParsedCallBack;
import com.yjy.matrixplugin.component.IssuesListActivity;
import com.yjy.matrixplugin.issue.IssueFilter;
import com.yjy.matrixplugin.issue.IssuesMap;
import com.yjy.matrixplugin.issue.PluginIssue;

import java.lang.ref.SoftReference;

/**
 * <pre>
 *     @author : yjy
 *     @e-mail : yujunyu12@gmail.com
 *     @date   : 2019/07/26
 *     desc   :
 *     github:yjy239@gitub.com
 * </pre>
 */
public class PluginReporterListener extends DefaultPluginListener {
    private static final String TAG = "PluginReporterListener";
    public static final String METHOD = "Trace_EvilMethod";


    public SoftReference<Context> softReference;




    public PluginReporterListener(Context context) {
        super(context);
        softReference = new SoftReference<>(context);
    }

    @Override
    public void onReportIssue(final Issue issue) {
        //多线程调用,需要处理
        super.onReportIssue(issue);


        PluginIssue pluginIssue = PluginIssue.parseIssue(issue);


        if(METHOD.equals(issue.getTag()) ){
            MatrixPluginClient.linkToReportProcess(softReference.get());

            //底层binder是有个栈做消费
            MatrixPluginClient.setOnParsedCompleteListener(new ParsedCallBack() {
                @Override
                public void finish(PluginIssue issue) {
                    IssuesMap.put(IssueFilter.getCurrentFilter(),issue);
                    jumpToIssueActivity();
                }
            });



            MatrixPluginClient.sendUnParsedStack(pluginIssue);

        }else {
            IssuesMap.put(IssueFilter.getCurrentFilter(),pluginIssue);
            jumpToIssueActivity();
        }




    }

    public void jumpToIssueActivity() {
        Context context = softReference.get();
        Intent intent = new Intent(context, IssuesListActivity.class);

        if (context instanceof Application) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
           // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        }

        context.startActivity(intent);
    }
}
