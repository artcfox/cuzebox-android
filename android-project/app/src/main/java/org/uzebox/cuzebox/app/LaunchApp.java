package org.uzebox.cuzebox.app;

import android.os.Environment;

import org.libsdl.app.SDLActivity;

import java.io.File;

/**
 * A sample wrapper class that just calls SDLActivity
 */

public class LaunchApp extends SDLActivity {
    @Override
    protected String[] getArguments() {
        File bootloader = new File(Environment.getExternalStorageDirectory(), getString(R.string.folder_cuzebox) + "/Bootloader.hex");
        String[] args = { bootloader.toString() };
        return args;
    }
}
