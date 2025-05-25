package com.example.farmassist;

import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.farmassist.models.GovernmentScheme;
import com.example.farmassist.services.SchemeService;

import java.util.List;

public class SchemesListActivity extends AppCompatActivity {

    private List<GovernmentScheme> schemes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schemes_list);

        ListView listView = findViewById(R.id.listViewSchemes);
        schemes = SchemeService.getSchemes();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                getSchemeNames(schemes)
        );
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            GovernmentScheme scheme = schemes.get(position);
            Intent intent = new Intent(SchemesListActivity.this, SchemeDetailActivity.class);
            intent.putExtra("name", scheme.getName());
            intent.putExtra("description", scheme.getDescription());
            intent.putExtra("eligibility", scheme.getEligibility());
            intent.putExtra("applyUrl", scheme.getApplyUrl());
            startActivity(intent);
        });
    }

    private String[] getSchemeNames(List<GovernmentScheme> schemes) {
        String[] names = new String[schemes.size()];
        for (int i = 0; i < schemes.size(); i++) {
            names[i] = schemes.get(i).getName();
        }
        return names;
    }
}
