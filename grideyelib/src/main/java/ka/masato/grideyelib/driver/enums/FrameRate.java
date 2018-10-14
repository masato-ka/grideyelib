package ka.masato.grideyelib.driver.enums;

public enum FrameRate {

    FRAMERATE_ONE(1),
    FRAMERATE_TEN(0);

    public int getValue() {
        return value;
    }

    private final int value;

    private FrameRate(final int value) {
        this.value = value;
    }


}
