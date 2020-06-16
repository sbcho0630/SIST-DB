// News: 뉴스
package com.sist.view;

import java.io.*;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sist.dao.MovieDAO;
import com.sist.vo.MovieVO;
import com.sist.vo.NewsVO;

@WebServlet("/NewsServlet")
public class NewsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		// ===============================================================
		// 1. HTML 출력하기 전에 받아야 할 정보를 받는다. 
		// ===============================================================
				
		// 사용자에게서 페이지를 받는다.
		String strPage = request.getParameter("page");
		if(strPage==null)
			strPage="1";
		int curpage=Integer.parseInt(strPage);
		
		/*
		 * request ==> 값 받는 법
		 *  1. String getParameter()
		 *  2. String[] getParameterValues()
		 *     ex) 체크박스 UI에서 여러개 클릭 
		 */
		
		// ===============================================================
		// 2. DAO 연결 - 요청에 해당되는 DAO 연결.
		// ===============================================================
		
		// DAO 연결
		MovieDAO dao = MovieDAO.newInstance();
		ArrayList<NewsVO> list = dao.newsListData(curpage); 
		int totalpage=dao.newsTotalPage();
		
		// ===============================================================
		// 3. HTML 출력한다. 
		// ===============================================================
		
		out.println("<html>");
		out.println("<head>");
		out.println("<style type=text/css>");
		
		out.println(".row{");
		out.println("margin: 0px auto;");
		out.println("width: 1200px;");
		out.println("}");
		out.println("</style>");
		out.println("</head>");
		out.println("<body>");	//이미 MainServlet에 body안에 container만들어 놨음 ==> 또 container 줄 필요 없다. 
		
		// 1) 뉴스 리스트
		out.println("<div class=row>");
		out.println("<table class=\"table table-hover\">");
		out.println("<tr>");
		out.println("<td>");
		for(NewsVO vo:list)
		{
			out.println("<table class=\"table table-hover\">");
			// 1) 이미지 2) 타이틀
			out.println("<tr>");
			out.println("<td width=30% class=text-center rowspan=3>");
			out.println("<img src="+vo.getPoster()+" width=100%>");
			out.println("</td>");
			out.println("<td class=text-center width=70%><b><a href="+vo.getLink()+">"+vo.getTitle()+"</a></b></td>");
			out.println("</tr>");
			// 3) 컨텐츠, 4) 작성자, 5) 작성일
			out.println("<tr>");
			out.println("<td width=70%>"+vo.getContent()+"</td>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("<td width=70%>"+vo.getAuthor()+"・"+vo.getRegdate()+"</td>");
			out.println("</tr>");
			out.println("</table>");
			
		}
		out.println("</td>");
		out.println("</tr>");
		out.println("</table>");
		out.println("</div>");
		
		// 2) 페이지
		out.println("<div class=\"row text-center\">");
		out.println("<ul class=\"pagination pagination-lg\">");
		for(int i=1;i<=totalpage;i++)
		{
			out.println("<li><a href=\"MainServlet?mode=3&page="+i+"\">"+i+"</a></li>");
		}
		
		out.println("</ul>");
		out.println("</div>");
		
		out.println("</body>");
		out.println("</html>");
		
	}
}



