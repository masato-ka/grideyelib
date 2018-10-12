package ka.masato.irarraysensorlib;

import android.graphics.Color;

class SensingData {

    private double red = 0;
    private double blue = 0;
    private double green = 0;

    private int indexX = 0;
    private int indexY = 0;
    private double value = 0;

    private int gain = 10;
    private double offsetX = 0.2;
    private double offsetGreen = 0.6;

    public SensingData(int indexX, int indexY, double value) {
        this.indexX = indexX;
        this.indexY = indexY;
        this.value =value;
    }

    private double sigmoid(double x){
        return sigmoid(x, 1.0, 0.0);
    }

    //1, 0
    private double sigmoid(double x, double gain, double offsetX) {
        return (double) ((Math.tanh(((x + offsetX) * gain) / 2.0) + 1.0) / 2.0);
    }

    public int getColorBarRGB(){
        double scaleValue = (value+1)/(80);
        scaleValue = (scaleValue * 2.0) - 1.0;
        this.red = sigmoid(scaleValue, gain, -1.0 * offsetX);
        this.blue = 1.0 - sigmoid(scaleValue, gain, offsetX);
        this.green = sigmoid(scaleValue, gain, offsetGreen) + (1.0 - sigmoid(scaleValue, gain, -1 * offsetGreen));
        this.green = this.green - 1.0;
        return Color.argb(100, (int)(255*red), (int)(255*green), (int)(255*blue));
    }

    public double getValue(){
        return value;
    }

    public int getLeft(){
        return (indexX) * 80;
    }

    public int getTop(){
        return (indexY) * 60;
    }

    public int getRight(){
        return ((indexX) * 80) + 80;
    }

    public int getBottom(){
        return ((indexY) * 60) + 60;
    }

}