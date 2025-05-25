package com.example.farmassist.models;

public class GovernmentScheme {
    private String name;
    private String description;
    private String eligibility;
    private String applyUrl;

    public GovernmentScheme(String name, String description, String eligibility, String applyUrl) {
        this.name = name;
        this.description = description;
        this.eligibility = eligibility;
        this.applyUrl = applyUrl;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getEligibility() { return eligibility; }
    public String getApplyUrl() { return applyUrl; }
}
