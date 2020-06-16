package com.sist.servlet;

import java.io.*; // *로 변경
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

// 추가 import
import com.sist.dao.*;

@WebServlet("/MusicDetail")
public class MusicDetail extends HttpServlet {
	private static final long serialVersionUID = 1L;


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out=response.getWriter();
		
		// ===============================================================
		// 1. HTML 출력하기 전에 받아야 할 정보를 받는다. 
		// ===============================================================
				
		// 1) 음악번호 (mno)
		String mno = request.getParameter("mno");
		
		//if(mno==null) // 
			//response.sendRedirect("MusicList");
	
		 
		
		// DAO 연동 
		MusicDAO dao = new MusicDAO();
		MusicVO vo = dao.musicDetailData(Integer.parseInt(mno));
		
		ArrayList<MusicReplyVO> list = dao.replyListData(Integer.parseInt(mno)); //어떤 mno에 대해서는 댓글이 없을 수도 있음 ==> 사이즈가 0 이상일 때 출력해야.
		ArrayList<MusicVO> topList = dao.musicTop5(); 
		
		// ===============================================================
		// 2. HTML 출력한다. 
		// ===============================================================
		
		out.println("<html>");
		out.println("<head>");
		out.println("<link rel=stylesheet href=\"css/bootstrap.min.css\">");
		out.println("<style type=text/css>");
		out.println(".col-md-9{");
		out.println("margin: 0px auto;");
		out.println("width: 900px;");
		out.println("}");
		out.println("h1{");
		out.println("text-align: center;");
		out.println("}");
		out.println("</style>");
		out.println("<script type=text/javascript src=\"http://code.jquery.com/jquery.js\"></script>");
		out.println("<script>");
		out.println("var i=0;");
		/*
		 *  <JavaScript>
		 *  - 변수 var. 동적형변환.
		 *  var i=0; ==> i(int)
		 *  var i=10.5 ==> i(double)
		 *  var i="Hello" ==> i(String)
		 *  var i=[] ==> i(배열)
		 *  var i={} ==> i(객체)
		 */
		// 수정 버튼 누르면 취소로 바뀌게, 취소 버튼 누르면 수정으로 바뀌게 처리.
		out.println("$(function(){");
		out.println("$('#ubtn').click(function(){");
		out.println("if(i==0){");
		out.println("$('#ubtn').val('취소');");
		out.println("i=1;");
		out.println("}");
		out.println("else{");
		out.println("$('#ubtn').val('수정');");
		out.println("i=0;");
		out.println("}");
		out.println("});");
		out.println("});");
		out.println("</script>");
		out.println("</head>");
		out.println("<body>");
		out.println("<div class=container-fluid>");
		out.println("<h1>&lt;"+vo.getTitle()+"&gt; 상세보기</h1>");
		
		// ======================================================================================================
		// [좌측영역] 게시글 상세페이지
		out.println("<div class=col-md-9>");
		out.println("<table class=\"table table-bordered\">");
		// 1. 동영상 
		out.println("<tr>");
		out.println("<td colspan=2 class=text-center>");
		out.println("<embed src=\"http://youtube.com/embed/"+vo.getKey()+"\" width=800 height=350>");
		out.println("</td>");
		out.println("</tr>");
		// 2. 노래명 
		out.println("<tr>");
		out.println("<td width=10% class=text-right>");
		out.println("노래명");
		out.println("</td>");
		out.println("<td width=90% class=text-left>");
		out.println(vo.getTitle());
		out.println("</td>");
		out.println("</tr>");
		// 3. 가수명 
		out.println("<tr>");
		out.println("<td width=10% class=text-right>");
		out.println("가수명");
		out.println("</td>");
		out.println("<td width=90% class=text-left>");
		out.println(vo.getSinger());
		out.println("</td>");
		out.println("</tr>");
		// 4. 앨범 
		out.println("<tr>");
		out.println("<td width=10% class=text-right>");
		out.println("앨범");
		out.println("</td>");
		out.println("<td width=90% class=text-left>");
		out.println(vo.getAlbum());
		out.println("</td>");
		out.println("</tr>");
		
		out.println("<tr>");
		out.println("<td colspan=2 class=text-right>");
		out.println("<a href=\"MusicList\" class=\"btn btn-lg btn-success\">목록</a>");
		out.println("</td>");
		out.println("</tr>");
		out.println("</table>");
		
		out.println("<div style=\"height=20px\"></div>");
		
		
		HttpSession session = request.getSession(); //session을 얻어온다. 
		// request ==> session, cookie: request가 있으면 sesseion과 cookie를 가지고 올 수 있다.  
		String id=(String)session.getAttribute("id");
		
