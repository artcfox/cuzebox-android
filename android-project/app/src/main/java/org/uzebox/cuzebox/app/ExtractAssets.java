package org.uzebox.cuzebox.app;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * Extracts assets stored inside the .apk to an external storage directory if they don't already exist there
 */

public class ExtractAssets extends IntentService {
    public static final String PENDING_RESULT_EXTRA = "pending_result";
    public static final String EA_ERRORS_EXTRA = "ea_errors";
    public static final String EA_FILENAME_EXTRA = "ea_filename";
    public static final int RESULT_CODE = 0;

    public ExtractAssets() {
        super("ExtractAssets");
    }

    enum EA_ERRORS {
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
    protected void onHandleIntent(Intent workIntent) {
        // Gets data from the incoming Intent
        String dataString = workIntent.getDataString();

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

        // Return the contents of 'err' in the calling Activity's onActivityResult
        // with any extra information stored in Extras.
        PendingIntent reply = workIntent.getParcelableExtra(PENDING_RESULT_EXTRA);
        try {
            Intent result = new Intent();
            result.putExtra(EA_ERRORS_EXTRA, err.ordinal());
            // Add any extra information about the error
            switch (err) {
                case ERR_UNABLE_TO_CREATE_DIR:
                case ERR_IS_NOT_DIRECTORY:
                    result.putExtra(EA_FILENAME_EXTRA, f.getAbsolutePath());
                    break;
                case ERR_EXTRACTING_FILE:
                    result.putExtra(EA_FILENAME_EXTRA, errFile);
                    break;
            }
            reply.send(this, RESULT_CODE, result);
        } catch (PendingIntent.CanceledException e) {
            // NOOP
        }

    }
}
