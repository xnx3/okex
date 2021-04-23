package com.xnx3.okex.ui;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import com.xnx3.okex.Global;
import com.xnx3.okex.action.PMA;
import com.xnx3.okex.api.Ticker;
import com.xnx3.okex.util.Log;
import com.xnx3.okex.util.SystemUtil;

import net.sf.json.JSONObject;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Cursor;
public class MainJframe extends JFrame {

	private JPanel contentPane;
	private JTextField pmaTextField;
	private JLabel jiedianLabel;

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
		setTitle("okex辅助");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 393, 501);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel label = new JLabel("当前请求节点");
		
		JPanel panel = new JPanel();
		
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
		
		JButton btnNewButton_1 = new JButton("搜索当前低价的币");
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
		
		JButton button_5 = new JButton("计划委托自动下单");
		button_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Global.jihuaweituoJframe.setVisible(true);
			}
		});
		
		JLabel label_1 = new JLabel("第一步：");
		
		JLabel label_2 = new JLabel("功能：");
		
		jiedianLabel = new JLabel("正在搜索可用节点...");
		
		JLabel lblNewLabel_1 = new JLabel("<html>\n说明：<br>\n当前只支持 BTC、USDT 为货币进行的交易。<br>\n交流论坛：  www.xxxxxx.com  欢迎进行沟通");
		lblNewLabel_1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblNewLabel_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//SystemUtil.openUrl("http://www.xxxxxx.com");
			}
		});
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.LEFT);
		
		JButton btnNewButton_2 = new JButton("暴跌自动买入卖出");
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(label, GroupLayout.PREFERRED_SIZE, 104, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(jiedianLabel, GroupLayout.PREFERRED_SIZE, 197, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(label_1)
								.addComponent(label_2))
							.addGap(23)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(btnOkex)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
										.addComponent(btnNewButton_2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(btnNewButton_1, 0, 0, Short.MAX_VALUE)
										.addComponent(button_5, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(button_4, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(button_2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(button_3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(button_1, GroupLayout.DEFAULT_SIZE, 171, Short.MAX_VALUE)
										.addComponent(btnNewButton, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
									.addGap(280)
									.addComponent(panel, GroupLayout.PREFERRED_SIZE, 394, GroupLayout.PREFERRED_SIZE))))
						.addComponent(lblNewLabel_1, GroupLayout.PREFERRED_SIZE, 374, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(label)
						.addComponent(jiedianLabel))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(btnOkex)
								.addComponent(label_1, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE))
							.addGap(30)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(label_2)
								.addComponent(button_1))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(button_3)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(button_2)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(button_4)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(button_5)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnNewButton)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnNewButton_1)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnNewButton_2)
							.addGap(25)
							.addComponent(lblNewLabel_1, GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE)
							.addContainerGap())
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(panel, GroupLayout.PREFERRED_SIZE, 132, GroupLayout.PREFERRED_SIZE)
							.addGap(86))))
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
		
		
		new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				getJiedianLabel().setText(Global.OKEX_DOMAIN);
			}
		}).start();
	}

	public JTextField getPmaTextField() {
		return pmaTextField;
	}
	public JLabel getJiedianLabel() {
		return jiedianLabel;
	}
}
