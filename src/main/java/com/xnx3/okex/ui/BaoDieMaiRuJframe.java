package com.xnx3.okex.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.xnx3.okex.action.BaoDieMaiRu;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * 暴跌自动买入卖出
 * @author 管雷鸣
 *
 */
public class BaoDieMaiRuJframe extends JFrame {

	private JPanel contentPane;
	private JButton runButton;
	private JComboBox chenggongjilvComboBox;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BaoDieMaiRuJframe frame = new BaoDieMaiRuJframe();
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
	public BaoDieMaiRuJframe() {
		setTitle("暴跌自动买入卖出");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel lblNewLabel = new JLabel("稳赚的成功几率");
		
		chenggongjilvComboBox = new JComboBox();
		chenggongjilvComboBox.setModel(new DefaultComboBoxModel(new String[] {"98%", "85%", "70%"}));
		
		JLabel lblNewLabel_1 = new JLabel("<html>当遇到暴跌，自动买入，如果买入成功，自动以当前价格+1.5%的利润差价委托卖出。\n<br/>成功几率越高，买入的次数也会越少，比如 98% 几率，可能好几天才能自动成交一单； 70%几率的，可能一天能成交个十来单。\n<br/>注意，要关闭请直接将软件关掉、另外如果关掉软件了，自己去看一下OKEX里面当前有没有正在买入的委托");
		
		runButton = new JButton("运行");
		runButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int index = chenggongjilvComboBox.getSelectedIndex();
				System.out.println(index);
				// 0:98%;  1:85%;   2:70%
				int kTime = 1;
				switch (index) {
				case 0:
					kTime = 1440;
					break;
				case 2:
					kTime = 30;
					break;
				default:
					//默认1分钟
					kTime = 1;
					break;
				}
				
				BaoDieMaiRu.run(kTime);
				
				
				runButton.setText("正在运行中..要关闭直接关掉软件");
				runButton.setEnabled(false);
			}
		});
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(runButton, GroupLayout.PREFERRED_SIZE, 344, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(96, Short.MAX_VALUE))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(lblNewLabel_1, GroupLayout.DEFAULT_SIZE, 434, Short.MAX_VALUE)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 112, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(chenggongjilvComboBox, GroupLayout.PREFERRED_SIZE, 136, GroupLayout.PREFERRED_SIZE)
							.addContainerGap(180, Short.MAX_VALUE))))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel)
						.addComponent(chenggongjilvComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(30)
					.addComponent(lblNewLabel_1, GroupLayout.PREFERRED_SIZE, 107, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(runButton, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(40, Short.MAX_VALUE))
		);
		contentPane.setLayout(gl_contentPane);
	}
	public JButton getRunButton() {
		return runButton;
	}
	public JComboBox getChenggongjilvComboBox() {
		return chenggongjilvComboBox;
	}
}
