-- ---------------------------------------------------------------------------
-- dept / emp minta-adatbázis (a klasszikus "SCOTT" séma MySQL-es változata)
--
-- Saját készítésű script, az eredeti DEPT_EMP_MYSQL.sql nem volt a tananyag
-- része. A Docker első induláskor automatikusan lefuttatja a `dml`
-- adatbázison. Kézzel is újrafuttatható (pl. a 10%-os béremelés után az
-- eredeti adatok visszaállításához):
--
--   docker compose exec -T db mysql -udml -pdml dml < db/01-dept-emp.sql
-- ---------------------------------------------------------------------------

SET NAMES utf8mb4;

-- Újrafuttathatóság: előbb a gyerek (emp), aztán a szülő (dept) tábla törlése.
DROP TABLE IF EXISTS emp;
DROP TABLE IF EXISTS dept;

-- Osztályok
CREATE TABLE dept (
    DEPTNO INT          NOT NULL PRIMARY KEY,
    DNAME  VARCHAR(14)  NOT NULL,
    LOC    VARCHAR(13)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

-- Dolgozók. MGR: a közvetlen vezető EMPNO-ja (önhivatkozó idegen kulcs).
CREATE TABLE emp (
    EMPNO    INT          NOT NULL PRIMARY KEY,
    ENAME    VARCHAR(10)  NOT NULL,
    JOB      VARCHAR(9),
    MGR      INT,
    HIREDATE DATE,
    SAL      DECIMAL(7, 2),
    COMM     DECIMAL(7, 2),
    DEPTNO   INT,
    CONSTRAINT fk_emp_dept FOREIGN KEY (DEPTNO) REFERENCES dept (DEPTNO),
    CONSTRAINT fk_emp_mgr  FOREIGN KEY (MGR)    REFERENCES emp (EMPNO)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

INSERT INTO dept (DEPTNO, DNAME, LOC) VALUES
    (10, 'ACCOUNTING', 'NEW YORK'),
    (20, 'RESEARCH',   'DALLAS'),
    (30, 'SALES',      'CHICAGO'),
    (40, 'OPERATIONS', 'BOSTON');

-- A vezetőket előbb szúrjuk be, hogy az MGR idegen kulcs mindig érvényes legyen.
INSERT INTO emp (EMPNO, ENAME, JOB, MGR, HIREDATE, SAL, COMM, DEPTNO) VALUES
    (7839, 'KING',   'PRESIDENT', NULL, '1981-11-17', 5000.00, NULL,    10),
    (7566, 'JONES',  'MANAGER',   7839, '1981-04-02', 2975.00, NULL,    20),
    (7698, 'BLAKE',  'MANAGER',   7839, '1981-05-01', 2850.00, NULL,    30),
    (7782, 'CLARK',  'MANAGER',   7839, '1981-06-09', 2450.00, NULL,    10),
    (7788, 'SCOTT',  'ANALYST',   7566, '1987-04-19', 3000.00, NULL,    20),
    (7902, 'FORD',   'ANALYST',   7566, '1981-12-03', 3000.00, NULL,    20),
    (7369, 'SMITH',  'CLERK',     7902, '1980-12-17',  800.00, NULL,    20),
    (7499, 'ALLEN',  'SALESMAN',  7698, '1981-02-20', 1600.00,  300.00, 30),
    (7521, 'WARD',   'SALESMAN',  7698, '1981-02-22', 1250.00,  500.00, 30),
    (7654, 'MARTIN', 'SALESMAN',  7698, '1981-09-28', 1250.00, 1400.00, 30),
    (7844, 'TURNER', 'SALESMAN',  7698, '1981-09-08', 1500.00,    0.00, 30),
    (7876, 'ADAMS',  'CLERK',     7788, '1987-05-23', 1100.00, NULL,    20),
    (7900, 'JAMES',  'CLERK',     7698, '1981-12-03',  950.00, NULL,    30),
    (7934, 'MILLER', 'CLERK',     7782, '1982-01-23', 1300.00, NULL,    10);
