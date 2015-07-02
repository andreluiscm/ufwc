package br.ufc.dc.sd4mp.ufwc;

import java.io.Serializable;

/**
 * Created by André on 10/06/2015.
 */
public class Bathroom implements Serializable {

    private int id;
    private double longitude;
    private double latitude;
    private String description;
    private String gender;
    private BathroomHelper bathroomHelper;

    public Bathroom() {

    }

    public Bathroom(int id, double longitude, double latitude, String description, String gender, BathroomHelper bathroomHelper) {

        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.description = description;
        this.gender = gender;
        this.bathroomHelper = bathroomHelper;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public BathroomHelper getBathroomHelper() {
        return bathroomHelper;
    }

    public void setBathroomHelper(BathroomHelper bathroomHelper) {
        this.bathroomHelper = bathroomHelper;
    }

    public double calculateGrade() {

        double grade;
        double gradeSum = 0d;

        gradeSum += getBathroomHelper().getHygienizationGreatCount() * 10.0;
        gradeSum += getBathroomHelper().getHygienizationGoodCount() * 7.5;
        gradeSum += getBathroomHelper().getHygienizationRegularCount() * 5.0;
        gradeSum += getBathroomHelper().getHygienizationPoorCount() * 2.5;
        gradeSum += getBathroomHelper().getHygienizationUnbearableCount() * 0.0;

        gradeSum += getBathroomHelper().getHasDoorCount() * 10.0;
        gradeSum += getBathroomHelper().getHasMirrorCount() * 10.0;
        gradeSum += getBathroomHelper().getHasToiletCount() * 10.0;
        gradeSum += getBathroomHelper().getHasPaperCount() * 10.0;
        gradeSum += getBathroomHelper().getHasWashbasinCount() * 10.0;
        gradeSum += getBathroomHelper().getHasWaterCount() * 10.0;
        gradeSum += getBathroomHelper().getHasSoapCount() * 10.0;
        gradeSum += getBathroomHelper().getHasShowerCount() * 10.0;
        gradeSum += getBathroomHelper().getHasAccessibilityCount() * 10.0;

        if (gradeSum == 0d)
            grade = 0d;

        else
            grade = gradeSum / ( getBathroomHelper().getReviewCount() * 1.0 );

        return grade;

    }

    public String toString() {

        return id + ", " + longitude + ", " + latitude + ", " + description + ", " + gender;

    }

}
