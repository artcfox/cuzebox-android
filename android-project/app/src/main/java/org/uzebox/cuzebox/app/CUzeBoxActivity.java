package org.uzebox.cuzebox.app;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import java.util.Locale;

/**
 * Handles requesting Runtime Permissions when running on Marshmallow or newer
 */

public class CUzeBoxActivity extends Activity {
    private static final int EXTRACT_ASSETS_REQUEST_CODE = 0;

    private void LaunchSDLActivity() {
        Intent intent = new Intent(this, LaunchApp.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == EXTRACT_ASSETS_REQUEST_CODE) && (resultCode == ExtractAssets.RESULT_CODE)) {
            ExtractAssets.EA_ERRORS err = ExtractAssets.EA_ERRORS.values()[data.getIntExtra(ExtractAssets.EA_ERRORS_EXTRA, 0)];
            if (err == ExtractAssets.EA_ERRORS.NO_ERR) {
                LaunchSDLActivity();
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
                                .setMessage(String.format(Locale.ENGLISH, getString(R.string.extract_unable_to_create_dir), data.getStringExtra(ExtractAssets.EA_FILENAME_EXTRA)));
                        break;
                    case ERR_IS_NOT_DIRECTORY:
                        builder.setTitle(R.string.extract_title)
                                .setMessage(String.format(Locale.ENGLISH, getString(R.string.extract_is_not_directory), data.getStringExtra(ExtractAssets.EA_FILENAME_EXTRA)));
                        break;
                    case ERR_UNABLE_TO_GET_ASSET_LIST:
                        builder.setTitle(R.string.extract_title)
                                .setMessage(R.string.extract_no_asset_list);
                        break;
                    case ERR_EXTRACTING_FILE:
                        builder.setTitle(R.string.extract_title)
                                .setMessage(String.format(Locale.ENGLISH, getString(R.string.extract_error_extracting), data.getStringExtra(ExtractAssets.EA_FILENAME_EXTRA)));
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

    private void AsyncAssetExtraction() {
        PendingIntent pendingResult = createPendingResult(EXTRACT_ASSETS_REQUEST_CODE, new Intent(), 0);
        Intent intent = new Intent(getApplicationContext(), ExtractAssets.class);
        intent.putExtra(ExtractAssets.PENDING_RESULT_EXTRA, pendingResult);
        startService(intent);
    }

    public static final int REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION = 1337;

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            AsyncAssetExtraction();
        } else {
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(this);
            }
            builder.setTitle(R.string.no_permission_title)
                   .setMessage(R.string.no_permission_message)
                   .setCancelable(false)
                   .setPositiveButton(R.string.no_permission_button_text, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                   })
                   .show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                this.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION);
            } else {
                AsyncAssetExtraction();
            }
        } else {
            AsyncAssetExtraction();
        }
    }

}
