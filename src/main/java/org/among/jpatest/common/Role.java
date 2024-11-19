package org.among.jpatest.common;

public enum Role {
    ADMIN("01", "관리자"),
    LEADER("02", "팀장"),
    MEMBER("03", "팀원");

    // getDeptById 메소드 위해 필드 선언
    private String code;
    private String name;
    Role(String code, String name) {
        this.code = code;
        this.name = name;
    }

    // DB에서 조회해온 ROLE ID를 통해 부서명 반환하는 메소드
    public static Role getRoleById(String roleId) {
        for (Role role : Role.values()) {
            if (role.code.equals(roleId)) {
                return role;
            }
        }
        throw new RuntimeException("Role not found with ID: " + roleId);
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
