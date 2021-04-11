package com.xnx3.okex.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
import com.xnx3.okex.api.Public;
import com.xnx3.okex.api.Trade;
import com.xnx3.okex.bean.trade.Order;
import com.xnx3.okex.thread.FinishOrder;
import com.xnx3.okex.util.DB;
import com.xnx3.okex.util.DoubleUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
					FinishOrderListJframe frame = new FinishOrderListJframe();
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
		vName.add("金额(USDT)");
		vName.add("创建时间");
		vName.add("成交时间");
		
		model = new DefaultTableModel(vData, vName){};
		table = new JTable();
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
	            
	            String instId = table.getValueAt(focusedRowIndex, 0).toString();
	            String print = table.getValueAt(focusedRowIndex, 1).toString();
	            //将单价转化为Double，在转化为string
	            print = DoubleUtil.doubleToString(Double.parseDouble(print));
	            
	            String size = table.getValueAt(focusedRowIndex, 2).toString();	//交易数量
	            //将
	            
	            //弹出菜单
	            JPopupMenu menu = new JPopupMenu();
	            JMenuItem delMenItem = new JMenuItem();
	            delMenItem.setText("计划委托");
	            delMenItem.addActionListener(new java.awt.event.ActionListener() {
	                public void actionPerformed(java.awt.event.ActionEvent evt) {
	                    //该操作需要做的事
	                	EditJiHuaWeiTuoJframe jihua = new EditJiHuaWeiTuoJframe();
	                	
	                	//instid
	                	String instId = table.getValueAt(focusedRowIndex, 0).toString();
	                	jihua.getInstIdTextField().setText(instId);
	                	
	                	//价格
	                	String print = table.getValueAt(focusedRowIndex, 1).toString();
	    	            //将单价转化为Double，在转化为string
	    	            print = DoubleUtil.doubleToString(Double.parseDouble(print));
	                	jihua.getPriceTextField().setText(print);
	                	
	                	//最小买入数量
	                	jihua.getNumberTextField().setText(DoubleUtil.doubleToString(Public.getInstrument(instId).getMinSize()));
	                	
	                	jihua.setVisible(true);
	                }
	            });
	            menu.add(delMenItem);
	            menu.show(table, evt.getX(), evt.getY());
	            
			}
		});
		table.setModel(model);
		RowSorter<TableModel> rowSorter = new TableRowSorter<TableModel>(model);
        table.setRowSorter(rowSorter);
		
		scrollPane.setViewportView(table);
		
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
//		JSONArray array = Trade.ordersHistory();
		model.setRowCount(0); //清除所有数据
		List<Order> list = DB.getDatabase().select(Order.class, "ORDER BY updateTime DESC LIMIT 300"); 
		
		for (int i = 0; i < list.size(); i++) {
			 Order order = list.get(i);
			 Vector vRow = new Vector();
			vRow.add(order.getInstId());
			vRow.add(order.getPrice());
			vRow.add(order.getSize());
			vRow.add(order.getSide());
			vRow.add(DoubleUtil.doubleToString(DoubleUtil.doubleSplit(order.getMoney(), 6)));
			try {
				vRow.add(DateUtil.dateFormat(order.getCreateTime(), "MM-dd HH:mm:ss"));
			} catch (NotReturnValueException e) {
				e.printStackTrace();
			}
			try {
				vRow.add(DateUtil.dateFormat(order.getUpdateTime(), "MM-dd HH:mm:ss"));
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
