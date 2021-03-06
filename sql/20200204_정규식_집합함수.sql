-- 2020-02-04 정규식 함수 (REGEXP_LIKE) 
/*
<정규식 기호>
 ^ : 시작 ==> A%
 $ : 끝 ==> %A
 . : 임의의 한 글자 ==> _
 ? : 앞의 글자가 포함이 되고, 미포함 ( ) 
 * : 여러 글자 (글자수가 0일수도 있음) 
 + : 여러 글자 (글자수가 1이상) 
     ex) [가-힣]* : 한글 여러글자. 한글이 0글자일 수도 있음. 
         [가-힣]+ : 한글 1글자 이상 
     ex) 맛있는 맛있게 맛있고 맛있 
         ^[맛있]* ==> 맛있는 맛있게 맛있고 맛있 
         ^[맛있]+ ==> 맛있는 맛있게 맛있고 
     ex) 짜고 짜다 짜서 짜
         ^[짜]* ==> 짜고 짜다 짜서 짜
         ^[짜]+ ==> 짜고 짜다 짜서 
 []: 해당문자 포함 
     ex) [A]: A 포함
         [A-Z]: 대문자 포함
         [a-z]: 소문자 포함 
         [가-힣]: 한글 포함
         [0-9]: 숫자 포함 
 [^] : 미포함 
 | : OR
 {}: 갯수 
     ex) {3}: 3개
         {1-3}: 1-3개
 \: 오라클에서 사용하는 문자들(. ? &) 자체를 가지고 오고 싶을 때 
     ex) [\.] : '.'을 포함하는 애 
     
 
*/

--ex) A로 시작하는 key값
SELECT key FROM music_genie 
WHERE REGEXP_LIKE (key, '^A');
SELECT key FROM music_genie 
WHERE key LIKE 'A%';

--ex) 대문자로 시작하는 key값
SELECT key FROM music_genie 
WHERE REGEXP_LIKE (key, '^[A-Z]');

--ex) 영어로 시작하는 key값 
SELECT key FROM music_genie
WHERE REGEXP_LIKE (key, '^[A-Za-z]');

--ex) 숫자로 시작하는 key값
SELECT key FROM music_genie
WHERE REGEXP_LIKE (key, '^[0-9]');

--ex) 공백으로 시작하는 key값 ==> 없다. 
SELECT key FROM music_genie
WHERE REGEXP_LIKE (key, '^[[:space:]]'); 

--ex) _가 들어간 key값 
SELECT key FROM music_genie
WHERE REGEXP_LIKE (key, '_'); 

--ex) 숫자로 끝나는 key값 
SELECT key FROM music_genie
WHERE REGEXP_LIKE (key, '[0-9]$'); 

--ex) '-' 또는 '--'를 포함하는 key값 
SELECT key FROM music_genie
WHERE REGEXP_LIKE (key, '-|--'); 

--ex) '-' 또는 숫자를 포함하는 key값 
SELECT key FROM music_genie
WHERE REGEXP_LIKE (key, '-|[0-9]'); 

--ex) .의 용법 ==> .이 임의의 글자를 의미하므로 200행 전체가 다 나옴
SELECT key FROM music_genie
WHERE REGEXP_LIKE (key, '.'); 

--ex) .이라는 글자가 포함된 데이터를 원한다면 \. 으로 써야. 
SELECT key FROM music_genie
WHERE REGEXP_LIKE (key, '\.'); 

--ex) 대문자 2글자로 시작하는 key값
SELECT key FROM music_genie
WHERE REGEXP_LIKE (key, '^[A-Z]{2}'); 

--ex) 소문자 3글자로 끝나는 key값 
SELECT key FROM music_genie
WHERE REGEXP_LIKE (key, '[a-z]{3}$'); 

--ex) 숫자 2개로 끝나는 key값 
SELECT key FROM music_genie
WHERE REGEXP_LIKE (key, '[0-9]{2}$'); 

--ex) 두번째 문자가 P거나 p인 key값 
SELECT key FROM music_genie
WHERE REGEXP_LIKE (key, '^.(P|p)'); 
SELECT key FROM music_genie
WHERE REGEXP_LIKE (key, '^.[P|p]'); 
SELECT key FROM music_genie
WHERE REGEXP_LIKE (key, '^.[Pp]'); 

--ex) '_B' 또는 'a_'를 포함하고 있는 key값
SELECT key FROM music_genie
WHERE REGEXP_LIKE (key, '(_B|a_)'); 

SELECT title FROM music_genie;

--ex) '?'가 포함된 제목
SELECT title FROM music_genie
WHERE REGEXP_LIKE (title, '\?'); 

SELECT title FROM music_genie
WHERE REGEXP_LIKE (title, '\?|\.');

--ex) '&'이 포함된 제목 
SELECT title FROM music_genie
WHERE REGEXP_LIKE (title, '\&'); 

--ex) ')'이 포함된 제목
SELECT title FROM music_genie
WHERE REGEXP_LIKE (title, '\)'); 

--ex) 
SELECT singer,title FROM music_genie
WHERE REGEXP_LIKE (singer, '아이유'); 

