package ka.masato.grideyelib.driver.enums;

public enum IntruptMode {

    ABSOLUTE_VALUE_INT_ACTIVE(0x03),
    ABSOLUTE_VALUE_INT_DEACTIVE(0x02),
    DIFFERENT_INT_ACTIVE(0x01),
    DIFFERENT_INT_DEACTIVE(0x00);

    private final byte value;

    private IntruptMode(int i) {
        this.value = (byte) i;
    }

    public byte getValue() {
        return value;
    }
}
