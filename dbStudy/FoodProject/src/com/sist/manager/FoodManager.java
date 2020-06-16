// 실행 => 데이터 수집됨 
package com.sist.manager;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.sist.dao.FoodDAO;
/*
 * <div class="top_list_slide">
        <ul class="list-toplist-slider" style="width: 531px;">
            <li>
              <img class="center-croping" alt="분짜 맛집 베스트 35곳 사진"
                   data-lazy="https://mp-seoul-image-production-s3.mangoplate.com/keyword_search/meta/pictures/ll_ikc83wddw3t9a.png?fit=around|600:400&amp;crop=600:400;*,*&amp;output-format=jpg&amp;output-quality=80"/>

              <a href="/top_lists/1649_bun_cha"
                 onclick="trackEvent('CLICK_TOPLIST', {&quot;section_position&quot;:0,&quot;section_title&quot;:&quot;믿고 보는 맛집 리스트&quot;,&quot;position&quot;:0,&quot;link_key&quot;:&quot;P5AXQ5F&quot;});">
                <figure class="ls-item">
                  <figcaption class="info">
                    <div class="info_inner_wrap">
                      <span class="title">분짜 맛집 베스트 35곳</span>
                      <p class="desc">"갑분짜: 갑자기 분짜 먹고 싶다."</p>
                      <p class="hash">
                          <span>#분짜 </span>
                          <span>#분짜 맛집 </span>
                          <span>#분짜맛집 </span>
                          <span>#베트남 </span>
                          <span>#베트남요리 </span>
                          <span>#베트남 요리 </span>
                          <span>#베트남음식 </span>
                          <span>#베트남 음식 </span>
                      </p>
                    </div>
                  </figcaption>
                </figure>
              </a>
            </li>
 */
public class FoodManager {
	
	// 1. 메인에서 카테고리 가져오기 
	public ArrayList<CategoryVO> categoryAllData()
	{
		ArrayList<CategoryVO> list = new ArrayList<CategoryVO>();
		try 
		{
			Document doc = Jsoup.connect("https://www.mangoplate.com/").get();
			Elements title=doc.select("div.info_inner_wrap span.title");
			Elements poster=doc.select("ul.list-toplist-slider li img.center-croping");
			Elements subject=doc.select("div.info_inner_wrap p.desc");
			Elements link=doc.select("ul.list-toplist-slider li a");
			
			for(int i=0; i<12;i++)
			{	
				System.out.println(title.get(i).text());
				System.out.println(subject.get(i).text());
				System.out.println(link.get(i).attr("href"));
				System.out.println(poster.get(i).attr("data-lazy"));
				
				CategoryVO vo = new CategoryVO();
				vo.setCateno(i+1); //i=0부터 시작하니까
				vo.setTitle(title.get(i).text());
				vo.setSubject(subject.get(i).text());
				vo.setLink("https://www.mangoplate.com"+link.get(i).attr("href"));
				String temp=poster.get(i).attr("data-lazy");
				temp=temp.replace("&", "@"); //Oracle에서 &하면 prompt창(입력창) 떠서... & 대신 @로 바꿔줬음.
				vo.setPoster(temp); 
				
				list.add(vo);
			}
			
		} 
		catch (Exception ex) 
		{
			ex.printStackTrace();
		}
		
		return list;
	}
	
