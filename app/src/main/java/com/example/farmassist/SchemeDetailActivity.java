package com.example.farmassist;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SchemeDetailActivity extends AppCompatActivity {

    private String applyUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheme_detail);

        TextView textName = findViewById(R.id.textSchemeName);
        TextView textDescription = findViewById(R.id.textSchemeDescription);
        TextView textEligibility = findViewById(R.id.textSchemeEligibility);
        Button buttonApply = findViewById(R.id.buttonApply);

        Intent intent = getIntent();
        textName.setText(intent.getStringExtra("name"));
        textDescription.setText(intent.getStringExtra("description"));
        textEligibility.setText(getString(R.string.scheme_eligibility, intent.getStringExtra("eligibility")));
        applyUrl = intent.getStringExtra("applyUrl");

        buttonApply.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(applyUrl));
            startActivity(browserIntent);
        });
    }
}
