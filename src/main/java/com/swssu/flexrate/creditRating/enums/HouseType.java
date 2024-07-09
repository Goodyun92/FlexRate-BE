package com.swssu.flexrate.creditRating.enums;

import com.swssu.flexrate.exception.AppException;
import com.swssu.flexrate.exception.enums.ErrorCode;
import lombok.Getter;

@Getter
public enum HouseType {
    HOUSE_LEASE("전월세"),
    HOUSE_FAMILY("기타가족소유"),
    HOUSE_OWNER("자가"),
    HOUSE_SPOUSE("배우자");

    private final String value;

    HouseType(String value) {
        this.value = value;
    }

    public static HouseType fromValue(String value) {
        for (HouseType type : HouseType.values()) {
            if (type.getValue().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new AppException(ErrorCode.ENUMTYPE_BAD_REQUEST, value + " : enum type 오류");
    }

}
