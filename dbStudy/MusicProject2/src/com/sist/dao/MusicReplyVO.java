package com.sist.dao;
/*
	[music_member 테이블]
	NO      NOT NULL NUMBER       
	MNO              NUMBER       
	ID               VARCHAR2(20) 
	NAME    NOT NULL VARCHAR2(51) 
	MSG     NOT NULL CLOB         
	REGDATE          DATE
	===================================
	이름(날짜, 시간) 			수정 삭제 댓글     ===> 날짜는 regdate로, 
	===================================      시간은 TO_CHAR(regdate, 'YYYY-MM-DD HH24:MI:SS')로 ★
	댓글										  ==> VO에 시간 값이 하나 추가되어 있어야 함 ★★★
	
	===================================
	===================================
	이름(날짜, 시간)			수정 삭제 댓글
	===================================
	댓글
	
	===================================
 */
import java.util.*;
public class MusicReplyVO {
	// 1. 테이블 컬럼 
	private int no;
	private int mno;
	private String id;
	private String name;
	private String msg;
	private Date regdate;
	
	// 2. 테이블 컬럼이 아니더라도 필요한 변수를 추가 가능하다.
	private String dbDay; //댓글작성 시간을 추가하기 위해 dbDay를 만들었다. ★★★
	private String sex; 	
	
	//reply를 따로 만드는 이유: 어떤 위치든 다 달 수 있게 하기 위하여. 재사용이 쉬움. 
	
	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
	}
	public int getMno() {
		return mno;
	}
	public void setMno(int mno) {
		this.mno = mno;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Date getRegdate() {
		return regdate;
	}
	public void setRegdate(Date regdate) {
		this.regdate = regdate;
	}
	public String getDbDay() {
		return dbDay;
	}
	public void setDbDay(String dbDay) {
		this.dbDay = dbDay;
	}
	
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	
	
}
