package com.swssu.flexrate.creditRating.enums;

import com.swssu.flexrate.exception.AppException;
import com.swssu.flexrate.exception.enums.ErrorCode;
import lombok.Getter;

@Getter
public enum LoanPurpose {
    PURPOSE_LIVING("생활비"),
    PURPOSE_REFINANCE("대환대출"),
    PURPOSE_BUSINESS("사업자금"),
    PURPOSE_ETC("기타"),
    PURPOSE_HOUSELEASE("전월세보증금"),
    PURPOSE_INVESTMENT("투자"),
    PURPOSE_HOUSEPURCHASE("주택구입"),
    PURPOSE_CARPURCHASE("자동차구입");

    private final String value;

    LoanPurpose(String value) {
        this.value = value;
    }

    public static LoanPurpose fromValue(String value) {
        for (LoanPurpose type : LoanPurpose.values()) {
            if (type.getValue().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new AppException(ErrorCode.ENUMTYPE_BAD_REQUEST, value + " : enum type 오류");
    }

}