package test;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import com.funkypickle.SwingLF.CustomLF;

public class Main extends JPanel {
	private static final long serialVersionUID = 1L;
	private JTable table;
	private JScrollPane sp;

	public Main() {
		setPreferredSize(new Dimension(500, 500));
		setLayout(null);
		@SuppressWarnings("serial")
		TableModel dataModel = new AbstractTableModel() {
			@Override
			public int getColumnCount() {
				return 10;
			}

			@Override
			public int getRowCount() {
				return 10;
			}

			@SuppressWarnings("deprecation")
			@Override
			public Object getValueAt(int row, int col) {
				return new Integer(row * col);
			}
		};
		table = new JTable(dataModel);
		sp = new JScrollPane(table);

		table.setBounds(0, 0, 200, 100);
		sp.setBounds(0, 0, 200, 100);

		add(sp);
	}

	public static void main(String[] args) throws UnsupportedLookAndFeelException {
		UIManager.setLookAndFeel(new CustomLF());
		JFrame frame = new JFrame("Tester");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(new Main());
		frame.pack();
		frame.setVisible(true);
	}
}
