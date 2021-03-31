package com.xnx3.okex.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.xnx3.DateUtil;
import com.xnx3.exception.NotReturnValueException;
import com.xnx3.okex.api.Trade;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

/**
 * 已成交的订单列表
 * @author 管雷鸣
 *
 */
public class FinishOrderListJframe extends JFrame {
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
					NotFinishOrderListJframe frame = new NotFinishOrderListJframe();
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
	public FinishOrderListJframe() {
		setTitle("已成交的订单列表");
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
		vName.add("单价");
		vName.add("交易数量");
		vName.add("买-卖");
		vName.add("创建时间");
		vName.add("成交时间");
		
		model = new DefaultTableModel(vData, vName){};
		table = new JTable();
		table.setModel(model);
		RowSorter<TableModel> rowSorter = new TableRowSorter<TableModel>(model);
        table.setRowSorter(rowSorter);
		
		scrollPane.setViewportView(table);
		
		loadJTableData();
		
		this.setVisible(true);
	}
	

	/**
	 * 加载表格数据
	 */
	private void loadJTableData(){
		JSONArray array = Trade.ordersHistory();
		
		model.setRowCount(0); //清除所有数据
		
		//加载楼号的数据
		for (int j = 0; j < array.size(); j++) {
			JSONObject item = array.getJSONObject(j);
			Vector vRow = new Vector();
			vRow.add(item.getString("instId"));
			vRow.add(item.getString("px"));
			vRow.add(item.getString("sz"));
			vRow.add(item.getString("side"));
			try {
				vRow.add(DateUtil.dateFormat(item.getLong("cTime"), "MM-dd HH:mm:ss"));
			} catch (NotReturnValueException e) {
				e.printStackTrace();
			}
			try {
				vRow.add(DateUtil.dateFormat(item.getLong("uTime"), "MM-dd HH:mm:ss"));
			} catch (NotReturnValueException e) {
				e.printStackTrace();
			}
			model.addRow(vRow);
		}
	}

	public JTable getTable() {
		return table;
	}
}
