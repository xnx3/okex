package com.xnx3.okex.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.List;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.xnx3.DateUtil;
import com.xnx3.exception.NotReturnValueException;
import com.xnx3.okex.action.JiHuaWeiTuo;
import com.xnx3.okex.api.Public;
import com.xnx3.okex.bean.trade.Jihuaweituo;
import com.xnx3.okex.bean.trade.Order;
import com.xnx3.okex.util.DB;
import com.xnx3.okex.util.DoubleUtil;
import com.xnx3.okex.util.Log;
import com.xnx3.swing.DialogUtil;

import javax.swing.JScrollPane;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JTable;
import javax.swing.RowSorter;

import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;

/**
 * 计划委托
 * @author 管雷鸣
 *
 */
public class JiHuaWeiTuoJframe extends JFrame {
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
					JiHuaWeiTuoJframe frame = new JiHuaWeiTuoJframe();
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
	public JiHuaWeiTuoJframe() {
		setTitle("计划委托");
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JScrollPane scrollPane = new JScrollPane();
		
		JButton btnNewButton = new JButton("添加一个计划委托");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new EditJiHuaWeiTuoJframe().setVisible(true);
			}
		});
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 440, Short.MAX_VALUE)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 166, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(274, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 233, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnNewButton))
		);
		
		table = new JTable();
		scrollPane.setViewportView(table);
		contentPane.setLayout(gl_contentPane);
		
		
		Vector vData = new Vector();
		Vector vName = new Vector();
		vName.add("ID"); //ID唯一编号，只是当前软件的id而已
		vName.add("币种");
		vName.add("买卖");
		vName.add("单价");
		vName.add("数量");
		vName.add("运行有效期至");
		vName.add("当前状态");
		
		model = new DefaultTableModel(vData, vName){};
		table.setModel(model);
		RowSorter<TableModel> rowSorter = new TableRowSorter<TableModel>(model);
        table.setRowSorter(rowSorter);
		
        //隐藏第一列id
  		table.getColumnModel().getColumn(0).setMaxWidth(1);
  		table.getColumnModel().getColumn(0).setMinWidth(1);
  		table.getTableHeader().getColumnModel().getColumn(0).setMaxWidth(2);
  		table.getTableHeader().getColumnModel().getColumn(0).setMinWidth(1);
  		
  		//不可整列移动,避免 setValue 出错
  		table.getTableHeader().setReorderingAllowed(false);
        
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
	            
	            String id = table.getValueAt(focusedRowIndex, 0).toString();
	            String instId = table.getValueAt(focusedRowIndex, 1).toString();
	            String side = table.getValueAt(focusedRowIndex, 2).toString().equals("买入")? "buy":"sell";
	            double print = Double.parseDouble(table.getValueAt(focusedRowIndex, 3).toString());
	            double size = Double.parseDouble(table.getValueAt(focusedRowIndex, 4).toString());	//交易数量
	            
	            int runstate = table.getValueAt(focusedRowIndex, 6).toString().equals("运行")? 1:0;
	            
	            
	            //弹出菜单
	            JPopupMenu menu = new JPopupMenu();
	            
	            JMenuItem runMenItem = new JMenuItem();
	            runMenItem.setText("运行");
	            runMenItem.addActionListener(new java.awt.event.ActionListener() {
	                public void actionPerformed(java.awt.event.ActionEvent evt) {
	                    //该操作需要做的事
	                	Jihuaweituo weituo = JiHuaWeiTuo.getJihuaweituoById(id);
	                	if(weituo == null){
	                		DialogUtil.showMessageDialog("计划委托id不存在");
	                		return;
	                	}
	                	
	                	JiHuaWeiTuo.runThread(weituo);
	                	
	                	Log.append("开启计划委托 "+instId+",  "+(side.equals("buy")? "买入":"卖出")+", 单价:"+table.getValueAt(focusedRowIndex, 3).toString());
	                	
	                	//设置此委托为已启动
	                	DB.getDatabase().update("update jihuaweituo set runstate = 1 WHERE id = '"+weituo.getId()+"'");
	                	//刷新table
	                	loadJTableData();
	                	
	                }
	            });
	            if(runstate == 0){
	            	//如果是未开启，才会有开启功能
	            	menu.add(runMenItem);
	            }
	            
	            JMenuItem editMenItem = new JMenuItem();
	            editMenItem.setText("编辑");
	            editMenItem.addActionListener(new java.awt.event.ActionListener() {
	                public void actionPerformed(java.awt.event.ActionEvent evt) {
	                    //该操作需要做的事
	                	EditJiHuaWeiTuoJframe edit = new EditJiHuaWeiTuoJframe();
	                	edit.jihuaweituoId = id;
	                	
	            		//根据 jihuaweituo.id 加载数据表的数据。如果这个id有值，是修改的话
            			Jihuaweituo weituo = JiHuaWeiTuo.getJihuaweituoById(id);
            			edit.getInstIdTextField().setText(weituo.getInstId());
            			edit.getPriceTextField().setText(DoubleUtil.doubleToString(weituo.getPrice()));
            			edit.getNumberTextField().setText(DoubleUtil.doubleToString(weituo.getSize()));
            			edit.getDayuxiaoyuComboBox().setSelectedIndex(weituo.getSide().equals("buy")? 0:1);
	                	
	                	
	                	edit.setVisible(true);
	                }
	            });
	            menu.add(editMenItem);
	            
	            
	            
	            
	            menu.show(table, evt.getX(), evt.getY());
	            
			}
		});
		
		loadJTableData();
	}
	
	

	/**
	 * 重新加载表格数据
	 */
	public void loadJTableData(){
		model.setRowCount(0); //清除所有数据
		List<Jihuaweituo> list = DB.getDatabase().select(Jihuaweituo.class, "ORDER BY instId ASC"); 
		for (int i = 0; i < list.size(); i++) {
			Jihuaweituo weituo = list.get(i);
			 Vector vRow = new Vector();
			 vRow.add(weituo.getId());
			 vRow.add(weituo.getInstId());
			vRow.add(weituo.getSide().equals("buy")? "买入":"卖出");
			vRow.add(DoubleUtil.doubleToString(weituo.getPrice()));
			vRow.add(DoubleUtil.doubleToString(weituo.getSize()));
			try {
				vRow.add(DateUtil.dateFormat(weituo.getValidtime(), "MM-dd hh:mm"));
			} catch (NotReturnValueException e) {
				e.printStackTrace();
			}
			vRow.add(weituo.getRunstate() == 1 ? "运行":"停止");
			
			model.addRow(vRow);
		}
	}

}
