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
		config.addDataSourceProperty("cachePreStmts", "true");//�Ƿ��Զ������ã�Ϊtrueʱ����������������Ч
		config.addDataSourceProperty("prepStmtCacheSize", "250");//���ӳش�СĬ��25���ٷ��Ƽ�250-500
		config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");//���������󳤶�Ĭ��256���ٷ��Ƽ�2048
		config.setReadOnly(false);//����ֻ�����ݿ�ʱ����Ϊtrue�� ��֤��ȫ
		config.setConnectionTimeout(40000);//�ȴ����ӳط������ӵ����ʱ�������룩���������ʱ����û���õ���������SQLException�� ȱʡ:30��
		config.setIdleTimeout(600000);//һ������idle״̬�����ʱ�������룩����ʱ���ͷţ�retired����ȱʡ:10����
		config.setMaxLifetime(1800000);//һ�����ӵ�����ʱ�������룩����ʱ����û��ʹ�����ͷţ�retired����ȱʡ:30���ӣ��������ñ����ݿⳬʱʱ����30��
		config.setMaximumPoolSize(15);//���ӳ�������������������ȱʡֵ��10���Ƽ��Ĺ�ʽ��((core_count * 2) + effective_spindle_count)
		
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