		// 5. 댓글 출력 - 로그인 여부와 관계 없이 댓글 볼 수 있음 
		// 5-1) 댓글이 없는 경우 		
		if(list.size()<1) 
		{	// null인 경우도 있지 않나?? ==> 없음. new 해서 메모리 할당했기 때문에 댓글이 없는 경우는 null이 아니라 size=0으로 들어갈 것.
			// size=0 이란 뜻은 메모리 할당은 됐으나 데이터가 없단 것.
			// null은 메모리 할당도 안 됐다는 뜻.
			out.println("<table class=\"table table-striped\">");
			out.println("<tr>");
			out.println("<td class=text-center>");
			out.println("<h3>댓글이 존재하지 않습니다.</h3>");
			out.println("</td>");
			out.println("</tr>");
			out.println("</table>");
		}
		// 5-2) 댓글이 있는 경우
		else{
			out.println("<table class=\"table table-striped\">");
			for(MusicReplyVO rvo:list)  //위에서 이미 vo 이름 썼어서 다시 쓸 수 X
			{
				out.println("<tr>");
				out.println("<td class=text-left>");
				String temp="";				
				// (1) 프로필 사진 - 성별에 따라 다르게 노출 
				if(rvo.getSex().equals("남자"))
					temp="m2.jpg";
				else
					temp="w3.jpg";
				out.println("<img src=\"images/"+temp+"\" width=25 height=25>");
				// (2) 이름, (3)등록일
				out.println("&nbsp;"+rvo.getName()+"("+rvo.getDbDay()+")");
				out.println("</td>");
				out.println("<td class=text-right>");
				//내가 쓴 글이면 수정/삭제 버튼 노출 
				if(rvo.getId().equals(id)) 
				{
					// (4) 수정버튼 
					out.println("<input type=button class=\"btn btn-xs btn-primary\" value=수정 id=ubtn data="+rvo.getNo()+">");
					// (5) 삭제버튼 
					out.println("<a href=\"ReplyDelete?no="+rvo.getNo()+"&mno="+mno+"\" class=\"btn btn-xs btn-danger\">삭제</a>"); 
					// 삭제: 댓글번호(no)와 게시물번호(mno)가 필요함 (댓글번호no: 답글 삭제를 위해서. 게시물 번호mno: 다시 게시물로 돌아오기 위해서.) 
				}
				out.println("</td>");
				out.println("</tr>");
				// (6) 댓글내용
				out.println("<tr>");
				out.println("<td colspan=2><pre style=\"whilte-space: pre-wrap\">"+rvo.getMsg()+"</pre></td>"); //white-space: 글자를 벗어나면 
				out.println("</tr>");
				
				// 댓글수정 창 - 수정버튼 누르기 전까지는 감춰져 있어야.
				out.println("<tr id=\"m"+rvo.getNo()+"\" style=\"display:none\">"); // 감춰놨음. 
				out.println("<td colspan=2>");
				out.println("<textarea rows=3 cols=80 style=\"float: left;\" required name=msg>"+rvo.getMsg()+"</textarea>");
				out.println("<input type=hidden value="+mno+" name=mno>");  
				out.println("<input type=submit value=댓글수정 style=\"height:70px; float: left;\" class=\"btn btn-primary\">");
				out.println("</td>");
				out.println("</tr>");
			}
			out.println("</table>");
		}		
		
		// 6. 댓글 입력 - 로그인시에만 댓글 입력 가능 
		if(id!=null) 
		{ 	// null: 저장이 안 된 상태  ==> Login이 안 된 상태(null) vs Login이 된 상태 (등록id) 
			// Login.java 파일부터 실행하면 댓글란이 나온다.
			// 로그인처리, 로그인 해야만 노출되는 메뉴 처리할 때 등에 사용. 
			out.println("<form method=post action=\"ReplyInsert\">");
			out.println("<table class=\"table table-striped\">");
			out.println("<tr>");
			out.println("<td>");
			out.println("<textarea rows=3 cols=80 style=\"float: left;\" required name=msg></textarea>");
			out.println("<input type=hidden value="+mno+" name=mno>"); //name갖고 값 읽으니까 꼭 name 줘야! ★★
			out.println("<input type=submit value=댓글쓰기 style=\"height:70px; float: left;\" class=\"btn btn-primary\">");
			out.println("</td>");
			out.println("</tr>");
			out.println("</table>");
			out.println("</form>");
		}		
		out.println("</div>");
		
		// ======================================================================================================
		// [우측영역] 인기 Top5
		out.println("<div class=col-md-3>");
		out.println("<table class=\"table table-striped\">");
		out.println("<caption>인기순위 Top5</caption>");
		for(MusicVO tvo:topList) //위에서 vo 이미 썼기 때문에 vo 쓸 수 X. 
		{
			out.println("<tr>");
			// 1) 순위 
			out.println("<td>"+tvo.getRank()+"</td>"); 
			// 2) 앨범 포스터
			out.println("<td>");
			out.println("<img src=\""+tvo.getPoster()+"\" width=35 height=35>");
			out.println("</td>");
			// 3) 노래제목
			out.println("<td>"+tvo.getTitle()+"</td>");
			out.println("</tr>");
		}
		out.println("</table>");
		out.println("</div>");
		
		out.println("</div>");
		out.println("</body>");
		out.println("</html>");
		
	}



}

