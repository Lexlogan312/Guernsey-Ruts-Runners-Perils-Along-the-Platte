/*
 * Created by JFormDesigner on Tue Apr 15 13:31:59 EDT 2025
 */

import java.awt.*;
import javax.swing.*;

/**
 * @author alexrandall
 */
public class GUI extends JPanel {
    public GUI() {
        initComponents();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
		// Generated using JFormDesigner Evaluation license - Alex Randall
		label1 = new JLabel();
		travelButton = new JButton();
		restButton = new JButton();
		huntButton = new JButton();
		inventoryButton = new JButton();
		historyButton = new JButton();
		quitButton = new JButton();
		outputTextScrollPane = new JScrollPane();
		outputTextArea = new JTextArea();
		mapPanel = new JPanel();
		dateTextField = new JTextField();
		daysTextField = new JTextField();
		locationTextField = new JTextField();
		distanceTextField = new JTextField();
		nextLandmarkTextField = new JTextField();
		weatherTextField = new JTextField();
		healthTextField = new JTextField();
		foodTextField = new JTextField();
		oxenHealthTextField = new JTextField();
		oxenHealthTextFieldData = new JTextField();
		foodTextFieldData = new JTextField();
		weatherTextFieldData = new JTextField();
		healthTextFieldData = new JTextField();
		nextLandmarkTextFieldData = new JTextField();
		distanceTextFieldData = new JTextField();
		locationTextFieldData = new JTextField();
		daysTextFieldData = new JTextField();
		dateTextFieldData = new JTextField();

		//======== this ========
		setBorder ( new javax . swing. border .CompoundBorder ( new javax . swing. border .TitledBorder ( new javax . swing.
		border .EmptyBorder ( 0, 0 ,0 , 0) ,  "JF\u006frmDes\u0069gner \u0045valua\u0074ion" , javax. swing .border . TitledBorder. CENTER
		,javax . swing. border .TitledBorder . BOTTOM, new java. awt .Font ( "D\u0069alog", java .awt . Font
		. BOLD ,12 ) ,java . awt. Color .red ) , getBorder () ) );  addPropertyChangeListener(
		new java. beans .PropertyChangeListener ( ){ @Override public void propertyChange (java . beans. PropertyChangeEvent e) { if( "\u0062order"
		.equals ( e. getPropertyName () ) )throw new RuntimeException( ) ;} } );
		setLayout(null);

		//---- label1 ----
		label1.setText("Perils Along the Platte");
		label1.setHorizontalAlignment(SwingConstants.CENTER);
		label1.setFont(label1.getFont().deriveFont(label1.getFont().getStyle() | Font.BOLD, label1.getFont().getSize() + 7f));
		add(label1);
		label1.setBounds(220, 5, 730, 36);

		//---- travelButton ----
		travelButton.setText("Travel");
		add(travelButton);
		travelButton.setBounds(940, 145, 112, 35);

		//---- restButton ----
		restButton.setText("Rest");
		add(restButton);
		restButton.setBounds(940, 190, 112, 35);

		//---- huntButton ----
		huntButton.setText("Hunt");
		add(huntButton);
		huntButton.setBounds(940, 235, 112, 35);

		//---- inventoryButton ----
		inventoryButton.setText("Inventory");
		add(inventoryButton);
		inventoryButton.setBounds(940, 280, 112, 35);

		//---- historyButton ----
		historyButton.setText("History");
		add(historyButton);
		historyButton.setBounds(940, 325, 112, 35);

		//---- quitButton ----
		quitButton.setText("Quit");
		add(quitButton);
		quitButton.setBounds(940, 370, 112, 35);

		//======== outputTextScrollPane ========
		{
			outputTextScrollPane.setViewportView(outputTextArea);
		}
		add(outputTextScrollPane);
		outputTextScrollPane.setBounds(10, 450, 920, 170);

		//======== mapPanel ========
		{
			mapPanel.setBackground(new Color(0x996600));
			mapPanel.setLayout(null);

			{
				// compute preferred size
				Dimension preferredSize = new Dimension();
				for(int i = 0; i < mapPanel.getComponentCount(); i++) {
					Rectangle bounds = mapPanel.getComponent(i).getBounds();
					preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
					preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
				}
				Insets insets = mapPanel.getInsets();
				preferredSize.width += insets.right;
				preferredSize.height += insets.bottom;
				mapPanel.setMinimumSize(preferredSize);
				mapPanel.setPreferredSize(preferredSize);
			}
		}
		add(mapPanel);
		mapPanel.setBounds(10, 125, 920, 315);

		//---- dateTextField ----
		dateTextField.setText("Date");
		dateTextField.setHorizontalAlignment(SwingConstants.CENTER);
		dateTextField.setEditable(false);
		dateTextField.setFont(dateTextField.getFont().deriveFont(dateTextField.getFont().getStyle() | Font.BOLD));
		add(dateTextField);
		dateTextField.setBounds(15, 45, 110, 30);

		//---- daysTextField ----
		daysTextField.setText("Days on Trail");
		daysTextField.setHorizontalAlignment(SwingConstants.CENTER);
		daysTextField.setEditable(false);
		daysTextField.setFont(daysTextField.getFont().deriveFont(daysTextField.getFont().getStyle() | Font.BOLD));
		add(daysTextField);
		daysTextField.setBounds(130, 45, 110, 30);

		//---- locationTextField ----
		locationTextField.setText("Location");
		locationTextField.setHorizontalAlignment(SwingConstants.CENTER);
		locationTextField.setEditable(false);
		locationTextField.setFont(locationTextField.getFont().deriveFont(locationTextField.getFont().getStyle() | Font.BOLD));
		add(locationTextField);
		locationTextField.setBounds(245, 45, 110, 30);

		//---- distanceTextField ----
		distanceTextField.setText("Distance");
		distanceTextField.setHorizontalAlignment(SwingConstants.CENTER);
		distanceTextField.setEditable(false);
		distanceTextField.setFont(distanceTextField.getFont().deriveFont(distanceTextField.getFont().getStyle() | Font.BOLD));
		add(distanceTextField);
		distanceTextField.setBounds(360, 45, 110, 30);

		//---- nextLandmarkTextField ----
		nextLandmarkTextField.setText("Next Landmark");
		nextLandmarkTextField.setHorizontalAlignment(SwingConstants.CENTER);
		nextLandmarkTextField.setEditable(false);
		nextLandmarkTextField.setFont(nextLandmarkTextField.getFont().deriveFont(nextLandmarkTextField.getFont().getStyle() | Font.BOLD));
		add(nextLandmarkTextField);
		nextLandmarkTextField.setBounds(475, 45, 110, 30);

		//---- weatherTextField ----
		weatherTextField.setText("Weather");
		weatherTextField.setHorizontalAlignment(SwingConstants.CENTER);
		weatherTextField.setEditable(false);
		weatherTextField.setFont(weatherTextField.getFont().deriveFont(weatherTextField.getFont().getStyle() | Font.BOLD));
		add(weatherTextField);
		weatherTextField.setBounds(590, 45, 110, 30);

		//---- healthTextField ----
		healthTextField.setText("Health");
		healthTextField.setHorizontalAlignment(SwingConstants.CENTER);
		healthTextField.setEditable(false);
		healthTextField.setFont(healthTextField.getFont().deriveFont(healthTextField.getFont().getStyle() | Font.BOLD));
		add(healthTextField);
		healthTextField.setBounds(705, 45, 110, 30);

		//---- foodTextField ----
		foodTextField.setText("Food");
		foodTextField.setHorizontalAlignment(SwingConstants.CENTER);
		foodTextField.setEditable(false);
		foodTextField.setFont(foodTextField.getFont().deriveFont(foodTextField.getFont().getStyle() | Font.BOLD));
		add(foodTextField);
		foodTextField.setBounds(820, 45, 110, 30);

		//---- oxenHealthTextField ----
		oxenHealthTextField.setText("Oxen Health");
		oxenHealthTextField.setHorizontalAlignment(SwingConstants.CENTER);
		oxenHealthTextField.setEditable(false);
		oxenHealthTextField.setFont(oxenHealthTextField.getFont().deriveFont(oxenHealthTextField.getFont().getStyle() | Font.BOLD));
		add(oxenHealthTextField);
		oxenHealthTextField.setBounds(935, 45, 110, 30);

		//---- oxenHealthTextFieldData ----
		oxenHealthTextFieldData.setText("100%");
		oxenHealthTextFieldData.setHorizontalAlignment(SwingConstants.CENTER);
		oxenHealthTextFieldData.setEditable(false);
		add(oxenHealthTextFieldData);
		oxenHealthTextFieldData.setBounds(935, 75, 110, 30);

		//---- foodTextFieldData ----
		foodTextFieldData.setText("200 pounds");
		foodTextFieldData.setHorizontalAlignment(SwingConstants.CENTER);
		foodTextFieldData.setEditable(false);
		add(foodTextFieldData);
		foodTextFieldData.setBounds(820, 75, 110, 30);

		//---- weatherTextFieldData ----
		weatherTextFieldData.setText("Clear");
		weatherTextFieldData.setHorizontalAlignment(SwingConstants.CENTER);
		weatherTextFieldData.setEditable(false);
		weatherTextFieldData.setFont(weatherTextFieldData.getFont().deriveFont(weatherTextFieldData.getFont().getStyle() & ~Font.BOLD));
		add(weatherTextFieldData);
		weatherTextFieldData.setBounds(590, 75, 110, 30);

		//---- healthTextFieldData ----
		healthTextFieldData.setText("Good");
		healthTextFieldData.setHorizontalAlignment(SwingConstants.CENTER);
		healthTextFieldData.setEditable(false);
		add(healthTextFieldData);
		healthTextFieldData.setBounds(705, 75, 110, 30);

		//---- nextLandmarkTextFieldData ----
		nextLandmarkTextFieldData.setText("Fort Kearny");
		nextLandmarkTextFieldData.setHorizontalAlignment(SwingConstants.CENTER);
		nextLandmarkTextFieldData.setEditable(false);
		add(nextLandmarkTextFieldData);
		nextLandmarkTextFieldData.setBounds(475, 75, 110, 30);

		//---- distanceTextFieldData ----
		distanceTextFieldData.setText("0 miles");
		distanceTextFieldData.setHorizontalAlignment(SwingConstants.CENTER);
		distanceTextFieldData.setEditable(false);
		add(distanceTextFieldData);
		distanceTextFieldData.setBounds(360, 75, 110, 30);

		//---- locationTextFieldData ----
		locationTextFieldData.setText("Independence, Missouri");
		locationTextFieldData.setHorizontalAlignment(SwingConstants.CENTER);
		locationTextFieldData.setEditable(false);
		add(locationTextFieldData);
		locationTextFieldData.setBounds(245, 75, 110, 30);

		//---- daysTextFieldData ----
		daysTextFieldData.setText("1");
		daysTextFieldData.setHorizontalAlignment(SwingConstants.CENTER);
		daysTextFieldData.setEditable(false);
		add(daysTextFieldData);
		daysTextFieldData.setBounds(130, 75, 110, 30);

		//---- dateTextFieldData ----
		dateTextFieldData.setText("March 1, 1848");
		dateTextFieldData.setHorizontalAlignment(SwingConstants.CENTER);
		dateTextFieldData.setEditable(false);
		add(dateTextFieldData);
		dateTextFieldData.setBounds(15, 75, 110, 30);

		{
			// compute preferred size
			Dimension preferredSize = new Dimension();
			for(int i = 0; i < getComponentCount(); i++) {
				Rectangle bounds = getComponent(i).getBounds();
				preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
				preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
			}
			Insets insets = getInsets();
			preferredSize.width += insets.right;
			preferredSize.height += insets.bottom;
			setMinimumSize(preferredSize);
			setPreferredSize(preferredSize);
		}
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
	// Generated using JFormDesigner Evaluation license - Alex Randall
	private JLabel label1;
	private JButton travelButton;
	private JButton restButton;
	private JButton huntButton;
	private JButton inventoryButton;
	private JButton historyButton;
	private JButton quitButton;
	private JScrollPane outputTextScrollPane;
	private JTextArea outputTextArea;
	private JPanel mapPanel;
	private JTextField dateTextField;
	private JTextField daysTextField;
	private JTextField locationTextField;
	private JTextField distanceTextField;
	private JTextField nextLandmarkTextField;
	private JTextField weatherTextField;
	private JTextField healthTextField;
	private JTextField foodTextField;
	private JTextField oxenHealthTextField;
	private JTextField oxenHealthTextFieldData;
	private JTextField foodTextFieldData;
	private JTextField weatherTextFieldData;
	private JTextField healthTextFieldData;
	private JTextField nextLandmarkTextFieldData;
	private JTextField distanceTextFieldData;
	private JTextField locationTextFieldData;
	private JTextField daysTextFieldData;
	private JTextField dateTextFieldData;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
