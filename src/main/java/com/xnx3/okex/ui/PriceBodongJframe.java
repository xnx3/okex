package com.xnx3.okex.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.xnx3.DateUtil;
import com.xnx3.exception.NotReturnValueException;
import com.xnx3.okex.api.Ticker;
import com.xnx3.okex.api.Trade;
import com.xnx3.okex.util.InstUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

/**
 * 所有币里面，价格波动最大，买卖利润最大的几个
 * @author 管雷鸣
 *
 */
public class PriceBodongJframe extends JFrame {
	public DefaultTableModel model;
	private JPanel contentPane;
	private JTable table;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PriceBodongJframe frame = new PriceBodongJframe();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public PriceBodongJframe() {
		setTitle("所有货币买卖波动情况");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JScrollPane scrollPane = new JScrollPane();
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 440, Short.MAX_VALUE)
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 268, Short.MAX_VALUE)
		);
		contentPane.setLayout(gl_contentPane);
		
		Vector vData = new Vector();
		Vector vName = new Vector();
		vName.add("币种");
		vName.add("卖价");
		vName.add("买价");
		vName.add("利润率");
		vName.add("24h交易量");
		vName.add("24h交易货币");
		
		model = new DefaultTableModel(vData, vName){};
		table = new JTable();
		table.setModel(model);
		
		scrollPane.setViewportView(table);
		
		loadJTableData();
		
		this.setVisible(true);
		
		
		new Thread(new Runnable() {
			public void run() {
				while(true){
					try {
						loadJTableData();
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						//30秒刷新一次数据
						Thread.sleep(30*1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	

	/**
	 * 加载表格数据
	 */
	private void loadJTableData(){
		JSONArray array = Ticker.liruncha();
		System.out.println("arraL:"+array);
		model.setRowCount(0); //清除所有数据
		System.out.println(array.size());
		
		//加载楼号的数据
		for (int j = 0; j < array.size(); j++) {
			JSONObject item = array.getJSONObject(j);
			Vector vRow = new Vector();
			vRow.add(item.getString("instId"));
			vRow.add(item.getString("askPx"));
			vRow.add(item.getString("bidPx"));
			vRow.add((int)(item.getDouble("lirunbaifenbi")*100)+"%");
			vRow.add(item.getString("vol24h")+" 个");
			vRow.add(item.getString("volCcy24h")+" "+InstUtil.getPriceName(item.getString("instId")));
			model.addRow(vRow);
			
			System.out.println(item);
		}
	}

	public JTable getTable() {
		return table;
	}
}
