/*
 * Copyright (C) 2011 The CyanogenMod Project
 * This code has been modified. Portions copyright (C) 2012 ParanoidAndroid Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.systemui.statusbar.policy;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.UserHandle;
import android.provider.Settings;
import android.widget.CompoundButton;

import com.android.internal.util.cm.TorchConstants;
import com.android.systemui.R;

public class FlashlightController implements CompoundButton.OnCheckedChangeListener {
    private static final String TAG = "StatusBar.FlashlightController";

    private static final IntentFilter STATE_FILTER =
            new IntentFilter(TorchConstants.ACTION_STATE_CHANGED);
    private boolean mActive = false;
    private Context mContext;
    private CompoundButton mCheckBox;

    public FlashlightController(Context context, CompoundButton checkbox) {
        mContext = context;
        mCheckBox = checkbox;
        checkbox.setChecked(mActive);
        checkbox.setOnCheckedChangeListener(this);
    }

    public void onCheckedChanged(CompoundButton view, boolean checked) {
        boolean bright = Settings.System.getIntForUser(mContext.getContentResolver(),
                Settings.System.EXPANDED_FLASH_MODE, 0, UserHandle.USER_CURRENT) == 1;
        Intent i = new Intent(TorchConstants.ACTION_TOGGLE_STATE);
        i.putExtra(TorchConstants.EXTRA_BRIGHT_MODE, bright);
        mContext.sendBroadcast(i);
    }

    protected IntentFilter getBroadcastIntentFilter() {
        return STATE_FILTER;
    }

    protected void onReceive(Context context, Intent intent) {
        mActive = intent.getIntExtra(TorchConstants.EXTRA_CURRENT_STATE, 0) != 0;
    }
}
