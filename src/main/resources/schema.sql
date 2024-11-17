-- DEPT 테이블(코드성)
CREATE TABLE DEPT (
  id VARCHAR(50) PRIMARY KEY,  -- id는 문자열로 설정
  name VARCHAR(100) NOT NULL   -- name은 부서의 이름을 저장
);

-- ROLE 테이블(코드성)
CREATE TABLE ROLE (
  id VARCHAR(50) PRIMARY KEY,  -- id는 문자열로 설정
  name VARCHAR(100) NOT NULL   -- name은 역할의 이름을 저장
);

-- USSR 테이블
CREATE TABLE USER (
  id VARCHAR(50) PRIMARY KEY,  -- user id는 문자열
  name VARCHAR(100) NOT NULL,  -- 사용자 이름
  password VARCHAR(100) NOT NULL,  -- 사용자 비밀번호
  dept_code VARCHAR(50),      -- 부서 코드
  FOREIGN KEY (dept_code) REFERENCES DEPT(id)  -- 부서 코드와 DEPT 테이블 연관
);

-- USER_ROLE 테이블
CREATE TABLE USER_ROLE (
   id VARCHAR(50) PRIMARY KEY,  -- 관계 테이블 고유 id
   user_id VARCHAR(50),         -- USER 테이블의 id (외래키)
   role_id VARCHAR(50),         -- ROLE 테이블의 id (외래키)
   FOREIGN KEY (user_id) REFERENCES USER(id),  -- USER 테이블과 연관
   FOREIGN KEY (role_id) REFERENCES ROLE(id)   -- ROLE 테이블과 연관
);


