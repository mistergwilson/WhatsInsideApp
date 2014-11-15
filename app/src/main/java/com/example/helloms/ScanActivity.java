package com.example.helloms;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.SurfaceView;

import com.moodstocks.android.AutoScannerSession;
import com.moodstocks.android.MoodstocksError;
import com.moodstocks.android.Result;
import com.moodstocks.android.Scanner;

public class ScanActivity extends Activity implements AutoScannerSession.Listener {

  private static final int TYPES = Result.Type.IMAGE | Result.Type.QRCODE | Result.Type.EAN13;

  private AutoScannerSession session = null;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_scan);

    SurfaceView preview = (SurfaceView)findViewById(R.id.preview);

    try {
      session = new AutoScannerSession(this, Scanner.get(), this, preview);
      session.setResultTypes(TYPES);
    } catch (MoodstocksError e) {
      e.printStackTrace();
    }
  }
  @Override
  protected void onResume() {
    super.onResume();
    session.start();
  }

  @Override
  protected void onPause() {
    super.onPause();
    session.stop();
  }

  @Override
  public void onCameraOpenFailed(Exception e) {
    // You should inform the user if this occurs!
  }

  @Override
  public void onWarning(String debugMessage) {
    // Useful for debugging!
  }

  @Override
  public void onResult(Result result) {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setCancelable(false);
    builder.setNeutralButton("OK", new OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        session.resume();
      }
    });
    builder.setTitle(result.getType() == Result.Type.IMAGE ? "Image:" : "Barcode:");
    builder.setMessage(result.getValue());
    builder.show();
    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=yNYJG3WFPak"));
    startActivity(browserIntent);
  }

}
