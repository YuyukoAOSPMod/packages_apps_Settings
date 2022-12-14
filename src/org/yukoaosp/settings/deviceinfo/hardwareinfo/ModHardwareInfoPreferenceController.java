package org.yukoaosp.settings.deviceinfo.hardwareinfo;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.os.UserHandle;

import androidx.preference.PreferenceScreen;

import com.android.settings.R;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.deviceinfo.HardwareInfoPreferenceController;

import org.yukoaosp.settings.preference.ModHardwareInfoPreference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import exthmuix.core.util.ExthmDeviceUtils;
import exthmuix.core.util.ExthmFormatter;

public class ModHardwareInfoPreferenceController extends BasePreferenceController {

    private final String KEY_HARDWARE_INFO = "mod_hardware_info";

    public ModHardwareInfoPreferenceController(Context context, String preferenceKey) {
        super(context, preferenceKey);
    }

    @Override
    public int getAvailabilityStatus() {
        return AVAILABLE;
    }

    @Override
    public void displayPreference(PreferenceScreen screen) {
        super.displayPreference(screen);
        List<ModHardwareInfoPreference.HardwareInfo> hardwareInfoList = new ArrayList<>();
        ModHardwareInfoPreference preference = screen.findPreference(KEY_HARDWARE_INFO);
        if (preference != null) {
            ModHardwareInfoPreference.HardwareInfo[] hardwareInfos = {
                    new ModHardwareInfoPreference.HardwareInfo(R.drawable.mod_settings_ic_hardware_info_phone,
                            mContext.getString(R.string.model_info), HardwareInfoPreferenceController.getDeviceModel()),
                    new ModHardwareInfoPreference.HardwareInfo(R.drawable.mod_settings_ic_hardware_info_storage,
                            mContext.getString(R.string.mod_settings_my_device_storage_title), mContext.getString(R.string.mod_settings_my_device_storage_regex, getStorageInfo()[0], getStorageInfo()[1])),
                    new ModHardwareInfoPreference.HardwareInfo(R.drawable.mod_settings_ic_hardware_info_processor,
                            mContext.getString(R.string.mod_settings_my_device_processor_title), getProcessorInfo()),
                    new ModHardwareInfoPreference.HardwareInfo(R.drawable.mod_settings_ic_hardware_info_memory,
                            mContext.getString(R.string.mod_settings_my_device_ram_title), getRamInfo())
            };
            hardwareInfoList.addAll(Arrays.asList(hardwareInfos));
        }
        preference.setData(hardwareInfoList);
    }

    private String getProcessorInfo() {
        String socManufacture = ExthmDeviceUtils.getSocManufacture();
        String socModel = ExthmDeviceUtils.getSocModel();
        if (socManufacture == null && socModel == null) {
            return mContext.getString(R.string.unknown);
        }
        switch (socManufacture.toLowerCase(Locale.ROOT)) {
            case "qualcomm":
                socManufacture = mContext.getString(R.string.soc_model_qualcomm);
                break;
            case "mediatek":
                socManufacture = mContext.getString(R.string.soc_model_mediatek);
                break;
            default:
                if (socModel.startsWith("MT")) {
                    socManufacture = mContext.getString(R.string.soc_model_mediatek);
                } else {
                    socManufacture = ExthmDeviceUtils.getSocManufacture();
                }
        }
        return socManufacture + " " + socModel;
    }

    private String getRamInfo() {
        return ExthmFormatter.formatRamSize(mContext, ExthmDeviceUtils.getTotalRamKiloBytesSize());
    }

    /**
     * To get internal storage info
     * @return A 2-length string array, the first value is used storage space,,
     * the second one is total storage size
     */
    private String[] getStorageInfo() {
        String externalStorageState = Environment.getExternalStorageState();
        String[] romInfo = new String[2];
        if (externalStorageState.equals("mounted")) {
            try {
                StatFs statFs = new StatFs(new Environment.UserEnvironment(UserHandle.myUserId()).getExternalStorageDirectory().getPath());
                long totalSize = (long) Math.pow(2, ((int) (Math.log((double) statFs.getTotalBytes()) / Math.log(2))) + 1);
                long usedSize = statFs.getTotalBytes() - statFs.getAvailableBytes();
                romInfo[0] = ExthmFormatter.formatFileSize(mContext, usedSize);
                romInfo[1] = ExthmFormatter.formatFileSize(mContext, totalSize);
            } catch (IllegalArgumentException ignored) {
            }
        } else {
            romInfo[0] = "Sdcard unavailable";
            romInfo[1] = "";
        }
        return romInfo;
    }

}