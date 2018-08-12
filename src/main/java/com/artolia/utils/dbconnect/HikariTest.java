package com.artolia.utils.dbconnect;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.Test;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class HikariTest {

	@Test
	public void test() {
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/marking?useSSL=false&serverTimezone=UTC");
		config.setUsername("root");
		config.setPassword("artolia");
		config.addDataSourceProperty("cachePreStmts", "true");//是否自定义配置，为true时下面两个参数才生效
		config.addDataSourceProperty("prepStmtCacheSize", "250");//连接池大小默认25，官方推荐250-500
		config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");//单条语句最大长度默认256，官方推荐2048
		config.setReadOnly(false);//连接只读数据库时配置为true， 保证安全
		config.setConnectionTimeout(40000);//等待连接池分配连接的最大时长（毫秒），超过这个时长还没可用的连接则发生SQLException， 缺省:30秒
		config.setIdleTimeout(600000);//一个连接idle状态的最大时长（毫秒），超时则被释放（retired），缺省:10分钟
		config.setMaxLifetime(1800000);//一个连接的生命时长（毫秒），超时而且没被使用则被释放（retired），缺省:30分钟，建议设置比数据库超时时长少30秒
		config.setMaximumPoolSize(15);//连接池中允许的最大连接数。缺省值：10；推荐的公式：((core_count * 2) + effective_spindle_count)
		
		HikariDataSource ds = new HikariDataSource(config);
		System.out.println(ds.getMaximumPoolSize());
		Connection con = null;
		try {
			con = ds.getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select * from score");
			while (rs.next()) {
				System.out.println(rs.getString("id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			ds.close();
		}
	}
	
}
