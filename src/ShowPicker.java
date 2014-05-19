

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Servlet implementation class ShowPicker
 */
@WebServlet("/ShowPicker")
public class ShowPicker extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ShowPicker() {
        super(); 
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("I'min the doGet method!!");
		
		String show = request.getParameter("video");
		
		Document doc;
		try {
			
			doc = Jsoup.connect("http://www.desi-tashan.com/").userAgent("Chrome").get();
			
			// get all links
			Elements links = doc.select("a[href]");
			for (Element link : links) {
		
				if(link.attr("href").contains(show)){
					
					System.out.println("Inside the if statement.");
					System.out.println("\nlink : " + link.attr("href"));
					
					Document doc2 = Jsoup.connect(link.attr("href")).userAgent("Chrome").get();
					
					Elements Dailymotionlinks = doc2.select("a[href]");
					ArrayList<String> DLinks = new ArrayList<String>();
					
					String parameter = "videos=";
					
					for (Element Dlink : Dailymotionlinks) {
						
						if(Dlink.attr("href").contains("dailymotion")){
							DLinks.add(Dlink.attr("href").toString());
						}					
					}
					
					if(DLinks.size() <= 1){
						System.out.println("There are only two links!!!");
						if(DLinks.get(0).contains("part1") && DLinks.get(1).contains("part2")){
						System.out.println("There are only two links!!!");
						parameter = parameter+getDailyMotionVideoIds(1,DLinks);
						}
						
					}else{
						if(DLinks.get(0).contains("part1") && DLinks.get(1).contains("part2") && DLinks.get(2).contains("part3")){
							System.out.println("There are only three links!!!");	
							parameter = parameter+getDailyMotionVideoIds(2,DLinks);
						}else if(DLinks.get(0).contains("part1") && DLinks.get(1).contains("part2") && DLinks.get(2).contains("part3") && DLinks.get(2).contains("part4")){
							System.out.println("There are only four links!!!");	
							parameter = parameter+getDailyMotionVideoIds(3,DLinks);
						}else{
							System.out.println("There are only 2 links!!!");
							parameter = parameter+getDailyMotionVideoIds(1,DLinks);
						}
					}
					
					System.out.println("The paramter values are"+ parameter);
					response.sendRedirect("index.html?"+parameter);
					//request.getRequestDispatcher("?"+parameter).forward(request, response);
					return;
				}
			}
	 
		} catch (IOException e) {
			e.printStackTrace();
		}

		response.sendRedirect("index.html?test");		
	}

	
	private String getDailyMotionVideoIds(int maxIndex, ArrayList<String> DLinks) throws IOException{
		int count = 0; 
		String params = "";
		while(count != maxIndex+1){
			Document doc = Jsoup.connect(DLinks.get(count)).userAgent("Chrome").get();
			
			Elements dailymotionEmbedLink = doc.getElementsByClass("DesiPlayer");
			String parts[] =dailymotionEmbedLink.toString().split("video/");
		
			String specialParameter[] = parts[1].toString().split("\\?");
			params = params + specialParameter[0] +",";
			count++;
		}
		params = params.substring(0, params.length() - 1);
		
		return params;
	}

}
