package com.swssu.flexrate.creditRating.enums;

import com.swssu.flexrate.exception.AppException;
import com.swssu.flexrate.exception.enums.ErrorCode;
import lombok.Getter;

@Getter
public enum IncomeType {
    INCOME_EMPLOYEE_O("직장가입자(4대보험O)"),
    INCOME_EMPLOYEE_X("직장가입자(4대보험X)"),
    INCOME_BUSINESS("개인사업자"),
    INCOME_FREE("프리랜서"),
    INCOME_ETC("기타소득");

    private final String value;

    IncomeType(String value) {
        this.value = value;
    }

    public static IncomeType fromValue(String value) {
        for (IncomeType type : IncomeType.values()) {
            if (type.getValue().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new AppException(ErrorCode.ENUMTYPE_BAD_REQUEST, value + " : enum type 오류");
    }

}
