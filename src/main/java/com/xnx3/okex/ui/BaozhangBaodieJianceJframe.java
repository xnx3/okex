package com.xnx3.okex.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.xnx3.okex.action.BaozhangBaodie;
import com.xnx3.swing.DialogUtil;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * 暴涨暴跌自动检测
 * @author 管雷鸣
 *
 */
public class BaozhangBaodieJianceJframe extends JFrame {

	private JPanel contentPane;
	private JTextField instIdTextField;
	private JTextField baifenbiTextField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BaozhangBaodieJianceJframe frame = new BaozhangBaodieJianceJframe();
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
	public BaozhangBaodieJianceJframe() {
		setTitle("暴涨暴跌自动检测提醒");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel lblNewLabel = new JLabel("检测的币种");
		
		instIdTextField = new JTextField();
		instIdTextField.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("涨跌提醒幅度");
		
		baifenbiTextField = new JTextField();
		baifenbiTextField.setColumns(10);
		
		JLabel lblNewLabel_2 = new JLabel("填写格式如 BTC-USDT");
		
		JLabel lblNewLabel_3 = new JLabel("填写如 0.02 则代表2%");
		
		JButton btnNewButton = new JButton("开启自动检测提醒");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				double baifenbi = Double.parseDouble(getBaifenbiTextField().getText());
				if(baifenbi < 0.000001){
					DialogUtil.showMessageDialog("请传入涨跌幅度的百分比，传入 0.01 ~ 0.99 ，当然你也可以填写 0.001 ");
					return;
				}
				btnNewButton.setEnabled(false);
				btnNewButton.setText("运行中...要停止直接关闭软件");
				new Thread(new Runnable() {
					public void run() {
						BaozhangBaodie jiance = new BaozhangBaodie();
						while(true){
							jiance.check(getInstIdTextField().getText(), baifenbi);
							
							try {
								//每5秒检测一次
								Thread.sleep(3000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				}).start();
			}
		});
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(lblNewLabel_1, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)
									.addGap(18)
									.addComponent(baifenbiTextField, GroupLayout.PREFERRED_SIZE, 130, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(lblNewLabel_3, GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 109, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(instIdTextField, GroupLayout.PREFERRED_SIZE, 124, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(lblNewLabel_2, GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE))))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(26)
							.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 379, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel)
						.addComponent(instIdTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNewLabel_2))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_1)
						.addComponent(baifenbiTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNewLabel_3))
					.addGap(29)
					.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 57, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(112, Short.MAX_VALUE))
		);
		contentPane.setLayout(gl_contentPane);
	}
	public JTextField getInstIdTextField() {
		return instIdTextField;
	}
	public JTextField getBaifenbiTextField() {
		return baifenbiTextField;
	}
}
