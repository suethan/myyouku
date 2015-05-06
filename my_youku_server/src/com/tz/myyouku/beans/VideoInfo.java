package com.tz.myyouku.beans;

/**
 * 影片信息
 * 
 * @author Jason
 * @date 2015-4-26
 */
public class VideoInfo {

	private int id;
	// 标题
	private String title;
	// 描述
	private String desc;
	// 播放的次数
	private String playTimes;
	// 缩略图路径
	private String imgUrl;
	// 影片路径
	private String videoUrl;

	public VideoInfo() {
	}

	public VideoInfo(int id, String title, String desc, String playTimes,
			String imgUrl, String videoUrl) {
		super();
		this.id = id;
		this.title = title;
		this.desc = desc;
		this.playTimes = playTimes;
		this.imgUrl = imgUrl;
		this.videoUrl = videoUrl;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getPlayTimes() {
		return playTimes;
	}

	public void setPlayTimes(String playTimes) {
		this.playTimes = playTimes;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

	@Override
	public String toString() {
		return "VideoInfo [id=" + id + ", title=" + title + ", desc=" + desc
				+ ", playTimes=" + playTimes + ", imgUrl=" + imgUrl
				+ ", videoUrl=" + videoUrl + "]";
	}

}
