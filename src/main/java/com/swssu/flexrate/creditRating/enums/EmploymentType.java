package com.swssu.flexrate.creditRating.enums;

import com.swssu.flexrate.exception.AppException;
import com.swssu.flexrate.exception.enums.ErrorCode;
import lombok.Getter;

@Getter
public enum EmploymentType {
    EMPLOYMENT_PERMANENT("정규직"),
    EMPLOYMENT_CONTRACT("계약직"),
    EMPLOYMENT_DAILY("일용직"),
    EMPLOYMENT_ETC("기타");

    private final String value;

    EmploymentType(String value) {
        this.value = value;
    }

    public static EmploymentType fromValue(String value) {
        for (EmploymentType type : EmploymentType.values()) {
            if (type.getValue().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new AppException(ErrorCode.ENUMTYPE_BAD_REQUEST, value + " : enum type 오류");
    }

}