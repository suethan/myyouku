package com.tz.myyouku.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.tz.myyouku.beans.VideoInfo;
import com.tz.myyouku.utils.DBUtil;

public class HomePageService {

	/**
	 * 查询数据库获取首页的影片信息
	 * @return
	 */
	public List<VideoInfo> get(){
		List<VideoInfo> list = new ArrayList<VideoInfo>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.getConnection();
			String sql = " select * from banner ";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			VideoInfo info = null;
			while(rs.next()){
				info = new VideoInfo();
				info.setId(rs.getInt("id"));
				info.setTitle(rs.getString("title"));
				info.setDesc(rs.getString("desc"));
				info.setPlayTimes(rs.getString("play_times"));
				info.setImgUrl(rs.getString("img_url"));
				info.setVideoUrl(rs.getString("video_url"));
				list.add(info);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DBUtil.freeDB(rs, ps, conn);
		}
		
		return list;
	}
	
	
}
