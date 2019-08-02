package com.yjy.matrixplugin.binder;

import com.yjy.matrixplugin.issue.PluginIssue;
import com.yjy.matrixplugin.matrixserver.data.EvilMethod;

/**
 * <pre>
 *     @author : yjy
 *     @e-mail : yujunyu12@gmail.com
 *     @date   : 2019/07/31
 *     desc   :
 *     github:yjy239@gitub.com
 * </pre>
 */
public interface ParsedCallBack {
    /**
     * 解析返回
     * @param issue 异常经过转化后的数据返回
     */
    void finish(PluginIssue issue);
}
