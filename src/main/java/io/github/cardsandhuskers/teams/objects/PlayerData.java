package io.github.cardsandhuskers.teams.objects;

public class PlayerData {
    private int tempPoints;
    private String name;

    public PlayerData(String name) {
        tempPoints = 0;
        this.name = name;
    }

    public void setTempPoints(int tempPoints) {
        this.tempPoints = tempPoints;
    }
    public void resetTempPoints() {
        tempPoints = 0;
    }
    public String getName() {
        return name;
    }
    public int getTempPoints() {
        return tempPoints;
    }
}
