package vn.edu.vhu.ltdd.a2stopwatch;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "A2_231A011078";

    private TextView tvStatus, tvTime, tvRecreate;
    private Button btnStart, btnReset;

    private boolean isRunning = false;
    private long seconds = 0;
    private int recreateCount = 0;

    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (isRunning) {
                seconds++;
                updateUI();
                handler.postDelayed(this, 1000);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate called");

        if (savedInstanceState != null) {
            seconds = savedInstanceState.getLong("seconds", 0);
            isRunning = savedInstanceState.getBoolean("isRunning", false);
            recreateCount = savedInstanceState.getInt("recreateCount", 0);
        }

        tvStatus = findViewById(R.id.tvStatus);
        tvTime = findViewById(R.id.tvTime);
        tvRecreate = findViewById(R.id.tvRecreate);
        btnStart = findViewById(R.id.btnStart);
        btnReset = findViewById(R.id.btnReset);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isRunning) {
                    isRunning = true;
                    btnStart.setText(R.string.pause);
                    tvStatus.setText(R.string.status_running);
                    handler.post(runnable);
                } else {
                    isRunning = false;
                    btnStart.setText(R.string.start);
                    tvStatus.setText(R.string.status_paused);
                    handler.removeCallbacks(runnable);
                }
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRunning = false;
                handler.removeCallbacks(runnable);
                seconds = 0;
                btnStart.setText(R.string.start);
                tvStatus.setText(R.string.status_paused);
                updateUI();
            }
        });

        updateUI();
    }

    private void updateUI() {
        long secs = seconds % 60;
        long mins = seconds / 60;
        tvTime.setText(String.format(Locale.getDefault(), "%02d:%02d.0", mins, secs));
        tvRecreate.setText("Số lần Recreate: " + recreateCount);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("seconds", seconds);
        outState.putBoolean("isRunning", isRunning);
        outState.putInt("recreateCount", recreateCount + 1);
        Log.d(TAG, "onSaveInstanceState called");
    }
}