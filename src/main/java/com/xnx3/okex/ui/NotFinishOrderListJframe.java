package com.xnx3.okex.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
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
 * 未完成的订单列表，进行中的
 * @author 管雷鸣
 *
 */
public class NotFinishOrderListJframe extends JFrame {
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
	public NotFinishOrderListJframe() {
		setTitle("未成交的订单，进行中的订单");
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
		vName.add("订单ID");
		vName.add("币种");
		vName.add("单价");
		vName.add("交易数量");
		vName.add("买-卖");
		vName.add("创建时间");
		
		model = new DefaultTableModel(vData, vName){};
		table = new JTable();
		table.setModel(model);
		RowSorter<TableModel> rowSorter = new TableRowSorter<TableModel>(model);
        table.setRowSorter(rowSorter);
		
		scrollPane.setViewportView(table);
		
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent evt) {
				//通过点击位置找到点击为表格中的行
	            int focusedRowIndex = table.rowAtPoint(evt.getPoint());
	            if (focusedRowIndex == -1) {
	                return;
	            }
	            //将表格所选项设为当前右键点击的行
	            table.setRowSelectionInterval(focusedRowIndex, focusedRowIndex);
	            
	            String instId = table.getValueAt(focusedRowIndex, 1).toString();
	            String print = table.getValueAt(focusedRowIndex, 2).toString();
	            String ordId = table.getValueAt(focusedRowIndex, 0).toString();
	            String size = table.getValueAt(focusedRowIndex, 3).toString();	//交易数量
	            String side = table.getValueAt(focusedRowIndex, 4).toString();
	            
	            //弹出菜单
	            JPopupMenu menu = new JPopupMenu();
	            
	            //撤销委托
	            JMenuItem cancelMenItem = new JMenuItem();
	            cancelMenItem.setText(" 撤销委托"+instId+": "+print);
	            cancelMenItem.addActionListener(new java.awt.event.ActionListener() {
	                public void actionPerformed(java.awt.event.ActionEvent evt) {
	                    //该操作需要做的事
	                	if(Trade.cancelOrder("PMA-BTC", ordId)){
	                		//撤销成功，刷新
	                		loadJTableData();
	                	}
	                }
	            });
	            
	            //持续委托重复下单
	            JMenuItem repeatMenItem = new JMenuItem();
	            repeatMenItem.setText(" 重复下单 ");
	            repeatMenItem.addActionListener(new java.awt.event.ActionListener() {
	                public void actionPerformed(java.awt.event.ActionEvent evt) {
	                	ChixuWeituoJframe jframe = new ChixuWeituoJframe();
	                	jframe.instId = instId;
	                	jframe.ordId = ordId;
	                	jframe.price = Double.parseDouble(print);
	                	jframe.size = Double.parseDouble(size);
	                	jframe.side = side;
	                	jframe.setVisible(true);
	                	
	                	jframe.loadUIInfo();
	                }
	            });
	            
	            
	            menu.add(cancelMenItem);
	            menu.add(repeatMenItem);
	            menu.show(table, evt.getX(), evt.getY());
	            
			}
		});
		
		
		new Thread(new Runnable() {
			public void run() {
				while(true){
					loadJTableData();
					try {
						Thread.sleep(10*1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
		
		this.setVisible(true);
	}
	

	/**
	 * 加载表格数据
	 */
	private void loadJTableData(){
		JSONArray array = Trade.ordersPending();
		
		model.setRowCount(0); //清除所有数据
		
		//加载楼号的数据
		for (int j = 0; j < array.size(); j++) {
			JSONObject item = array.getJSONObject(j);
			Vector vRow = new Vector();
			vRow.add(item.getString("ordId"));
			vRow.add(item.getString("instId"));
			vRow.add(item.getString("px"));
			vRow.add(item.getString("sz"));
			vRow.add(item.getString("side").equalsIgnoreCase("buy")? "买入":"卖出");
			try {
				vRow.add(DateUtil.dateFormat(item.getLong("cTime"), "dd HH:mm:ss"));
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
