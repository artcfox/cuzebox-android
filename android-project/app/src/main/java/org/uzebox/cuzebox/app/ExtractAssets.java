package org.uzebox.cuzebox.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

import java.io.File;
import java.util.Locale;


/**
 * Will eventually extract assets from inside the .apk to the external storage directory
 */

public class ExtractAssets extends Activity {
    private void LaunchNextActivity() {
        Intent intent = new Intent(this, LaunchApp.class);
        startActivity(intent);
        finish();
    }

    public enum EA_ERRORS {
        NO_ERR, ERR_UNABLE_TO_CREATE_DIR, ERR_IS_NOT_DIRECTORY, ERR_MISSING_BOOTLOADER
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EA_ERRORS err = EA_ERRORS.NO_ERR;
        String folder_cuzebox = "cuzebox";

        File f = new File(Environment.getExternalStorageDirectory(), folder_cuzebox);
        if (!f.exists()) {
            if (!f.mkdirs()) {
                err = EA_ERRORS.ERR_UNABLE_TO_CREATE_DIR;
            }
        } else if  (!f.isDirectory()) {
            err = EA_ERRORS.ERR_IS_NOT_DIRECTORY;
        }

        File b = new File("");
        if (err == EA_ERRORS.NO_ERR) {
            b = new File(f, "Bootloader_0_4_5.hex");
            if (!b.exists()) {
                // TODO: Extract it from the assets directory
                err = EA_ERRORS.ERR_MISSING_BOOTLOADER;
            }
        }

        if (err == EA_ERRORS.NO_ERR) {
            LaunchNextActivity();
        } else {
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(this);
            }
            switch (err) {
                case ERR_UNABLE_TO_CREATE_DIR:
                    builder.setTitle(R.string.extract_title)
                           .setMessage(String.format(Locale.ENGLISH, getString(R.string.extract_unable_to_create_dir), f.getAbsolutePath()));
                    break;
                case ERR_IS_NOT_DIRECTORY:
                    builder.setTitle(R.string.extract_title)
                           .setMessage(String.format(Locale.ENGLISH, getString(R.string.extract_is_not_directory), f.getAbsolutePath()));
                    break;
                case ERR_MISSING_BOOTLOADER:
                    builder.setTitle(R.string.extract_title)
                           .setMessage(String.format(Locale.ENGLISH, getString(R.string.extract_missing_bootloader), b.getAbsolutePath()));
                    break;
            }
            builder.setCancelable(false)
                   .setPositiveButton(R.string.extract_button_text, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                   })
                   .show();
        }
    }
}
