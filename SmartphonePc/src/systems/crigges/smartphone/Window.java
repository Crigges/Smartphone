package systems.crigges.smartphone;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.UIManager;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.JScrollPane;
import java.awt.Component;
import javax.swing.Box;
import java.awt.Dimension;
import javax.swing.border.TitledBorder;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.border.BevelBorder;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.awt.event.ActionEvent;

public class Window implements ServerUI {

	private JFrame frmSmartphone;
	private JCheckBox chckbxUseBluetooth;
	private JCheckBox chckbxUseWirelessLan;
	private JPanel settingsPanel;
	private JLabel btNameStatus;
	private JLabel btAdapterStatus;
	private JLabel btAddressStatus;
	private JTextField wlanPortStatus;
	private JLabel wlanIpStatus;
	private JLabel portLabel;
	private JLabel ipLabel;
	private JLabel statusLabel;
	private JLabel addressLabel;
	private JLabel nameLabel;
	private JTextPane log;

	private Calendar cal = Calendar.getInstance();
	private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
	private Server server;
	private JLabel currentStatusLabel;
	private JButton btnStart;
	private JButton btnStop;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Throwable e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Window window = new Window();
					window.frmSmartphone.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Window() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmSmartphone = new JFrame();
		frmSmartphone.setTitle("SmartPHONE");
		frmSmartphone.setBounds(100, 100, 739, 403);
		frmSmartphone.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JMenuBar menuBar = new JMenuBar();
		menuBar.setBorderPainted(false);
		frmSmartphone.setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenu menu = new JMenu("?");
		menuBar.add(menu);

		btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				settingsPanel.setEnabled(false);
				btAdapterStatus.setEnabled(false);
				btAddressStatus.setEnabled(false);
				btNameStatus.setEnabled(false);
				wlanPortStatus.setEnabled(false);
				wlanIpStatus.setEnabled(false);
				chckbxUseBluetooth.setEnabled(false);
				chckbxUseWirelessLan.setEnabled(false);
				statusLabel.setEnabled(false);
				addressLabel.setEnabled(false);
				ipLabel.setEnabled(false);
				nameLabel.setEnabled(false);
				portLabel.setEnabled(false);
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						server.startServer();
					}
				}).start();
				
			}
		});

		btnStop = new JButton("Stop");
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				settingsPanel.setEnabled(true);
				btAdapterStatus.setEnabled(true);
				btAddressStatus.setEnabled(true);
				btNameStatus.setEnabled(true);
				wlanPortStatus.setEnabled(true);
				wlanIpStatus.setEnabled(true);
				chckbxUseBluetooth.setEnabled(true);
				chckbxUseWirelessLan.setEnabled(true);
				statusLabel.setEnabled(true);
				addressLabel.setEnabled(true);
				ipLabel.setEnabled(true);
				nameLabel.setEnabled(true);
				portLabel.setEnabled(true);
				server.stopServer();
			}
		});

		currentStatusLabel = new JLabel("Pending...");

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBorder(new TitledBorder(new LineBorder(new Color(128, 128, 128)), "Log", TitledBorder.LEADING,
				TitledBorder.TOP, null, new Color(0, 0, 0)));
		scrollPane.setViewportBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));

		settingsPanel = new JPanel();
		settingsPanel.setBorder(new TitledBorder(new LineBorder(new Color(128, 128, 128)), "Settings",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GroupLayout groupLayout = new GroupLayout(frmSmartphone.getContentPane());
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.TRAILING).addGroup(groupLayout
				.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout
						.createSequentialGroup().addGap(24)
						.addComponent(currentStatusLabel, GroupLayout.PREFERRED_SIZE, 126, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED, 347, Short.MAX_VALUE)
						.addComponent(btnStop, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(btnStart, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup().addContainerGap()
								.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
										.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 693, Short.MAX_VALUE)
										.addComponent(settingsPanel, GroupLayout.DEFAULT_SIZE, 693, Short.MAX_VALUE))))
				.addContainerGap()));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup().addGap(9)
						.addComponent(settingsPanel, GroupLayout.PREFERRED_SIZE, 137, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(btnStart)
								.addComponent(btnStop).addComponent(currentStatusLabel))
						.addContainerGap()));

		chckbxUseBluetooth = new JCheckBox("Use Bluetooth");
		chckbxUseBluetooth.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (chckbxUseBluetooth.isSelected() && chckbxUseWirelessLan.isSelected()) {
					chckbxUseWirelessLan.setSelected(false);
				}
				if (chckbxUseBluetooth.isSelected()) {
					server = new BluetoothServer(Window.this);
				}
			}
		});

		chckbxUseWirelessLan = new JCheckBox("Use Wireless LAN");
		chckbxUseWirelessLan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (chckbxUseBluetooth.isSelected() && chckbxUseWirelessLan.isSelected()) {
					chckbxUseBluetooth.setSelected(false);
				}
			}
		});

		statusLabel = new JLabel("Adapter Status:");

		addressLabel = new JLabel("Adapter Address:");

		nameLabel = new JLabel("Adapter Name:");

		btAdapterStatus = new JLabel("Unknown");

		btAddressStatus = new JLabel("Unknown");

		btNameStatus = new JLabel("Unknown");

		ipLabel = new JLabel("Local IP:");

		portLabel = new JLabel("Port:");

		wlanIpStatus = new JLabel("Unknown");

		wlanPortStatus = new JTextField();
		wlanPortStatus.setText("22223");
		wlanPortStatus.setColumns(10);
		GroupLayout gl_settingsPanel = new GroupLayout(settingsPanel);
		gl_settingsPanel
				.setHorizontalGroup(
						gl_settingsPanel.createParallelGroup(Alignment.LEADING).addGroup(gl_settingsPanel
								.createSequentialGroup().addContainerGap()
								.addGroup(gl_settingsPanel.createParallelGroup(Alignment.LEADING)
										.addComponent(chckbxUseBluetooth).addGroup(gl_settingsPanel
												.createSequentialGroup()
												.addGroup(gl_settingsPanel.createParallelGroup(Alignment.LEADING)
														.addComponent(statusLabel).addComponent(addressLabel)
														.addComponent(nameLabel))
												.addGap(40)
												.addGroup(gl_settingsPanel.createParallelGroup(Alignment.LEADING, false)
														.addComponent(btAddressStatus, GroupLayout.DEFAULT_SIZE, 111,
																Short.MAX_VALUE)
														.addComponent(btAdapterStatus, GroupLayout.DEFAULT_SIZE,
																GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
														.addComponent(btNameStatus, GroupLayout.DEFAULT_SIZE,
																GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
								.addPreferredGap(ComponentPlacement.RELATED, 61, Short.MAX_VALUE)
								.addGroup(gl_settingsPanel.createParallelGroup(Alignment.LEADING)
										.addComponent(chckbxUseWirelessLan)
										.addGroup(gl_settingsPanel.createSequentialGroup()
												.addGroup(gl_settingsPanel.createParallelGroup(Alignment.LEADING)
														.addComponent(ipLabel).addComponent(portLabel))
												.addGap(44)
												.addGroup(gl_settingsPanel.createParallelGroup(Alignment.LEADING)
														.addComponent(wlanPortStatus, GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
														.addComponent(wlanIpStatus))))
								.addContainerGap(109, Short.MAX_VALUE)));
		gl_settingsPanel.setVerticalGroup(gl_settingsPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_settingsPanel.createSequentialGroup()
						.addGroup(gl_settingsPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(chckbxUseBluetooth).addComponent(chckbxUseWirelessLan))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_settingsPanel.createParallelGroup(Alignment.BASELINE).addComponent(statusLabel)
								.addComponent(btAdapterStatus).addComponent(ipLabel).addComponent(wlanIpStatus))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_settingsPanel.createParallelGroup(Alignment.BASELINE).addComponent(addressLabel)
								.addComponent(btAddressStatus, GroupLayout.PREFERRED_SIZE, 21,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(portLabel).addComponent(wlanPortStatus, GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_settingsPanel.createParallelGroup(Alignment.BASELINE).addComponent(nameLabel)
								.addComponent(btNameStatus, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE))
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		settingsPanel.setLayout(gl_settingsPanel);

		log = new JTextPane();
		log.setBorder(null);
		log.setText("");
		scrollPane.setViewportView(log);
		frmSmartphone.getContentPane().setLayout(groupLayout);
	}

	public void log(String msg) {
		log.setText(log.getText() + "[" + sdf.format(cal.getTime()) + "] " + msg + System.lineSeparator());
	}

	@Override
	public void setDeviceStatus(boolean avail, String name, String address) {
		if (avail) {
			btAdapterStatus.setText("Available");
			btNameStatus.setText(name);
			btAddressStatus.setText(address);
		} else {
			btAdapterStatus.setText("Unavailable");
		}
	}

	@Override
	public void setStatus(Status s) {
		switch (s) {
		case Awaiting:
			btnStart.setEnabled(false);
			btnStop.setEnabled(true);
			currentStatusLabel.setText("Waiting for client...");
			break;
		case Connecting:
			btnStart.setEnabled(false);
			btnStop.setEnabled(true);
			currentStatusLabel.setText("Waiting for client...");
			break;
		case Pending:
			btnStart.setEnabled(false);
			btnStop.setEnabled(false);
			currentStatusLabel.setText("Settings pending");
			break;
		case Ready:
			btnStart.setEnabled(true);
			btnStop.setEnabled(false);
			currentStatusLabel.setText("Ready to start Server");
			break;
		case Running:
			btnStart.setEnabled(false);
			btnStop.setEnabled(true);
			currentStatusLabel.setText("Running...");
			break;
		default:
			break;
		}
	}
}
