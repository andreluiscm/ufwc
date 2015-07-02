package br.ufc.dc.sd4mp.ufwc;

/**
 * Created by André on 10/06/2015.
 */
public class Review {

    private int userId;
    private int bathroomId;
    private double hygienizationGrade;
    private boolean hasDoor;
    private boolean hasMirror;
    private boolean hasToilet;
    private boolean hasPaper;
    private boolean hasWashbasin;
    private boolean hasWater;
    private boolean hasSoap;
    private boolean hasShower;
    private boolean hasAccessibility;

    public Review() {

    }

    public Review(int userId, int bathroomId, double hygienizationGrade, boolean hasDoor, boolean hasMirror, boolean hasToilet, boolean hasPaper, boolean hasWashbasin, boolean hasWater, boolean hasSoap, boolean hasShower, boolean hasAccessibility) {

        this.userId = userId;
        this.bathroomId = bathroomId;
        this.hygienizationGrade = hygienizationGrade;
        this.hasDoor = hasDoor;
        this.hasMirror = hasMirror;
        this.hasToilet = hasToilet;
        this.hasPaper = hasPaper;
        this.hasWashbasin = hasWashbasin;
        this.hasWater = hasWater;
        this.hasSoap = hasSoap;
        this.hasShower = hasShower;
        this.hasAccessibility = hasAccessibility;

    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getBathroomId() {
        return bathroomId;
    }

    public void setBathroomId(int bathroomId) {
        this.bathroomId = bathroomId;
    }

    public double getHygienizationGrade() {
        return hygienizationGrade;
    }

    public void setHygienizationGrade(double hygienizationGrade) {
        this.hygienizationGrade = hygienizationGrade;
    }

    public boolean isHasDoor() {
        return hasDoor;
    }

    public void setHasDoor(boolean hasDoor) {
        this.hasDoor = hasDoor;
    }

    public boolean isHasMirror() {
        return hasMirror;
    }

    public void setHasMirror(boolean hasMirror) {
        this.hasMirror = hasMirror;
    }

    public boolean isHasToilet() {
        return hasToilet;
    }

    public void setHasToilet(boolean hasToilet) {
        this.hasToilet = hasToilet;
    }

    public boolean isHasPaper() {
        return hasPaper;
    }

    public void setHasPaper(boolean hasPaper) {
        this.hasPaper = hasPaper;
    }

    public boolean isHasWashbasin() {
        return hasWashbasin;
    }

    public void setHasWashbasin(boolean hasWashbasin) {
        this.hasWashbasin = hasWashbasin;
    }

    public boolean isHasWater() {
        return hasWater;
    }

    public void setHasWater(boolean hasWater) {
        this.hasWater = hasWater;
    }

    public boolean isHasSoap() {
        return hasSoap;
    }

    public void setHasSoap(boolean hasSoap) {
        this.hasSoap = hasSoap;
    }

    public boolean isHasShower() {
        return hasShower;
    }

    public void setHasShower(boolean hasShower) {
        this.hasShower = hasShower;
    }

    public boolean isHasAccessibility() {
        return hasAccessibility;
    }

    public void setHasAccessibility(boolean hasAccessibility) {
        this.hasAccessibility = hasAccessibility;
    }

    public String toString() {

        return userId + ", " + bathroomId + ", " + hygienizationGrade + ", " + hasDoor + ", " + hasMirror + ", " +
                hasToilet + ", " + hasPaper + ", " + hasWashbasin + ", " + hasWater + ", " + hasSoap + ", " +
                hasShower + ", " + hasAccessibility;

    }

}
