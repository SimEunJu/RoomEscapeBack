package com.sej.escape.entity.constants;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Genre {
    DRAMAS(0), ROMANTIC(1), ANIMATION(2),
    HISTORICAL(3), SCI_FI(4), THRILLERS(5),
    CRIME(6), MYSTERIES(7), HORROR(8);

    private int binaryDigitPower;


}
