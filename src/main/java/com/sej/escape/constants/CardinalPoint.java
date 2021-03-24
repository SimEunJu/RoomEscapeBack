package com.sej.escape.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CardinalPoint {
    NORTH(0), WEST(270), SOUTH(180), EAST(90),
    NORTH_WEST(315), SOUTH_WEST(225), SOUTH_EAST(135), NORTH_EAST(45)
    ;
    private double angle;
}
