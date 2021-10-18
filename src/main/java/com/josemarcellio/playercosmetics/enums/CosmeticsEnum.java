package com.josemarcellio.playercosmetics.enums;

import java.util.Arrays;
import java.util.Optional;

/**
 * enum of Cosmetic types, can either be something that goes in the armor slots or something that stays in the inventory
 *
 */

public enum CosmeticsEnum {
    INVENTORY_ITEM ((byte)4),
    HAT ((byte)3);

    private final byte id;
    CosmeticsEnum(byte i) {
        this.id = i;
    }

    public byte getID() {
        return id;
    }

    public static Optional<com.josemarcellio.playercosmetics.enums.CosmeticsEnum> getFromID(byte value) {
        return Arrays.stream(values()).filter(legNo -> legNo.id == value).findFirst();
        //https://stackoverflow.com/questions/11047756/getting-enum-associated-with-int-value
    }

}
