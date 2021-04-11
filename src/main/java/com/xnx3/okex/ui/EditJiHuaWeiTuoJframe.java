package com.xnx3.okex.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.xnx3.media.TTSUtil;
import com.xnx3.okex.api.Market;
import com.xnx3.okex.api.Trade;
import com.xnx3.okex.bean.market.Book;
import com.xnx3.okex.bean.market.PriceNumber;
import com.xnx3.swing.DialogUtil;

import net.sf.json.JSONObject;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * 计划委托的设置
 */
public class EditJiHuaWeiTuoJframe extends JFrame {
	public EditJiHuaWeiTuoJframe() {
		setBounds(100, 100, 546, 315);
		setTitle("计划委托-自动委托");
		
		JLabel lblNewLabel = new JLabel("交易的币");
		
		instIdTextField = new JTextField();
		instIdTextField.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("填写格式如  BTC-USDT");
		
		JLabel lblNewLabel_2 = new JLabel("当价格");
		
		priceTextField = new JTextField();
		priceTextField.setColumns(10);
		
		JLabel lblNewLabel_3 = new JLabel("这个价时，以这个价");
		
		JLabel lblNewLabel_4 = new JLabel("执行操作");
		
		sideComboBox = new JComboBox();
		sideComboBox.setModel(new DefaultComboBoxModel(new String[] {"买入", "卖出"}));
		
		JLabel lblNewLabel_5 = new JLabel("数量");
		
		numberTextField = new JTextField();
		numberTextField.setColumns(10);
		
		JLabel lblNewLabel_6 = new JLabel("个");
		
		JButton saveBtnNewButton = new JButton("保存并启动");
		saveBtnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				new Thread(new Runnable() {
					public void run() {
						String instId = getInstIdTextField().getText();
						int dayuxiaoyu = dayuxiaoyuComboBox.getSelectedIndex();	// 0是低于，1是大于
						String side = sideComboBox.getSelectedIndex() == 0? "buy":"sell";
						double number = Double.parseDouble(numberTextField.getText());	//买入或卖出数量
						double price = Double.parseDouble(priceTextField.getText());	//单价
						
						if(side.equals("buy")){
							//买入
							
							//既然是买入，肯定是按照价格低了买，判断设置的是不是按照低于来设置的
							if(dayuxiaoyu == 1){
								DialogUtil.showMessageDialog("你设置的是自动买入，但你设置的是大于多少时买入！应该是小于多少时自动买入吧？");
								return;
							}
							
						}else{
							//卖出
							
							//既然是卖出，肯定是按照价格高了卖，判断设置的是不是按照高于来设置的
							if(dayuxiaoyu == 0){
								DialogUtil.showMessageDialog("你设置的是自动卖出，但你设置的是小于多少时卖出！应该是大于多少时自动卖出吧？");
								return;
							}
						}
						
						saveBtnNewButton.setEnabled(false);
						saveBtnNewButton.setText("执行中...要停止可结束软件");
						
						while(true){
							//拉取当前最新价格
							Book book = Market.books(instId, 5);
							
							if(side.equals("buy")){
								//买入
								//判断当前买的最高价
								PriceNumber pn = book.getBids().get(0);
								if(pn.getPrice() <= price){
									//价格合适，自动买入
									
									TTSUtil.speakByThread("自动委托，"+instId+"，价格:"+price+",已自动下单完毕");
									String orderId = Trade.createOrder(instId, side, number, price);
									if(orderId == null || orderId.length() < 2){
										//创建订单失败
									}else{
										//创建订单成功后，跟踪，15分钟内没有成交，那么自动撤销
										new Thread(new Runnable() {
											public void run() {
												try {
													Thread.sleep(15*60*1000);
												} catch (InterruptedException e) {
													e.printStackTrace();
												}
												
												//如果未成交自动取消委托
												JSONObject orderJson = Trade.order(instId, orderId);
												if(!orderJson.getString("state").equals("filled")){
													//只要没有完全成交，那都撤销订单
													Trade.cancelOrder(instId, orderId);
													TTSUtil.speakByThread(instId+"超时未成交，已自动撤销委托。单价："+price);
												}
											}
										}).start();
									}
									
									saveBtnNewButton.setText("已触发自动委托。");
									return;
								}
							}else{
								//卖出
								
							}
							
							
							//每3秒监控一次
							try {
								Thread.sleep(3*1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						
					}
				}).start();
				
			}
		});
		
		dayuxiaoyuComboBox = new JComboBox();
		dayuxiaoyuComboBox.setModel(new DefaultComboBoxModel(new String[] {"低于", "高于"}));
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(lblNewLabel_5, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(lblNewLabel_4, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(lblNewLabel_2, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(lblNewLabel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
						.addComponent(numberTextField, 0, 0, Short.MAX_VALUE)
						.addComponent(sideComboBox, 0, 102, Short.MAX_VALUE)
						.addComponent(dayuxiaoyuComboBox, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(instIdTextField, 0, 0, Short.MAX_VALUE))
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblNewLabel_6))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(6)
							.addComponent(priceTextField, GroupLayout.PREFERRED_SIZE, 106, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(lblNewLabel_3, GroupLayout.DEFAULT_SIZE, 143, Short.MAX_VALUE))
						.addGroup(groupLayout.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblNewLabel_1, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(52)
					.addComponent(saveBtnNewButton, GroupLayout.DEFAULT_SIZE, 375, Short.MAX_VALUE)
					.addGap(60))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel)
						.addComponent(instIdTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNewLabel_1, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_2)
						.addComponent(lblNewLabel_3)
						.addComponent(priceTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(dayuxiaoyuComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_4)
						.addComponent(sideComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_5)
						.addComponent(numberTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNewLabel_6, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED, 44, Short.MAX_VALUE)
					.addComponent(saveBtnNewButton, GroupLayout.PREFERRED_SIZE, 51, GroupLayout.PREFERRED_SIZE)
					.addGap(42))
		);
		getContentPane().setLayout(groupLayout);
	}

	private JPanel contentPane;
	private JTextField instIdTextField;
	private JTextField priceTextField;
	private JTextField numberTextField;
	private JComboBox dayuxiaoyuComboBox;
	private JComboBox sideComboBox;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					EditJiHuaWeiTuoJframe frame = new EditJiHuaWeiTuoJframe();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public JTextField getInstIdTextField() {
		return instIdTextField;
	}
	public JComboBox getDayuxiaoyuComboBox() {
		return dayuxiaoyuComboBox;
	}
	public JTextField getPriceTextField() {
		return priceTextField;
	}
	public JComboBox getSideComboBox() {
		return sideComboBox;
	}
	public JTextField getNumberTextField() {
		return numberTextField;
	}
}
