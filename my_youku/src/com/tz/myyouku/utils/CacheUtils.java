package com.tz.myyouku.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import android.content.Context;

public class CacheUtils {

	private Context context;
	private String fileName;
	
	//首页缓存文件名称
	public static final String CACHE_FILE_HOMEPAGE = "hp_cache.txt";

	public CacheUtils(Context context, String fileName) {
		this.context = context;
		this.fileName = fileName;
	}

	/**
	 * 缓存
	 * 
	 * @param text
	 */
	public void save(String text) {
		try {
			FileOutputStream os = context.openFileOutput(fileName,
					Context.MODE_PRIVATE);
			os.write(text.getBytes());
			os.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 读取缓存
	 * @return
	 */
	public String load() {
		try {
			FileInputStream in = context.openFileInput(fileName);
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String text  = reader.readLine();
			reader.close();
			in.close();
			return text;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
