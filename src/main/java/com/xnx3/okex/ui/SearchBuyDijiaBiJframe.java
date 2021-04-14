package com.xnx3.okex.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.PopupMenu;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.xnx3.okex.api.Ticker;
import com.xnx3.okex.suanfa.market.kLine.KLine;
import com.xnx3.okex.util.InstUtil;
import com.xnx3.okex.util.Log;

import net.sf.json.JSONArray;

import javax.swing.ComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JCheckBox;

/**
 * 搜索当前可以购买的低价货币
 * @author 管雷鸣
 *
 */
public class SearchBuyDijiaBiJframe extends JFrame {

	private JPanel contentPane;
	private JComboBox timeComboBox;
	public int min;	//分钟数，区间的分钟数
	public double baifenbi;	//低价的百分比，0.01~0.2
	private JComboBox baifenbiComboBox;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SearchBuyDijiaBiJframe frame = new SearchBuyDijiaBiJframe();
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
	public SearchBuyDijiaBiJframe() {
		setTitle("从全部的币中找当前时间价格最低的");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel lblNewLabel = new JLabel("时间区间");
		
		timeComboBox = new JComboBox();
		timeComboBox.setModel(new DefaultComboBoxModel(new String[] {"1分钟", "3分钟", "5分钟", "15分钟", "30分钟", "1小时", "2小时"}));
		
		JButton btnNewButton = new JButton("开始搜索");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//分钟数
				int index = timeComboBox.getSelectedIndex();
				switch (index) {
				case 0:
					min = 1;
					break;
				case 1:
					min = 3;
					break;
				case 2:
					min = 5;
					break;
				case 3:
					min = 15;
					break;
				case 4:
					min = 30;
					break;
				case 5:
					min = 60;
					break;
				case 6:
					min = 120;
					break;
				default:
					break;
				}
				
				//百分比
				double baifenbiIndex = getBaifenbiComboBox().getSelectedIndex()+1;
				baifenbi = baifenbiIndex/100;
				System.out.println(baifenbi);
				
				btnNewButton.setEnabled(false);
				btnNewButton.setText("搜索中...");
				
				new Thread(new Runnable() {
					public void run() {
						
						//1. 找到几分钟内，可买的，其内,存的是instId
						List<String> buyList = new ArrayList<String>();
						Log.append("自动搜索分析，以"+min+"分钟为一个区间(区块)，分析当前最近的100个区间里，当前的币的价格（当前区间），在这100个区间中，属于 "+((int)baifenbiIndex)+" 个最低价格区间之一的币种：");
						
						JSONArray allHangqing = Ticker.allHangqing();
						for (int i = 0; i < allHangqing.size(); i++) {
							String instId = allHangqing.getJSONObject(i).getString("instId");
							String moneyName = InstUtil.getPriceName(instId);
							if(moneyName.equals("USDK") || moneyName.equals("USDT") || moneyName.equals("BTC")){
								boolean find = KLine.isDigu(instId, min, baifenbi);
								if(find){
									buyList.add(instId);
									Log.append("低价币：\t"+instId);
								}
								try {
									Thread.sleep(200);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
						}
						Log.append("当前低价的币搜索结束，共搜索到"+buyList.size()+"个");
						
						
						btnNewButton.setEnabled(true);
						btnNewButton.setText("开始搜索");
					}
				}).start();
				
				
			}
		});
		
		JLabel lblNewLabel_1 = new JLabel("当前低价程度");
		
		baifenbiComboBox = new JComboBox();
		baifenbiComboBox.setModel(new DefaultComboBoxModel(new String[] {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20"}));
		
		JLabel lblNewLabel_2 = new JLabel("%");
		
		JLabel lblNewLabel_3 = new JLabel("<html>比如区间设置1分钟，当前低价程度设置5%，  那么点击搜索后，会以1分钟为一个区间，搜索最近100个区间，也就是最近100分钟内，价格曲线，如果当前的最新价格在这100分钟的价格曲线内，属于最低的5个价格之一，那么这个币就会被记录，扫描出来。");
		
		JLabel lblNewLabel_4 = new JLabel("忽略一路下跌");
		
		JCheckBox checkBox = new JCheckBox("忽略");
		checkBox.setSelected(true);
		checkBox.setEnabled(false);
		
		JLabel lblNewLabel_5 = new JLabel("<html>如果这个币的曲线成一路下跌状，那么风险大，忽略这个币，不值得买。");
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(61)
							.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 240, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblNewLabel_3, GroupLayout.DEFAULT_SIZE, 428, Short.MAX_VALUE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING, false)
								.addComponent(lblNewLabel_4, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(lblNewLabel_1, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(lblNewLabel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
										.addComponent(baifenbiComboBox, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(timeComboBox, 0, 91, Short.MAX_VALUE))
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(lblNewLabel_2, GroupLayout.DEFAULT_SIZE, 223, Short.MAX_VALUE))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(checkBox, GroupLayout.PREFERRED_SIZE, 73, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(lblNewLabel_5, GroupLayout.PREFERRED_SIZE, 226, GroupLayout.PREFERRED_SIZE)))))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel)
						.addComponent(timeComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_1)
						.addComponent(baifenbiComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNewLabel_2))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_4)
						.addComponent(checkBox)
						.addComponent(lblNewLabel_5))
					.addGap(26)
					.addComponent(lblNewLabel_3, GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		
		contentPane.setLayout(gl_contentPane);
		
		
	}
	public JComboBox getTimeComboBox() {
		return timeComboBox;
	}
	public JComboBox getBaifenbiComboBox() {
		return baifenbiComboBox;
	}
}
