package org.among.jpatest.common;

public enum Dept {
    SALES("01", "영업부"),
    ACCOUNTING("02", "회계부");

//    Dept(String code, String name) {
//    }

    // getDeptById 메소드 위해 필드 선언
    private String code;
    private String name;

    Dept(String code, String name) {
        this.code = code;
        this.name = name;
    }

    // DB에서 조회해온 DEPT ID를 통해 부서명 반환하는 메소드
    public static Dept getDeptById(String deptId) {
        for (Dept dept : Dept.values()) {
            if (dept.code.equals(deptId)) {
                return dept;
            }
        }
        throw new RuntimeException("Dept not found with ID: " + deptId);
    }

    public String getName() {
        return name;
    }
}
