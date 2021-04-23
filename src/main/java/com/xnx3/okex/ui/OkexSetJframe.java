package com.xnx3.okex.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.xnx3.okex.Global;
import com.xnx3.okex.action.OkexSet;
import com.xnx3.okex.api.Account;
import com.xnx3.okex.util.Log;
import com.xnx3.swing.DialogUtil;

import net.sf.json.JSONObject;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Okex的一些参数设置
 * @author 管雷鸣
 *
 */
public class OkexSetJframe extends JFrame {

	private JPanel contentPane;
	private JTextField apiKeyTextField;
	private JTextField secretKeyTextField;
	private JTextField passphraseTextField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					OkexSetJframe frame = new OkexSetJframe();
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
	public OkexSetJframe() {
		setTitle("Okex参数设置");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 573, 270);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel lblNewLabel = new JLabel("Api Key");
		
		apiKeyTextField = new JTextField();
		apiKeyTextField.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Secret Key");
		
		secretKeyTextField = new JTextField();
		secretKeyTextField.setColumns(10);
		
		JLabel lblNewLabel_2 = new JLabel("Passphrase");
		
		passphraseTextField = new JTextField();
		passphraseTextField.setColumns(10);
		
		JButton btnNewButton = new JButton("保存");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				OkexSet.save(getApiKeyTextField().getText(), getSecretKeyTextField().getText(), getPassphraseTextField().getText());
				//自动加载数据到Global
				OkexSet.load();
				
				//判断是否准确，自动买一个 PMA
				JSONObject json = Account.balance();
				if(json.get("code") == null){
					Log.append("配置参数验证时异常！请检查三个参数是否准确");
					return;
				}
				if(!json.get("code").equals("0")){
					Log.append("配置参数验证时异常！请检查三个参数是否准确!OKEX响应："+json.toString());
					return;
				}
				
				DialogUtil.showMessageDialog("参数验证准确，已保存");
			}
		});
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
								.addComponent(lblNewLabel, GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE)
								.addComponent(lblNewLabel_1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(lblNewLabel_2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(apiKeyTextField, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 385, Short.MAX_VALUE)
								.addComponent(secretKeyTextField, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 385, Short.MAX_VALUE)
								.addComponent(passphraseTextField, GroupLayout.DEFAULT_SIZE, 385, Short.MAX_VALUE)))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(93)
							.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 311, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel)
						.addComponent(apiKeyTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_1)
						.addComponent(secretKeyTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_2)
						.addComponent(passphraseTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(28)
					.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(38, Short.MAX_VALUE))
		);
		contentPane.setLayout(gl_contentPane);
		
		//赋值
		apiKeyTextField.setText(Global.OK_ACCESS_KEY);
		secretKeyTextField.setText(Global.OK_ACCESS_SECRET_KEY);
		passphraseTextField.setText(Global.OK_ACCESS_PASSPHRASE);
	}

	public JTextField getSecretKeyTextField() {
		return secretKeyTextField;
	}
	public JTextField getPassphraseTextField() {
		return passphraseTextField;
	}
	public JTextField getApiKeyTextField() {
		return apiKeyTextField;
	}
}
