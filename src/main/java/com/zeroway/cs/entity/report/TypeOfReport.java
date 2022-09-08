package com.zeroway.cs.entity.report;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum TypeOfReport {
    ABUSE("욕설/비하"),
    UNWHOLESOME("음란물/불건전한 만남 및 대화"),
    SCAM("유출/사칭/사기"),
    COMMERCIAL("상업적 광고 및 판매"),
    PAPERING("낚시/도배");

    private final String name;

    public static TypeOfReport enumOf(String name) {
        return Arrays.stream(TypeOfReport.values())
                .filter(t -> t.getName().equals(name))
                .findAny().orElse(null);
    }

}