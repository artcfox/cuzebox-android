package org.uzebox.cuzebox.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;


/**
 * Extracts assets stored inside the .apk to an external storage directory if they don't already exist there
 */

public class ExtractAssets extends Activity {
    private void LaunchNextActivity() {
        Intent intent = new Intent(this, LaunchApp.class);
        startActivity(intent);
        finish();
    }

    public enum EA_ERRORS {
        NO_ERR, ERR_UNABLE_TO_CREATE_DIR, ERR_IS_NOT_DIRECTORY,
        ERR_UNABLE_TO_GET_ASSET_LIST, ERR_EXTRACTING_FILE
    };

    private final static int BUFFER_SIZE = 1024;

    private void copyAssetFile(InputStream in, OutputStream out) throws IOException {
            byte[] buffer = new byte[BUFFER_SIZE];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EA_ERRORS err = EA_ERRORS.NO_ERR;
        String errFile = "";

        File f = new File(Environment.getExternalStorageDirectory(), getString(R.string.folder_cuzebox));
        if (!f.exists()) {
            if (!f.mkdirs()) {
                err = EA_ERRORS.ERR_UNABLE_TO_CREATE_DIR;
            }
        } else if  (!f.isDirectory()) {
            err = EA_ERRORS.ERR_IS_NOT_DIRECTORY;
        }

        if (err == EA_ERRORS.NO_ERR) {
            AssetManager assetFiles = getAssets();
            String[] files = null;
            try {
                files = assetFiles.list("");
            } catch (IOException e) {
                err = EA_ERRORS.ERR_UNABLE_TO_GET_ASSET_LIST;
            }

            if (files != null) for (String file : files) {

                if (file.equalsIgnoreCase("images") ||
                        file.equalsIgnoreCase("sounds") ||
                        file.equalsIgnoreCase("webkit")) {
                    // For some reason Android forces these directories to appear on the
                    // list as files, even though they don't exist in the assets directory.
                    continue;
                }

                errFile = file;
                InputStream in = null;
                OutputStream out = null;

                try {
                    in = assetFiles.open(file);

                    File outFile = new File(f, file);
                    if (!outFile.exists()) {
                        out = new FileOutputStream(outFile);
                        copyAssetFile(in, out);
                    }
                } catch (IOException e) {
                    err = EA_ERRORS.ERR_EXTRACTING_FILE;
                    break;
                } finally {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                            // NOOP
                        }
                    }
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException e) {
                            // NOOP
                        }
                    }
                }
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
                case ERR_UNABLE_TO_GET_ASSET_LIST:
                    builder.setTitle(R.string.extract_title)
                           .setMessage(R.string.extract_no_asset_list);
                    break;
                case ERR_EXTRACTING_FILE:
                    builder.setTitle(R.string.extract_title)
                           .setMessage(String.format(Locale.ENGLISH, getString(R.string.extract_error_extracting), errFile));
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
