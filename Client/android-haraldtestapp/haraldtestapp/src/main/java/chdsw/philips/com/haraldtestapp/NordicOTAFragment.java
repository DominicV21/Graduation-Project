package chdsw.philips.com.haraldtestapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import chdsw.philips.com.haraldlib.HBCentral;
import chdsw.philips.com.haraldlib.HBConnectionListener;
import chdsw.philips.com.haraldlib.services.nordicdfu.HBNordicDFUListener;

import static android.app.Activity.RESULT_OK;
import static chdsw.philips.com.haraldtestapp.DeviceActivity.DEVICE_ADDRESS;

public class NordicOTAFragment extends Fragment {
    private static final int ACCESS_READ_REQUEST = 2;
    private static final int FILE_SELECT_CODE = 400;

    private Context context;
    private HBCentral hbCentral;
    private String deviceAddress;
    private File selectedFile;

    private Button chooseFileButton;
    private TextView pathTextView;
    private Button startOTAButton;
    private TextView statusTextView;
    private ProgressBar progressBar;
    private TextView errorCodeTextView;
    private TextView errorMessageTextView;

    private HBNordicDFUListener hbNordicDFUListener = new HBNordicDFUListener() {

        @Override
        public void onOTAProgressChanged(String deviceAddress, float sendFileSize, float totalFileSize) {
            statusTextView.setText(R.string.dfu_status_progress);
            progressBar.setProgress((int)((sendFileSize / totalFileSize) * 100));
            errorCodeTextView.setText("-");
            errorMessageTextView.setText("-");
        }

        @Override
        public void onOTACompleted(String deviceAddress) {
            statusTextView.setText(R.string.dfu_status_completed);
            progressBar.setProgress(100);
            chooseFileButton.setEnabled(true);
            startOTAButton.setEnabled(true);
        }

        @Override
        public void onOTAAborted(String deviceAddress) {
            statusTextView.setText(R.string.dfu_status_aborted);
            chooseFileButton.setEnabled(true);
            startOTAButton.setEnabled(true);
        }

        @Override
        public void onOTAError(String deviceAddress, int errorCode, String message) {
            statusTextView.setText(R.string.dfu_status_error);
            errorCodeTextView.setText(String.valueOf(errorCode));
            errorMessageTextView.setText(message);
            chooseFileButton.setEnabled(true);
            startOTAButton.setEnabled(true);
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.context = getContext();
        this.hbCentral = HBCentralInstance.getInstance(this.context).getHBCentral();
        this.deviceAddress = getArguments().getString(DEVICE_ADDRESS);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_nordic_ota, container, false);

        chooseFileButton = (Button) rootView.findViewById(R.id.chooseFileButton);
        chooseFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFile();
            }
        });

        startOTAButton = (Button) rootView.findViewById(R.id.startOTAButton);
        startOTAButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startOTA();
            }
        });

        pathTextView = (TextView) rootView.findViewById(R.id.pathTextView);
        statusTextView = (TextView) rootView.findViewById(R.id.statusTextView);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        errorCodeTextView = (TextView) rootView.findViewById(R.id.errorCodeTextView);
        errorMessageTextView = (TextView) rootView.findViewById(R.id.errorMessageTextView);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        this.hbCentral.addNordicDFUServiceListener(this.deviceAddress, this.hbNordicDFUListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        this.hbCentral.removeNordicDFUServiceListener(this.deviceAddress);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    try {
                        Uri uri = data.getData();
                        String path = uri.getPath();
                        if(path.startsWith("/file")) {
                            path = path.replaceFirst("/file", "");
                        }
                        selectedFile = new File(path);
                        pathTextView.setText(selectedFile.getName());
                        startOTAButton.setEnabled(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(context, "Selected file invalid.", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == ACCESS_READ_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startOTA();
            }
        }
    }

    private void selectFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/zip");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"), FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(context, "Please install a File Manager.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean hasPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, ACCESS_READ_REQUEST);
                return false;
            }
        }

        return true;
    }

    private void startOTA() {
        if (selectedFile != null) {
            if (hasPermissions()) {
                hbCentral.startNordicDFU(deviceAddress, selectedFile);
                startOTAButton.setEnabled(false);
                chooseFileButton.setEnabled(false);
            }
        } else {
            Toast.makeText(context, "Please select a file first.", Toast.LENGTH_LONG).show();
        }
    }
}
