package ClasesApp;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFormattedTextField;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class VentanaPrincipal extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	public static JTextField CuadroTexto;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaPrincipal frame = new VentanaPrincipal();
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
	public VentanaPrincipal() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnNewButton = new JButton("Enviar");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Ventana2 x=new Ventana2();
				x.setVisible(true);
			}
		});
		btnNewButton.setBounds(172, 100, 89, 23);
		btnNewButton.setHorizontalAlignment(SwingConstants.LEFT);
		contentPane.add(btnNewButton);
		
		JLabel lblNewLabel = new JLabel("Ponga aquí su nombre");
		lblNewLabel.setBounds(160, 30, 111, 14);
		contentPane.add(lblNewLabel);
		
		CuadroTexto = new JTextField();
		CuadroTexto.setBounds(172, 55, 86, 20);
		contentPane.add(CuadroTexto);
		CuadroTexto.setColumns(10);

	}
}
