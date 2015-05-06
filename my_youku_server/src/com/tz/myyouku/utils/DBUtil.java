package com.tz.myyouku.utils;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtil {
	private static String dbUrl = "jdbc:mysql://localhost:3306/myyouku";
	private static String dbUser = "root";
	private static String dbPwd = "root";

	static {
		try {
			// 加载MySQL数据库驱动程序
			Class.forName("com.mysql.jdbc.Driver");
		} catch (Exception e) {
			throw new ExceptionInInitializerError(e);
		}
	}

	public static Connection getConnection() throws SQLException {
		// 获得一个数据库连接
		return DriverManager.getConnection(dbUrl, dbUser, dbPwd);
	}

	/** 关闭数据库连接 */
	public static void freeDB(ResultSet rs, Statement st, Connection conn) {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (st != null) {
					st.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
