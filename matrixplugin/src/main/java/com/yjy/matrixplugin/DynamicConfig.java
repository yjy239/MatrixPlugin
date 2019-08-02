package com.yjy.matrixplugin;

import com.tencent.matrix.util.MatrixLog;
import com.tencent.mrs.plugin.IDynamicConfig;

/**
 * <pre>
 *     @author : yjy
 *     @e-mail : yujunyu12@gmail.com
 *     @date   : 2019/07/26
 *     desc   :
 *     github:yjy239@gitub.com
 * </pre>
 */
public class DynamicConfig implements IDynamicConfig {
    private static final String TAG = DynamicConfig.class.getName();

    @Override
    public String get(String key, String defStr) {
        return defStr;
    }

    @Override
    public int get(String key, int defInt) {
        //TODO here return default value which is inside sdk, you can change it as you wish. matrix-sdk-key in class MatrixEnum.
        if (MatrixEnum.clicfg_matrix_resource_max_detect_times.name().equals(key)) {
            MatrixLog.i(TAG, "key:" + key + ", before change:" + defInt + ", after change, value:" + 2);
            return 2;//new value
        }

        if (MatrixEnum.clicfg_matrix_trace_fps_report_threshold.name().equals(key)) {
            return 10000;
        }

        if (MatrixEnum.clicfg_matrix_trace_fps_time_slice.name().equals(key)) {
            return 12000;
        }
        return defInt;
    }

    @Override
    public long get(String key, long defLong) {
        //TODO here return default value which is inside sdk, you can change it as you wish. matrix-sdk-key in class MatrixEnum.
        if (MatrixEnum.clicfg_matrix_trace_fps_report_threshold.name().equals(key)) {
            return 10000L;
        }

        if (MatrixEnum.clicfg_matrix_resource_detect_interval_millis.name().equals(key)) {
            MatrixLog.i(TAG, key + ", before change:" + defLong + ", after change, value:" + 2000);
            return 2000;
        }
        return defLong;
    }

    @Override
    public boolean get(String key, boolean defBool) {
        return defBool;
    }

    @Override
    public float get(String key, float defFloat) {
        return defFloat;
    }
}
