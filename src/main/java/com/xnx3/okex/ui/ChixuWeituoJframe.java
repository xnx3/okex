package com.xnx3.okex.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import javax.swing.border.TitledBorder;

import com.xnx3.media.TTSUtil;
import com.xnx3.okex.api.Market;
import com.xnx3.okex.api.Trade;
import com.xnx3.okex.bean.market.Book;
import com.xnx3.okex.bean.market.PriceNumber;
import com.xnx3.okex.util.DoubleUtil;
import com.xnx3.swing.DialogUtil;

import net.sf.json.JSONObject;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * 持续委托
 * @author 管雷鸣
 */
public class ChixuWeituoJframe extends JFrame {

	private JPanel contentPane;
	private JTextField sellTextField;
	private JTextField buyTextField;
	
	//下单的一些参数
	public String ordId;	//要持续下单的，当前订单的订单号
	public String instId;
	public double price;
	public double size;
	public String side;	//这里存的是  买入、 卖出  的文字
	public String sideApiStr;	//这里的是接口的 buy 、 sell
	
	private JTextField printTextField;
	private JTextField sizeTextField;
	private JLabel sideLabel;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChixuWeituoJframe frame = new ChixuWeituoJframe();
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
	public ChixuWeituoJframe() {
		setTitle("循环持续委托");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 508);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel lblNewLabel = new JLabel("卖价(最低价)");
		
		sellTextField = new JTextField();
		sellTextField.setColumns(10);
		
		JLabel label = new JLabel("买家(我之外的最高价)");
		
		buyTextField = new JTextField();
		buyTextField.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("<html>只要卖价、买价还是这个数，那么当这个订单成交后，程序就会自动再重复下一单此数量跟金额的\n<br>如果卖价、买价发生任何变动，数值跟设置的对不上了，那么就自动退出循环出价。\n<br>如果想中途停止，可直接终止此辅助。");
		
		JButton btnNewButton = new JButton("开始");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnNewButton.setEnabled(false);
				btnNewButton.setText("已开启持续委托模式");
				//开始重复下单，
				
				new Thread(new Runnable() {
					public void run() {
						while(true){
							/***** 首先判断自己的订单是否已成交 ****/
							JSONObject orderJson = Trade.order(instId, ordId);
							if(!orderJson.getString("state").equals("filled")){
								//如果订单还没有完全成交，那么退出循环，等待
								System.out.println("还未完全成交");
								try {
									Thread.sleep(300);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								continue;
							}
							
							
							/***** 首先读买卖深度是否符合要求 ******/
							//获取当前交易深度信息
							Book book = Market.books(instId, 4);
							//当前卖出的价格
							PriceNumber sellPriceNumber = book.getAsks().get(0);
							//当前买入的价格
							PriceNumber buyPriceNumber=  book.getBids().get(1);
							
							//判断买入卖出价格是否跟ui界面中的一致
							if(!DoubleUtil.doubleToString(sellPriceNumber.getPrice()).equals(sellTextField.getText())){
								//卖出价格已发生变动，持续委托结束
								TTSUtil.speakByThread("卖出价格已发生变动，持续委托结束");
								return;
							}
							if(!DoubleUtil.doubleToString(buyPriceNumber.getPrice()).equals(buyTextField.getText())){
								//买入价格已发生变动，持续委托结束
								TTSUtil.speakByThread("买入价格已发生变动，持续委托结束");
								return;
							}
							
							//进行重复下单
							System.out.println("重复下单");
							ordId = Trade.order(instId, sideApiStr, size, price);
							if(ordId == null || ordId.length() == 0){
								TTSUtil.speakByThread("持续委托自动创建委托失败，持续委托结束");
								return;
							}else{
								TTSUtil.speakByThread("持续委托自动创建订单成功");
							}
							
						}
					}
				}).start();
				
				
				
			}
		});
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "\u91CD\u590D\u4E0B\u5355\u7684\u4FE1\u606F", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(label, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(lblNewLabel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
						.addComponent(buyTextField)
						.addComponent(sellTextField, GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE))
					.addContainerGap(86, Short.MAX_VALUE))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(76)
					.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 270, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(94, Short.MAX_VALUE))
				.addComponent(lblNewLabel_1, GroupLayout.DEFAULT_SIZE, 440, Short.MAX_VALUE)
				.addComponent(panel, GroupLayout.DEFAULT_SIZE, 440, Short.MAX_VALUE)
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
						.addComponent(sellTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(label)
						.addComponent(buyTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addComponent(lblNewLabel_1, GroupLayout.PREFERRED_SIZE, 116, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel, GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE)
					.addGap(18)
					.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		
		JLabel lblNewLabel_2 = new JLabel("金额");
		
		printTextField = new JTextField();
		printTextField.setColumns(10);
		
		JLabel label_1 = new JLabel("数量");
		
		sizeTextField = new JTextField();
		sizeTextField.setColumns(10);
		
		sideLabel = new JLabel("买入还是卖出");
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(label_1, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(lblNewLabel_2, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(sideLabel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(printTextField, Alignment.LEADING)
						.addComponent(sizeTextField, Alignment.LEADING))
					.addContainerGap(210, Short.MAX_VALUE))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_2)
						.addComponent(printTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(label_1)
						.addComponent(sizeTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addComponent(sideLabel, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(45, Short.MAX_VALUE))
		);
		panel.setLayout(gl_panel);
		contentPane.setLayout(gl_contentPane);
		
	}
	
	//加载界面信息
	public void loadUIInfo(){
		getPrintTextField().setText(DoubleUtil.doubleToString(this.price));
		getSizeTextField().setText(DoubleUtil.doubleToString(this.size));
		this.sideLabel.setText(this.side);
		
		//获取当前交易深度信息
		Book book = Market.books(this.instId, 4);
		
		//判断当前是买还是卖出
		if(this.side.equals("卖出")){
			sideApiStr = "sell";
		}else if(this.side.equals("买入")){
			sideApiStr = "buy";
		}else{
			DialogUtil.showMessageDialog("出错，未发现当前订单是买入还是卖出的");
			return;
		}
		
		//当前卖出的价格
		PriceNumber sellPriceNumber = null;
		if(sideApiStr.equals("sell")){
			//卖出，那么第一条应该是自己的，第二条才是别人的
			//判断第一条是不是自己的
			if(!(Math.abs(DoubleUtil.subtract(book.getAsks().get(0).getPrice(), this.price)) < 0.000000000000000001)){
				//不是自己的
				DialogUtil.showMessageDialog("出错，您当前的卖出的委托不再第一条");
				return;
			}
			sellPriceNumber = book.getAsks().get(1);
		}else{
			sellPriceNumber = book.getAsks().get(0);
		}
		
		//当前买入的价格
		PriceNumber buyPriceNumber = null;
		if(sideApiStr.equals("buy")){
			//取第二条，因为第一条是自己的
			if(!(Math.abs(DoubleUtil.subtract(book.getBids().get(0).getPrice(), this.price)) < 0.000000000000000001)){
				//不是自己的
				DialogUtil.showMessageDialog("出错，您当前的买入的委托不再第一条");
				return;
			}
			buyPriceNumber = book.getBids().get(1);
		}else{
			buyPriceNumber = book.getBids().get(0);
		}
				
		this.buyTextField.setText(DoubleUtil.doubleToString(buyPriceNumber.getPrice()));
		this.sellTextField.setText(DoubleUtil.doubleToString(sellPriceNumber.getPrice()));
		
	}
	
	public JTextField getPrintTextField() {
		return printTextField;
	}
	public JTextField getSizeTextField() {
		return sizeTextField;
	}
}