--ex) 가나다라마바사 가 포함된 제목
SELECT title FROM music_genie
WHERE REGEXP_LIKE (title, '가|나|다|라|마|바|사');

--ex) 제목이 ㄱ-ㄴ 포함?
SELECT title FROM music_genie
WHERE REGEXP_LIKE (title, '[가-나]');

--★ ex) 노래 제목에 공백 없는 것 
SELECT title FROM music_genie
WHERE NOT REGEXP_LIKE (title,'[[:space:]]');

--★ ex) 제목이 영어로만 된 노래 (한글,숫자X)
SELECT title FROM music_genie
WHERE NOT REGEXP_LIKE (title,'[가-힣0-9]');
--한자는 안 빠짐... 'WINTER FLOWER (雪中梅) (Feat. RM)' 는 어쩔 수 없당..

--ex) Mo 또는 Ma로 시작하는 노래 제목
SELECT title FROM music_genie
WHERE REGEXP_LIKE (title,'^(Mo|Ma)');

--ex) M으로 시작하고 두번째 글자가 o 또는 a로 시작하는 노래
SELECT title FROM music_genie
WHERE REGEXP_LIKE (title,'^M(a|o)');

--ex) 제목에 Ma또는 Mo가 포함된 노래 
SELECT title FROM music_genie
WHERE REGEXP_LIKE (title,'(Mo|Ma)');

--ex) 한글로 시작하는 노래만 
SELECT title FROM music_genie
WHERE REGEXP_LIKE (title,'^[가-힣]');

/*
<집합함수>
 - COUNT, MAX, AVG, MIN, RANK, SUM
 - SELECT FROM
   WHERE
   ===========
   GROUP BY      <==오늘은 GROUP BY 랑 HAVING 배울 예정!
   HAVING
   ===========
   ORDER
*/

/*
<GROUP BY>
 - HAVING은 GROUP BY 뒤에 나와야. HAVING 혼자 단독으로 쓸 수 없다. 
 
*/
SELECT * FROM emp ORDER BY deptno;

--ex) 부서별 인원수, 월급 합, 월급 평균 
SELECT deptno, COUNT(*), SUM(sal), AVG(sal) FROM emp
GROUP BY deptno;

--ex) 직업별 인원수, 월급 최대값, 월급 최소값
SELECT job, COUNT(*), MAX(sal), MIN(sal) FROM emp
GROUP BY job;

--★ex) 입사년도별로 인원수, 급여의 합, 급여 평균 출력 
SELECT TO_CHAR(hiredate,'YYYY') AS "입사년도", COUNT(*), SUM(sal), AVG(sal) FROM emp
GROUP BY TO_CHAR(hiredate,'YYYY');
SELECT SUBSTR(hiredate,1,2) AS "입사년도", COUNT(*), SUM(sal), AVG(sal) FROM emp
GROUP BY SUBSTR(hiredate,1,2);

--★ex) 입사한 요일별 인원수, 급여합, 급여평균 출력 
SELECT TO_CHAR(hiredate,'Day') AS "입사요일", COUNT(*), SUM(sal), AVG(sal) FROM emp
GROUP BY TO_CHAR(hiredate,'Day'); --'금요일' 형식
SELECT TO_CHAR(hiredate,'DY') AS "입사요일", COUNT(*), SUM(sal), AVG(sal) FROM emp
GROUP BY TO_CHAR(hiredate,'DY'); --'금' 형식
SELECT TO_CHAR(hiredate,'D') AS "입사요일", COUNT(*), SUM(sal), AVG(sal) FROM emp
GROUP BY TO_CHAR(hiredate,'D'); --'1' 형식 (1:일요일, 7:토요일)

--★ex) 2중 그룹 
SELECT deptno, job, SUM(sal), AVG(sal) FROM emp
GROUP BY deptno,job
ORDER BY deptno ASC;

--★ex) 2중그룹 - 부서별 년도별로 
SELECT deptno, TO_CHAR(hiredate,'YYYY'), COUNT(*), SUM(sal), AVG(sal) FROM emp
GROUP BY deptno, TO_CHAR(hiredate,'YYYY')
ORDER BY deptno ASC;

--이렇게 하면 출력 불가!! 
SELECT deptno, TO_CHAR(hiredate,'YYYY'), job FROM emp
GROUP BY deptno, TO_CHAR(hiredate,'YYYY')
ORDER BY deptno ASC;
--이렇게는 가능 : 3중 
SELECT deptno, TO_CHAR(hiredate,'YYYY'), job FROM emp
GROUP BY deptno, TO_CHAR(hiredate,'YYYY'), job
ORDER BY deptno ASC;

--ex) 그룹 조건
SELECT deptno,COUNT(*), SUM(sal), AVG(sal) FROM emp
GROUP BY deptno;
--ex) 그룹 조건 
SELECT deptno,COUNT(*), SUM(sal), AVG(sal) FROM emp
GROUP BY deptno
HAVING AVG(sal)<=2000;

--ROLLUP
SELECT deptno, job, COUNT(*),ROUND(AVG(sal),2) FROM emp
GROUP BY ROLLUP(deptno,job);

--CUBE   
SELECT deptno, job, COUNT(*),ROUND(AVG(sal),2) FROM emp
GROUP BY CUBE(deptno,job);















