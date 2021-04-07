package com.xnx3.okex.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.xnx3.DateUtil;
import com.xnx3.okex.util.DB;
import com.xnx3.okex.util.DoubleUtil;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;

public class MoneyJframe extends JFrame {

	private JPanel contentPane;
	private JLabel dayProfitLabel;
	private JLabel shouxufeiLabel;
	private JLabel dayChengjiaoOrderNumberLabel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MoneyJframe frame = new MoneyJframe();
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
	public MoneyJframe() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 606, 610);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(67)
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 296, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(233, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(71)
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 189, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(318, Short.MAX_VALUE))
		);
		
		JLabel label = new JLabel("本日买卖支出：");
		
		dayProfitLabel = new JLabel("New label");
		
		JLabel label_1 = new JLabel("本日手续费消耗：");
		
		shouxufeiLabel = new JLabel("New label");
		
		JLabel label_2 = new JLabel("本日成交订单数：");
		
		JLabel label_3 = new JLabel("本日成交订单：");
		
		dayChengjiaoOrderNumberLabel = new JLabel("New label");
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING, false)
								.addComponent(label_2, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(label_1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(label, GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE))
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel.createSequentialGroup()
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(dayProfitLabel, GroupLayout.PREFERRED_SIZE, 156, GroupLayout.PREFERRED_SIZE))
								.addGroup(Alignment.TRAILING, gl_panel.createSequentialGroup()
									.addPreferredGap(ComponentPlacement.RELATED)
									.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
										.addComponent(dayChengjiaoOrderNumberLabel, GroupLayout.PREFERRED_SIZE, 156, GroupLayout.PREFERRED_SIZE)
										.addComponent(shouxufeiLabel, GroupLayout.PREFERRED_SIZE, 156, GroupLayout.PREFERRED_SIZE)))))
						.addComponent(label_3, GroupLayout.PREFERRED_SIZE, 113, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(15, Short.MAX_VALUE))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(label, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
						.addComponent(dayProfitLabel, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(label_1, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
						.addComponent(shouxufeiLabel, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(label_2, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(label_3, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE))
						.addComponent(dayChengjiaoOrderNumberLabel, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(57, Short.MAX_VALUE))
		);
		panel.setLayout(gl_panel);
		contentPane.setLayout(gl_contentPane);
		
		
		
		new Thread(new Runnable() {
			public void run() {
				while(true){
					load();
					try {
						Thread.sleep(10*1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	public JLabel getDayProfitLabel() {
		return dayProfitLabel;
	}
	public JLabel getShouxufeiLabel() {
		return shouxufeiLabel;
	}
	
	/**
	 * 加载数据
	 */
	public void load(){
		//今天0点的10位时间戳
		int dayZeroTime = DateUtil.getDateZeroTime(DateUtil.timeForUnix10());
		
		
		//今日手续费
		getShouxufeiLabel().setText(DoubleUtil.doubleSplit((double)(DB.getDatabase().getOneObject("SELECT sum(shouxufei) FROM \"order\" WHERE updateTime > "+dayZeroTime+"000")),5)+" USDT");
		
		//今日收益
		getDayProfitLabel().setText(DoubleUtil.doubleSplit((double)DB.getDatabase().getOneObject("SELECT sum(money) FROM \"order\" WHERE updateTime > "+dayZeroTime+"000"),5) + " USDT");
		
		//今日成交订单数
		getDayChengjiaoOrderNumberLabel().setText(DB.getDatabase().getOneObject("SELECT count(id) FROM \"order\" WHERE updateTime > "+dayZeroTime+"000")+"");
		
	}
	public JLabel getDayChengjiaoOrderNumberLabel() {
		return dayChengjiaoOrderNumberLabel;
	}
}
