package twitter_IF25;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import org.jzy3d.chart.Chart;
import org.jzy3d.colors.Color;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.Scatter;
import org.jzy3d.plot3d.rendering.canvas.Quality;


public class Fenetre extends JFrame {
	private Thread thread_rest,thread_stream,thread_analyse;
	private JButton startBouton_rest = new JButton("Démarrer");
	private JButton cancelBouton_rest = new JButton("Arrêter");
	private JButton startBouton_stream = new JButton("Démarrer");
	private JButton cancelBouton_stream = new JButton("Arrêter");
	private JButton startBouton_analyse = new JButton("Démarrer");
	private JButton cancelBouton_analyse = new JButton("Arrêter");
	private JButton startBouton_statistique = new JButton("Mettre à jour");
	private JButton startBouton_visualisation = new JButton("Mettre à jour");
	private JPanel panDroite;
	public Fenetre() {
		AnalyseData analyse = new AnalyseData();
		//StreamTweets stream = new StreamTweets();
		this.setTitle("IF25");
		this.setSize(720, 480);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setResizable(false);

		// GAUCHE

		JLabel mot_cle_Label = new JLabel("Mot clé :");
		final JTextField mot_cle = new JTextField();
		mot_cle.setPreferredSize(new Dimension(100, 25));

		JPanel panGauche = new JPanel();
		panGauche.setPreferredSize(new Dimension(240, 120));
		panGauche.setBorder(BorderFactory.createTitledBorder("Actions"));

		// REST
		cancelBouton_rest.setEnabled(false);
		JPanel panRest = new JPanel();
		panRest.setBorder(BorderFactory.createTitledBorder("REST API"));
		panRest.setPreferredSize(new Dimension(220, 90));
		panRest.add(mot_cle_Label);
		panRest.add(mot_cle);
		panRest.add(startBouton_rest);
		panRest.add(cancelBouton_rest);
		panGauche.add(panRest);

		cancelBouton_rest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// setVisible(false);
				// GUIAccueil guiAccueil= new GUIAccueil();
				// guiAccuei-l.setVisible(true);
				cancelBouton_rest.setEnabled(false);
				startBouton_rest.setEnabled(true);
				thread_rest.suspend();
			}
		});

		startBouton_rest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (mot_cle.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "Saisir le mot clé");
				} else {
					cancelBouton_rest.setEnabled(true);
					startBouton_rest.setEnabled(false);
					SearchTweets search = new SearchTweets(mot_cle.getText());
					thread_rest = new Thread(search);
					thread_rest.start();
				}
				// thread_rest.suspend();
				// thread_rest.resume();

			}
		});

		// STREAM
		cancelBouton_stream.setEnabled(false);
		JPanel panstream = new JPanel();
		panstream.setBorder(BorderFactory.createTitledBorder("STREAM API"));
		panstream.setPreferredSize(new Dimension(220, 60));
		panstream.add(startBouton_stream);
		panstream.add(cancelBouton_stream);
		panGauche.add(panstream);

		cancelBouton_stream.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// setVisible(false);
				// GUIAccueil guiAccueil= new GUIAccueil();
				// guiAccuei-l.setVisible(true);
				cancelBouton_stream.setEnabled(false);
				startBouton_stream.setEnabled(true);
				//thread_stream.suspend();
				StreamTweets stream = new StreamTweets(false);
				thread_stream = new Thread(stream);
				thread_stream.start();
			}
		});

		startBouton_stream.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				cancelBouton_stream.setEnabled(true);
				startBouton_stream.setEnabled(false);
				StreamTweets stream = new StreamTweets(true);
				thread_stream = new Thread(stream);
				thread_stream.start();
				// thread_rest.suspend();
				// thread_rest.resume();

			}
		});

		// ANALYSE
		cancelBouton_analyse.setEnabled(false);
		JPanel pananalyse = new JPanel();
		pananalyse.setBorder(BorderFactory.createTitledBorder("ANALYSE API"));
		pananalyse.setPreferredSize(new Dimension(220, 60));
		pananalyse.add(startBouton_analyse);
		pananalyse.add(cancelBouton_analyse);
		panGauche.add(pananalyse);

		cancelBouton_analyse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// setVisible(false);
				// GUIAccueil guiAccueil= new GUIAccueil();
				// guiAccuei-l.setVisible(true);
				cancelBouton_analyse.setEnabled(false);
				startBouton_analyse.setEnabled(true);
				thread_analyse.suspend();
			}
		});

		startBouton_analyse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
					cancelBouton_analyse.setEnabled(true);
					startBouton_analyse.setEnabled(false);
					AnalyseData analyse = new AnalyseData();
					thread_analyse = new Thread(analyse);
					thread_analyse.start();
				// thread_analyse.suspend();
				// thread_analyse.resume();

			}
		});

		
		// CENTRE
		JPanel panCentre = new JPanel();
		
		panCentre.setPreferredSize(new Dimension(480, 120));
		panCentre.setBorder(BorderFactory.createTitledBorder("Statistiques"));
		Statistique statistique=new Statistique();
		statistique.getStatistique();
		final JLabel count_Label = new JLabel("<html>Nombre de tweets : "+statistique.count+"<br/>"+"Nombre de tweets analysés : "+statistique.count_analysed+"<br/>"+"Nombre d'utilisateurs : "+statistique.count_user+"</html>",SwingConstants.LEFT);
		panCentre.add(count_Label);
		//panCentre.add(startBouton_statistique);

		startBouton_statistique.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {				
				Statistique statistique=new Statistique();
				statistique.getStatistique();
				count_Label.setText("<html>Nombre de tweets : "+statistique.count+"<br/>"+"Nombre de tweets analysés : "+statistique.count_analysed+"<br/>"+"Nombre d'utilisateurs : "+statistique.count_user+"</html>");
			}
		});
		final Timer updater = new Timer(3000, new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	Statistique statistique=new Statistique();
				statistique.getStatistique();
				count_Label.setText("<html>Nombre de tweets : "+statistique.count+"<br/>"+"Nombre de tweets analysés : "+statistique.count_analysed+"<br/>"+"Nombre d'utilisateurs : "+statistique.count_user+"</html>");
		    }
		});	
        updater.start();

		// DROITE
		panDroite = new JPanel();
		panDroite.setPreferredSize(new Dimension(480, 480));
		panDroite.setBorder(BorderFactory.createTitledBorder("Visualisation"));

        panDroite.add(startBouton_visualisation);
        mettre_a_jour_graph();
        startBouton_visualisation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {				
				mettre_a_jour_graph();
			}
		});
        
		this.getContentPane().add(panDroite, BorderLayout.EAST);
		this.getContentPane().add(panCentre, BorderLayout.SOUTH);
		this.getContentPane().add(panGauche, BorderLayout.WEST);

		this.setVisible(true);

	}
	
	public void mettre_a_jour_graph(){

		Visualisation visualisation=new Visualisation();
		visualisation.getVisualisation();
		
		int size = visualisation.count_user;
        double x;
        double y;
        double z;
        float a;
        
        Coord3d[] points = new Coord3d[size];
        Color[]   colors = new Color[size];
        
        for(int i=0; i<size; i++){
//            x = (float)Math.random() - 0.5f;
//            y = (float)Math.random() - 0.5f;
//            z = (float)Math.random() - 0.5f;
        	x=visualisation.agressiveness[i];
        	y=visualisation.visibility[i];
        	//z=visualisation.danger[i];
        	z = (float)Math.random() - 0.5f;
            points[i] = new Coord3d(x, y, z);
            a = 0.25f;
            colors[i] = new Color(255, 0, 0);
        }
        
        Scatter scatter = new Scatter(points, colors);
        //scatter.setWidth(2);
        Chart chart = new Chart(Quality.Advanced,"awt");
        chart.getScene().add(scatter);
        Component com=(Component)chart.getCanvas();
        com.setPreferredSize(new Dimension(320, 320));
        System.out.println(com.getPreferredSize());
        panDroite.add(com, BorderLayout.EAST);		
	}

	public static void main(String[] args) {
		Fenetre fenetre = new Fenetre();

	}


}