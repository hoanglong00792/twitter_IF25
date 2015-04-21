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
	private Thread thread_rest, thread_stream, thread_analyse, thread_SVM;
	private JButton startBouton_rest = new JButton("Démarrer");
	private JButton cancelBouton_rest = new JButton("Arrêter");
	private JButton startBouton_stream = new JButton("Démarrer");
	private JButton cancelBouton_stream = new JButton("Arrêter");
	private JButton startBouton_analyse = new JButton("Démarrer");
	private JButton cancelBouton_analyse = new JButton("Arrêter");
	private JButton startBouton_statistique = new JButton("Mettre à jour");
	private JButton startBouton_visualisation = new JButton("Mettre à jour");
	private JButton startBouton_SVM = new JButton("Démarrer");
	private JButton cancelBouton_SVM = new JButton("Arrêter");
	private JPanel panDroite, panGauche, panCentre, panRest, panstream,
			pananalyse, panUpdate_graph, panSVM;
	private Component com;
	private Timer updater;
	private JTextField mot_cle;
	private JLabel mot_cle_Label;
	private JComboBox[] coordinate_Boxs;
	private JComboBox group_Boxs;

	public Fenetre() {
		// AnalyseData analyse = new AnalyseData(false);
		// StreamTweets stream = new StreamTweets();
		this.setTitle("IF25");
		this.setSize(820, 480);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setResizable(false);

		// GAUCHE

		mot_cle_Label = new JLabel("Mot clé :");
		mot_cle = new JTextField();
		mot_cle.setPreferredSize(new Dimension(100, 25));

		panGauche = new JPanel();
		panGauche.setPreferredSize(new Dimension(220, 120));
		panGauche.setBorder(BorderFactory.createTitledBorder("Actions"));

		// REST
		cancelBouton_rest.setEnabled(false);
		panRest = new JPanel();
		panRest.setBorder(BorderFactory.createTitledBorder("REST API"));
		panRest.setPreferredSize(new Dimension(210, 90));
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
					SearchTweets search = new SearchTweets(mot_cle.getText(),
							false);
					thread_rest = new Thread(search);
					thread_rest.start();
				}
				// thread_rest.suspend();
				// thread_rest.resume();

			}
		});

		// STREAM
		cancelBouton_stream.setEnabled(false);
		panstream = new JPanel();
		panstream.setBorder(BorderFactory.createTitledBorder("STREAM API"));
		panstream.setPreferredSize(new Dimension(210, 60));
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
				// thread_stream.suspend();
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
		pananalyse = new JPanel();
		pananalyse.setBorder(BorderFactory.createTitledBorder("ANALYSE API"));
		pananalyse.setPreferredSize(new Dimension(210, 60));
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
				AnalyseData analyse = new AnalyseData(0);
				thread_analyse = new Thread(analyse);
				thread_analyse.start();
				// thread_analyse.suspend();
				// thread_analyse.resume();

			}
		});

		// SVM
		cancelBouton_SVM.setEnabled(false);
		panSVM = new JPanel();
		panSVM.setBorder(BorderFactory.createTitledBorder("SVM"));
		panSVM.setPreferredSize(new Dimension(210, 60));
		panSVM.add(startBouton_SVM);
		panSVM.add(cancelBouton_SVM);
		panGauche.add(panSVM);

		cancelBouton_SVM.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// setVisible(false);
				// GUIAccueil guiAccueil= new GUIAccueil();
				// guiAccuei-l.setVisible(true);
				cancelBouton_SVM.setEnabled(false);
				startBouton_SVM.setEnabled(true);
				thread_SVM.suspend();
			}
		});

		startBouton_SVM.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				cancelBouton_SVM.setEnabled(true);
				startBouton_SVM.setEnabled(false);
				ClassifierSVM SVM = new ClassifierSVM();
				thread_SVM = new Thread(SVM);
				thread_SVM.start();
				// thread_SVM.suspend();
				// thread_SVM.resume();

			}
		});

		// CENTRE
		panCentre = new JPanel();

		panCentre.setPreferredSize(new Dimension(560, 120));
		panCentre.setBorder(BorderFactory.createTitledBorder("Statistiques"));
		Statistique statistique = new Statistique();
		statistique.getStatistique();
		final JLabel count_Label = new JLabel("<html>Nombre de tweets : "
				+ statistique.count + " " + "dont analysés : "
				+ statistique.count_analysed + "<br/>"
				+ "Nombre d'utilisateurs : " + statistique.count_user + " "
				+ "dont soupçonneux  : " + statistique.count_spam + " "
				+ "et vérifiés  : " + statistique.count_verified + "</html>",
				SwingConstants.LEFT);
		panCentre.add(count_Label);
		// panCentre.add(startBouton_statistique);

		// startBouton_statistique.addActionListener(new ActionListener() {
		// public void actionPerformed(ActionEvent arg0) {
		// Statistique statistique = new Statistique();
		// statistique.getStatistique();
		// count_Label.setText("<html>Nombre de tweets : "
		// + statistique.count + " "
		// + "Nombre de tweets analysés : "
		// + statistique.count_analysed + "<br/>"
		// + "Nombre d'utilisateurs : "
		// + statistique.count_user +" "
		// + "Nombre d'utilisateurs soupçonneux  : "
		// + statistique.count_spam + " "
		// + "Nombre d'utilisateurs vérifiés  : "
		// + statistique.count_verified +
		// "</html>");
		// }
		// });
		updater = new Timer(3000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (cancelBouton_analyse.isEnabled()
						|| cancelBouton_rest.isEnabled()
						|| cancelBouton_stream.isEnabled()
						|| cancelBouton_SVM.isEnabled()) {
					Statistique statistique = new Statistique();
					statistique.getStatistique();
					if (statistique.count == statistique.count_analysed) {
						cancelBouton_analyse.setEnabled(false);
						startBouton_analyse.setEnabled(true);
					}
					if (statistique.count_verified + statistique.count_spam == statistique.count_user) {
						cancelBouton_SVM.setEnabled(false);
						startBouton_SVM.setEnabled(true);
					}
					count_Label.setText("<html>Nombre de tweets : "
							+ statistique.count + " " + "dont analysés : "
							+ statistique.count_analysed + "<br/>"
							+ "Nombre d'utilisateurs : "
							+ statistique.count_user + " "
							+ "dont soupçonneux  : " + statistique.count_spam
							+ " " + "et vérifiés  : "
							+ statistique.count_verified + "</html>");
				}
			}
		});
		updater.start();

		// DROITE
		panDroite = new JPanel();
		panDroite.setPreferredSize(new Dimension(600, 480));
		panDroite.setBorder(BorderFactory.createTitledBorder("Visualisation"));
		panUpdate_graph = new JPanel();
		coordinate_Boxs = new JComboBox[3];
		for (int i = 0; i < 3; i++) {
			coordinate_Boxs[i] = new JComboBox();
			coordinate_Boxs[i].addItem("agressiveness");
			coordinate_Boxs[i].addItem("visibility");
			coordinate_Boxs[i].addItem("danger");
			coordinate_Boxs[i].addItem("mention_per_tweet");
			coordinate_Boxs[i].addItem("hashtag_per_tweet");
			coordinate_Boxs[i].addItem("malware_link_per_tweet");
			coordinate_Boxs[i].addItem("friends_count");
			coordinate_Boxs[i].addItem("followers_count");
			JLabel coordinate_JLabel;
			if (i == 0) {
				coordinate_JLabel = new JLabel("Coordonnée X :");
				coordinate_Boxs[i].setSelectedItem("agressiveness");
			} else if (i == 1) {
				coordinate_JLabel = new JLabel("Coordonnée Y :");
				coordinate_Boxs[i].setSelectedItem("visibility");
			} else if (i == 2) {
				coordinate_JLabel = new JLabel("Coordonnée Z :");
				coordinate_Boxs[i].setSelectedItem("danger");
			} else {
				coordinate_JLabel = new JLabel("Coordonnée :");
				coordinate_Boxs[i].setSelectedItem("agressiveness");
			}
			panUpdate_graph.add(coordinate_JLabel);
			panUpdate_graph.add(coordinate_Boxs[i]);
		}
		JLabel group_JLabel = new JLabel("Filtre :");
		group_Boxs = new JComboBox();
		group_Boxs.addItem("Tout");
		group_Boxs.addItem("Les utilisateurs soupçonneux");
		group_Boxs.addItem("Les utilisateurs vérifiés");
		panUpdate_graph.add(group_JLabel);
		panUpdate_graph.add(group_Boxs);
		panUpdate_graph.add(startBouton_visualisation);
		panUpdate_graph.setBorder(BorderFactory
				.createTitledBorder("Les coordonnées"));
		panUpdate_graph.setPreferredSize(new Dimension(250, 300));
		panDroite.add(panUpdate_graph);
		mettre_a_jour_graph(false);
		startBouton_visualisation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mettre_a_jour_graph(true);
				// panDroite.add(startBouton_visualisation);
			}
		});
		this.getContentPane().add(panDroite, BorderLayout.EAST);
		this.getContentPane().add(panGauche, BorderLayout.WEST);
		// this.getContentPane().add(panCentre, BorderLayout.SOUTH);

		this.setVisible(true);

	}

	public void mettre_a_jour_graph(boolean update) {
		Visualisation visualisation;
		if (group_Boxs.getSelectedItem()
				.equals("Les utilisateurs vérifiés")) {
			visualisation= new Visualisation(3);
		} else if (group_Boxs.getSelectedItem().equals(
				"Les utilisateurs soupçonneux")) {
			visualisation= new Visualisation(4);
		}else{
			visualisation= new Visualisation(0);
		}

		 
		visualisation.getVisualisation();

		int size = visualisation.count_user;
		float a;

		Coord3d[] points = new Coord3d[size];
		Color[] colors = new Color[size];
		double[][] coordinate = new double[3][size];
		String[] string_coordinate = new String[3];
		for (int i = 0; i < 3; i++) {
			string_coordinate[i] = (String) coordinate_Boxs[i]
					.getSelectedItem();
			if (string_coordinate[i].equals("agressiveness"))
				coordinate[i] = visualisation.agressiveness;
			else if (string_coordinate[i].equals("visibility"))
				coordinate[i] = visualisation.visibility;
			else if (string_coordinate[i].equals("danger"))
				coordinate[i] = visualisation.danger;
			else if (string_coordinate[i].equals("mention_per_tweet"))
				coordinate[i] = visualisation.mention_per_tweet;
			else if (string_coordinate[i].equals("hashtag_per_tweet"))
				coordinate[i] = visualisation.hashtag_per_tweet;
			else if (string_coordinate[i].equals("malware_link_per_tweet"))
				coordinate[i] = visualisation.malware_link_per_tweet;
			else if (string_coordinate[i].equals("friends_count"))
				coordinate[i] = visualisation.friends_count;
			else if (string_coordinate[i].equals("followers_count"))
				coordinate[i] = visualisation.followers_count;
			else {
				coordinate[i] = visualisation.agressiveness;
			}
		}
		for (int i = 0; i < size; i++) {

			// x = (float)Math.random() - 0.5f;
			// y = (float)Math.random() - 0.5f;
			// z = (float)Math.random() - 0.5f;

			points[i] = new Coord3d(coordinate[0][i], coordinate[1][i],
					coordinate[2][i]);
			a = 0.25f;
			if (visualisation.user_class[i] == -1) {
				colors[i] = new Color(255, 0, 0);
			} else if (visualisation.user_class[i] == 1) {
				colors[i] = new Color(0, 0, 255);
			} else {
				colors[i] = new Color(0, 255, 0);
			}
		}

		Scatter scatter = new Scatter(points, colors);
		scatter.setWidth(2);
		Chart chart = new Chart(Quality.Advanced, "awt");
		chart.getScene().add(scatter);
		if (update) {
			panDroite.remove(com);
			// this.remove(panDroite);
			panDroite.remove(panCentre);
			panDroite.revalidate();
		}
		com = (Component) chart.getCanvas();
		com.setPreferredSize(new Dimension(320, 320));
		System.out.println(com.getPreferredSize());
		panDroite.add(com, BorderLayout.EAST);
		panDroite.add(panCentre);
		if (update) {
			this.repaint();
			// this.getContentPane().add(panDroite, BorderLayout.EAST);
		}
	}

	public static void main(String[] args) {
		Fenetre fenetre = new Fenetre();

	}

}