package com.tz.myyouku.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.tz.myyouku.beans.VideoInfo;
import com.tz.myyouku.service.HomePageService;

/**
 * 给客户端响应首页的影片信息
 * @author Jason
 * @date 2015-4-26
 */
public class HomePageSerlvet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
    public HomePageSerlvet() {
        super();
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String json = null;
		try {
			HomePageService service = new HomePageService();
			List<VideoInfo> list = service.get();
			Gson gson = new Gson();
			json = gson.toJson(list);
		} catch (Exception e) {
			e.printStackTrace();
			json = "失败";
		}
		
		//跳转到jsp打印json数组
		request.setAttribute("json", json);
		request.getRequestDispatcher("/json.jsp").forward(request, response);
		
	}

}
