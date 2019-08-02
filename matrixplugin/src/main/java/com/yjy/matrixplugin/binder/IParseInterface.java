package com.yjy.matrixplugin.binder;

import android.os.IInterface;
import android.os.RemoteException;

import com.tencent.matrix.report.Issue;
import com.yjy.matrixplugin.issue.PluginIssue;
import com.yjy.matrixplugin.matrixserver.data.EvilMethod;
import com.yjy.matrixplugin.matrixserver.data.ParsedIssueContent;

/**
 * <pre>
 *     @author : yjy
 *     @e-mail : yujunyu12@gmail.com
 *     @date   : 2019/07/31
 *     desc   :
 *     github:yjy239@gitub.com
 * </pre>
 */
public interface IParseInterface extends IInterface {

    void setMethodMappingPath(String path) throws RemoteException;

    void sendToParsedContent(PluginIssue issue) throws RemoteException;
}
