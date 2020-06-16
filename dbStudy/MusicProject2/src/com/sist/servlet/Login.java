package com.sist.servlet;

import java.io.*;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

//추가 import - 1) dao 
import com.sist.dao.*;

@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;		

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		out.println("<html>");
		out.println("<head>");
		out.println("<link rel=stylesheet href=\"css/bootstrap.min.css\">");
		out.println("<style type=text/css>");
		out.println(".row{");
		out.println("margin: 0px auto;"); //가운데 정렬 
		out.println("width: 350px;");
		out.println("}");
		out.println("h1{");
		out.println("text-align: center;");
		out.println("}");
		out.println("</style>");
		out.println("</head>");
		out.println("<body>");
		out.println("<div class=\"container-fluid\">");
		out.println("<h1>Login</h1>");
		out.println("<div class=row>");
		out.println("<form method=post action=Login>"); 
		// doPost 호출해야하니까 method=post
		// 자기 자신한테 보내면 되니까 action=Login
		out.println("<table class=\"table table-hover\">");
		out.println("<tr>");
		out.println("<td width=20% class=text-right>ID</td>");
		out.println("<td width=80% class=text-left><input type=text name=id size=15></td>");
		out.println("</tr>"); 
		out.println("<tr>");
		out.println("<td width=20% class=text-right>Password</td>");
		out.println("<td width=80% class=text-left><input type=password name=pwd size=15></td>");
		out.println("</tr>"); 
		out.println("<tr>");
		/*
		 * btn-xs, btn-sm, btn-md, btm-lg
		 */
		out.println("<td colspan=2 class=text-center><input type=submit class=\"btn btn-sm btn-warning\" value=로그인></td>");
		out.println("</tr>"); 
		out.println("</table>");
		out.println("</form>");
		out.println("</div>"); //.row 닫음
		out.println("</div>"); //.container 닫음
		out.println("</body>");
		out.println("</html>");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8"); //js로 로그인 이벤트 처리해줘야 하니까 컨텐츠타입 html이라고 했음. html안의 js 읽게...
		PrintWriter out = response.getWriter();
		
		MusicDAO dao = new MusicDAO();
		// 사용자가 보내준 ID,PWD 읽어 와야
		// getParameter안에는 아무거나 쓸 수 있는 게 아니라, form에서 보내준 name값을 써야 ★★★
		String id=request.getParameter("id");
		String pwd=request.getParameter("pwd");
		
		String result=dao.isLogin(id, pwd);
		if(result.equals("NOID"))
		{
			out.println("<script>");
			out.println("<alert(\"ID가 존재하지 않습니다.\");>");
			out.println("history.back();");
			out.println("</script>");
		}
		else if(result.equals("NOPWD"))
		{
			out.println("<script>");
			out.println("<alert(\"비밀번호가 존재하지 않습니다.\");>");
			out.println("</script>");
		}
		else
		{
			// session 가져옴 
			HttpSession session = request.getSession();
			
			// id,name 
			session.setAttribute("id", id); 
			StringTokenizer st = new StringTokenizer(result,"|");
			session.setAttribute("name", st.nextToken()); // 왜 name을 result로 가져오나? DAO에서 ID/비번 다 일치하면 result에 name을 싣었기 때문. 
			// setAttribute(String key, Object value) 
			// : 해당 내장 객체의 속성(attribute)값을 설정하는 메소드로, 속성명에 해당하는 key 매개 변수에, 속성값에 해당하는 value 매개 변수의 값을 지정한다. 
			session.setAttribute("sex", st.nextToken());
			
			// 파일 이동
			response.sendRedirect("MusicList");
			
			
		}
		
	}

}

/*
 * 
 * <Session>
 *  - Session: 사용자의 일부 정보를 저장. ★★★★★
 * 화면이 넘어감과 동시에 이 클래스가 끝나니까 메모리 회수당해서 id를 갖고가지 못함. 
 * 그러므로, id 잊어버리지 않으려고 세션에 저장하면 톰캣이 id 가져갈 수 있다. 
 * 세션 기본 시간: 30분. ex) 웹사이트 로긴하고 30분간 아무 동작 없으면 로그아웃됨  
 * 참고) 세션: 서버에 저장됨 <===> 쿠키: 컴퓨터에 저장됨
 */

/*
 * <session, request, response>
 *  - session: 일정시간 정보 유지.
 *  - request: 화면이 바뀌면 저장된 값이 초기화된다. ★★★ <== 기억할 것!!
 *  - session: 우리가 만든다. 
 *  - response, request는 톰캣이 넘겨준다. 
 */






