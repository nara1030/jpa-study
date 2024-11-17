-- 외래 키 제약 조건 오류를 무시하기 위한 임시 해결책
SET foreign_key_checks = 0;

-- DEPT 데이터
INSERT INTO DEPT (id, name) VALUES ('01', 'SALES');
INSERT INTO DEPT (id, name) VALUES ('02', 'ACCOUNTING');

-- ROLE 데이터
INSERT INTO ROLE (id, name) VALUES ('01', 'ADMIN');
INSERT INTO ROLE (id, name) VALUES ('02', 'LEADER');
INSERT INTO ROLE (id, name) VALUES ('03', 'MEMBER');

-- USER 데이터
INSERT INTO USER (id, name, password, dept_code) VALUES ('01', 'rami', '1234', '02');
INSERT INTO USER (id, name, password, dept_code) VALUES ('02', 'jun', '4321', '01');
INSERT INTO USER (id, name, password, dept_code) VALUES ('03', 'jisung', 'qwer', '02');
INSERT INTO USER (id, name, password, dept_code) VALUES ('04', 'kangin', '1111', '02');
INSERT INTO USER (id, name, password, dept_code) VALUES ('05', 'junho', 'abcd', '01');

COMMIT;

-- USER_ROLE 데이터
INSERT INTO USER_ROLE (id, user_id, role_id) VALUES ('01', '01', '02');
INSERT INTO USER_ROLE (id, user_id, role_id) VALUES ('02', '01', '03');
INSERT INTO USER_ROLE (id, user_id, role_id) VALUES ('03', '02', '03');
INSERT INTO USER_ROLE (id, user_id, role_id) VALUES ('04', '03', '03');
INSERT INTO USER_ROLE (id, user_id, role_id) VALUES ('05', '04', '03');
INSERT INTO USER_ROLE (id, user_id, role_id) VALUES ('06', '05', '03');

SET foreign_key_checks = 1;