/*
 * Copyright (C) 2022 Acme
 * Copyright (C) 2022 YuyukoAOSPMod
 *
 * PDX-License-Identifier: Apache-2.0
 *
 */

package com.android.settings.deviceinfo.firmwareversion;

import android.content.Context;
import android.os.SystemProperties;

import com.android.settings.R;
import com.android.settings.core.BasePreferenceController;

public class ModVersionPreferenceController extends BasePreferenceController {

    private static final String TAG = "ModVersionPreferenceController";

    private static final String KEY_MOD_BRANCH_PROP = "ro.yuyuko.aospmod.branch";

    private static final String KEY_MOD_BUILD_TYPE_PROP = "ro.yuyuko.aospmod.build.type";

    public ModVersionPreferenceController(Context context, String key) {
        super(context, key);
    }

    @Override
    public int getAvailabilityStatus() {
        return AVAILABLE;
    }

    @Override
    public CharSequence getSummary() {
        return SystemProperties.get(KEY_MOD_BRANCH_PROP, mContext.getString(R.string.unknown)) + " | " + SystemProperties.get(KEY_MOD_BUILD_TYPE_PROP, mContext.getString(R.string.unknown)).toUpperCase();
    }
}
