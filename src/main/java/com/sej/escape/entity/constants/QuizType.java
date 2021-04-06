package com.sej.escape.entity.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum QuizType{

    LOCK, DEVICE;

}
