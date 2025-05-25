package com.example.farmassist.services;

import com.example.farmassist.models.GovernmentScheme;
import java.util.ArrayList;
import java.util.List;

public class SchemeService {
    public static List<GovernmentScheme> getSchemes() {
        List<GovernmentScheme> schemes = new ArrayList<>();
        schemes.add(new GovernmentScheme(
                "PM-KISAN",
                "Income support for farmers.",
                "Landholding farmers",
                "https://pmkisan.gov.in/"
        ));
        schemes.add(new GovernmentScheme(
                "PMFBY",
                "Crop insurance for farmers.",
                "All farmers",
                "https://pmfby.gov.in/"
        ));
        // Add more schemes as needed or fetch from an API
        return schemes;
    }
}
