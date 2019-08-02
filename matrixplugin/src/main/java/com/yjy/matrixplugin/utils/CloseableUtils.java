package com.yjy.matrixplugin.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * <pre>
 *     @author : yjy
 *     @e-mail : yujunyu12@gmail.com
 *     @date   : 2019/07/29
 *     desc   :
 *     github:yjy239@gitub.com
 * </pre>
 */
class CloseableUtils {
    /**
     * 大部分Close关闭流，以及实现Closeable的功能可使用此方法
     *
     * @param c Closeable对象，包括Stream等
     */
    public static void closeQuietly(Closeable c) {
        try {
            if (c != null) {
                c.close();
            }
        } catch (final IOException ioe) {
            // ignore
        }
    }
}