	// 2. 카테고리 화면에서 노출되는 맛집리스트 10개에 대한 정보 가져오기
	public ArrayList<FoodHouseVO> foodAllData()
	{
		ArrayList<FoodHouseVO> fList = new ArrayList<FoodHouseVO>();
		try 
		{
			ArrayList<CategoryVO> cList = categoryAllData();
			int k=1;
			for(CategoryVO cvo:cList)
			{
				Document doc=Jsoup.connect(cvo.getLink()).get();
				/*
				 * <div class="with-review">
                  <figure class="restaurant-item">
                    <a href="/restaurants/DG9WuFwF8U" onclick="trackEvent('CLICK_RESTAURANT', {&quot;position&quot;:2,&quot;restaurant_key&quot;:&quot;DG9WuFwF8U&quot;})">
                      <div class="thumb">
                        <img class="center-croping lazy" alt="교다이야 사진 - 서울특별시 마포구 성지길 39" data-original="https://mp-seoul-image-production-s3.mangoplate.com/14798/1478273_1569385743173_13527?fit=around|738:738&amp;crop=738:738;*,*&amp;output-format=jpg&amp;output-quality=80" data-error="https://mp-seoul-image-production-s3.mangoplate.com/web/resources/kssf5eveeva_xlmy.jpg?fit=around|*:*&amp;crop=*:*;*,*&amp;output-format=jpg&amp;output-quality=80" src="https://mp-seoul-image-production-s3.mangoplate.com/14798/1478273_1569385743173_13527?fit=around|738:738&amp;crop=738:738;*,*&amp;output-format=jpg&amp;output-quality=80" style="display: block;">
                      </div>
                    </a>
				 */
				Elements link=doc.select("div.info span.title a");
				// 각각의 카테고리 잘 들어가는지 확인
				System.out.println(cvo.getTitle());
				
				
				for(int j=0;j<link.size();j++) //10바퀴 돈다. 
				{
					try {
						// 1) 상세페이지 url 가져옴
						String url="https://www.mangoplate.com"+link.get(j).attr("href");
						
						// 2) 상세화면에 나오는 정보들 가져옴
						Document doc2=Jsoup.connect(url).get();
						Elements poster=doc2.select("figure.list-photo img.center-croping");
						String img="";
						for(int a=0; a<poster.size(); a++)
						{
							//System.out.println(poster.get(a).attr("src"));
							img+=poster.get(a).attr("src")+"^"; //다섯개니까 한 번에 저장할 때 ^로 구분해줬음
						}
						img=img.substring(0,img.lastIndexOf("^")); //^ 기준으로 잘라줘라. 맨 마지막 ^ 하나는 제외하고.
						
						System.out.println("********************************");
						Element title=doc2.selectFirst("span.title h1.restaurant_name");
						Element score=doc2.selectFirst("span.title strong.rate-point span");
						Element address=doc2.select("table.info tr td").get(0); //첫번째 td값 가져오겠다 
						Element tel=doc2.select("table.info tr td").get(1);
						Element type=doc2.select("table.info tr td").get(2);
						Element price=doc2.select("table.info tr td").get(3);
						Element temp=doc2.selectFirst("script#reviewCountInfo"); // '맛있다', '괜찮다', '별로'는 js로 들어가고 있다 
						//<script id="reviewCountInfo" 
						/*
						 *  private String title;
							private double score;
							private String address;
							private String tel;
							private String type;
							private String price;
							private String parking;
							private String time;
							private String image;
							private int good;
							private int soso;
							private int bad;
						 */

						// 데이터 제대로 잡는지 확인
						System.out.println("k="+k);
						System.out.println(title.text());
						System.out.println(score.text());
						System.out.println(address.text());
						System.out.println(tel.text());
						System.out.println(type.text());
						System.out.println(price.text());
						System.out.println(temp.data()); //js는 .text()가 아니라 .data()로 줘야
						System.out.println("=================================================");
						
						FoodHouseVO vo=new FoodHouseVO();
						vo.setCno(cvo.getCateno());
						vo.setTitle(title.text());
						vo.setScore(Double.parseDouble(score.text()));
						vo.setAddress(address.text());
						vo.setTel(tel.text());
						vo.setType(type.text());
						vo.setPrice(price.text());
						vo.setImage(img);
						// '맛있다', '괜찮다', '별로' 정보를 가져와보자.. JSON parser로 잘라올거임. 
						// JSON은 {} or [{},{},...{}] 형태
						// [{"action_value":1,"count":11},{"action_value":2,"count":39},{"action_value":3,"count":125}] 
						// 순서대로 '별로', '괜찮다', '맛있다' 데이터임. 
						// 위의 애는 배열 ==> Array로 받는다
						JSONParser parser=new JSONParser();
						JSONArray arr=(JSONArray)parser.parse(temp.data());
						System.out.println("JSON=>"+arr.toJSONString());
						
						long[] data=new long[3];
						
						for(int b=0;b<arr.size();b++)
						{
							JSONObject obj=(JSONObject)arr.get(b); //{} 안에 들어있는 것 : Object
							System.out.println("Object=>"+obj.toJSONString());
							long count=(long)obj.get("count");
							data[b]=count;
							System.out.println("count="+count);
						}
						vo.setBad((int)data[0]);
						vo.setSoso((int)data[1]);
						vo.setGood((int)data[2]);
						
						fList.add(vo);
						k++;
					} catch (Exception ex) {}
					
				}
			}
		} 
		catch (Exception ex) {}
		
		return fList;
	}
	
	// main은 application (웹X) ==> DBCP 사용 불가.
	public static void main(String[] args) {
		// 메인 돌리면 톰캣이 동작X... 웹 프로그래밍이 아니므로. ==> DBCP 쓸 수 X. JDBC로 직접 연결해야. 
		// DBCP는 웹에서만 사용해야함. 
		
		FoodManager fm = new FoodManager();
		
		// 1) category 테이블에 데이터 삽입 
		// 주석해제 후 돌리면 테이블에 데이터 들어간다: FoodDAO.java의 categoryInsert 안의 sql구문 실행됨 
		/*ArrayList<CategoryVO> list=fm.categoryAllData();
		FoodDAO dao=FoodDAO.newInstance();
		int k=1;
		for(CategoryVO vo:list)
		{
			dao.categoryInsert(vo);
			System.out.println("k="+k);
			k++;
		}
		System.out.println("Save End...");*/
		
		// [기타] 콘솔창 통해서 망고플레이트 싸이트에서 데이터 잘 가져오는지 확인 ('2)'실행 전) 
		//fm.foodAllData();
		
		// 2) foodhouse 테이블에 데이터 삽입
		// 주석해제 후 돌리면 테이블에 데이터 들어간다: FoodDAO.java의 foodHouseInsert 안의 sql구문 실행됨 
		ArrayList<FoodHouseVO> list=fm.foodAllData();
		FoodDAO dao=FoodDAO.newInstance();
		int k=1;
		for(FoodHouseVO vo:list){
			dao.foodHouseInsert(vo);
			System.out.println("k="+k);
			//for문이 너무 빨리 돌아서... 못들어가는 데이터가 생겨서 쓰레드 실행에 텀을 좀 뒀음  
			try { 
				Thread.sleep(100);
			} catch (Exception ex) {}
			k++;
		}
		System.out.println("Save End...");
		
	}
	
}

/*
 * 2020.02.20 (목)
 * 
 * <웹 프로그램 vs Application >
 * 1. 웹 프로그램
 *   - Servlet과 JSP만 웹 프로그램임. 
 *   - 톰캣을 사용한다. 
 *   - 구동: 우클릭 > Run As > Run on Server
 * 2. Application 
 *   - 파일 내에 main이 들어가는 애들은 java만 단독으로 돌릴 수 있는 프로그램 ==> 웹 프로그램이 아니다. 
 *   - 브라우저에 출력하지 않고, Servlet이랑 JSP를 이용하지 않음.
 *   - 웹이 아니므로 톰캣이 동작하지 않는다. 
 *   - CBCP를 쓸 수 X(톰캣 동작 안하므로) ==> JDBC로 직접 연결해야. 
 *   - 구동: 우클릭 > Run As > Java Application 
 *   
 */
