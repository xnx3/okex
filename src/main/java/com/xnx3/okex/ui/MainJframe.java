package com.xnx3.okex.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.xnx3.okex.Global;
import com.xnx3.okex.action.PMA;
import com.xnx3.okex.api.Ticker;
import com.xnx3.okex.util.Log;
import com.xnx3.swing.LogFrame;

import net.sf.json.JSONObject;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import javax.swing.JTextField;

public class MainJframe extends JFrame {

	private JPanel contentPane;
	private JComboBox requestEndpointComboBox;
	private JTextField pmaTextField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainJframe frame = new MainJframe();
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
	public MainJframe() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 391);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel label = new JLabel("请求节点");
		
		requestEndpointComboBox = new JComboBox();
		requestEndpointComboBox.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
			}
		});
		requestEndpointComboBox.setModel(new DefaultComboBoxModel(new String[] {"hk.okex.zvo.cn", "xinjiapo.okex.zvo.cn", "meiguo.okex.zvo.cn"}));
		
		JPanel panel = new JPanel();
		
		JButton button = new JButton("使用");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				switch (requestEndpointComboBox.getSelectedIndex()) {
				case 0:
					Global.OKEX_DOMAIN = "http://hk.okex.zvo.cn";
					break;
				case 1:
					Global.OKEX_DOMAIN = "http://xinjiapo.okex.zvo.cn";
				default:
					Global.OKEX_DOMAIN = "http://meiguo.okex.zvo.cn";
					break;
				}
			}
		});
		
		JButton button_1 = new JButton("未成交订单列表");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new NotFinishOrderListJframe().setVisible(true);
			}
		});
		
		JButton button_2 = new JButton("高差价币列表");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new PriceBodongJframe().setVisible(true);
			}
		});
		
		JButton button_3 = new JButton("已成交订单列表");
		button_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new FinishOrderListJframe().setVisible(true);
				
				
			}
		});
		
		JButton button_4 = new JButton("金额及统计");
		button_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new MoneyJframe().setVisible(true);
			}
		});
		
		JButton btnNewButton = new JButton("暴涨暴跌自动提醒");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new BaozhangBaodieJianceJframe().setVisible(true);
			}
		});
		
		JButton btnNewButton_1 = new JButton("自动搜索当前低价的币");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new SearchBuyDijiaBiJframe().setVisible(true);
			}
		});
		
		JButton btnOkex = new JButton("Okex参数设置");
		btnOkex.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new OkexSetJframe().setVisible(true);
			}
		});
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addComponent(label, GroupLayout.PREFERRED_SIZE, 69, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(requestEndpointComboBox, GroupLayout.PREFERRED_SIZE, 170, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(button))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(12)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(button_1)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(button_3))
								.addComponent(panel, GroupLayout.PREFERRED_SIZE, 394, GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGap(6)
									.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
										.addGroup(gl_contentPane.createSequentialGroup()
											.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 189, GroupLayout.PREFERRED_SIZE)
											.addGap(18)
											.addComponent(btnNewButton_1))
										.addGroup(gl_contentPane.createSequentialGroup()
											.addComponent(button_2)
											.addGap(18)
											.addComponent(button_4))))))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addComponent(btnOkex)))
					.addContainerGap(34, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(label)
						.addComponent(requestEndpointComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(button))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnOkex)
					.addGap(18)
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 132, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(button_1)
						.addComponent(button_3))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(button_2)
						.addComponent(button_4))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnNewButton)
						.addComponent(btnNewButton_1))
					.addContainerGap(22, Short.MAX_VALUE))
		);
		
		JButton btnBtm = new JButton("PMA_BTC 转 USDK");
		btnBtm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Log.log(PMA.btcToUsdk());
			}
		});
		
		pmaTextField = new JTextField();
		pmaTextField.setText("0.000000006");
		pmaTextField.setColumns(10);
		
		JButton btnusdk = new JButton("转usdk");
		btnusdk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				double d = Double.parseDouble(getPmaTextField().getText());
				JSONObject btc_usdt_json = Ticker.oneHangqing("BTC-USDK");
				Log.append(d+"btc --->  "+(d*btc_usdt_json.getDouble("askPx")));
			}
		});
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(6)
							.addComponent(pmaTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnusdk))
						.addComponent(btnBtm, GroupLayout.PREFERRED_SIZE, 195, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(129, Short.MAX_VALUE))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addComponent(btnBtm)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(pmaTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnusdk))
					.addContainerGap(65, Short.MAX_VALUE))
		);
		panel.setLayout(gl_panel);
		contentPane.setLayout(gl_contentPane);
	}
	public JComboBox getRequestEndpointComboBox() {
		return requestEndpointComboBox;
	}
	public JTextField getPmaTextField() {
		return pmaTextField;
	}
}
