package yazlabql;

import java.awt.BasicStroke;
import java.awt.Color;
import static java.awt.Color.WHITE;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class YazLabQL{

    final static DecimalFormat df = new DecimalFormat("###.###");
    public static final double GAMMA = 0.8;

    public static void main(String[] args){
       
        JFrame frame = new JFrame("Yaz_LabII_I");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());
        JTextField ilkDurumyazi = new JTextField("ilk dugum=");
        JTextField ilkdDurum = new JTextField(13);
        JTextField sonDurumyazi = new JTextField("hedef dugum=");
        JTextField sondDurum = new JTextField(13);
        JTextField itrSayyazi = new JTextField("iterasyon sayisi=");
        JTextField itrdSay = new JTextField(13);
        ilkDurumyazi.setEditable(false);
        sonDurumyazi.setEditable(false);
        itrSayyazi.setEditable(false);
        JButton butonTamam = new JButton("Tamam");
        frame.add(ilkDurumyazi);
        frame.add(ilkdDurum);
        frame.add(sonDurumyazi);
        frame.add(sondDurum);
        frame.add(itrSayyazi);
        frame.add(itrdSay);
        frame.add(butonTamam);
        frame.setSize(500,200);
        frame.setVisible(true);
        frame.setLocation(100,300);
        
        butonTamam.addActionListener(new ActionListener(){
        	
        	public void actionPerformed(ActionEvent e){
        	   
        		String satir;
        		ArrayList<ArrayList<Integer>> durum = new ArrayList<>();
        
        		try{
        			BufferedReader dosya = new BufferedReader(new FileReader("C:\\Users\\**username**\\Desktop\\input.txt"));

        			while ((satir = dosya.readLine()) != null){
        				String[] elemanlar = satir.split(",");
        				ArrayList<Integer> komsu = new ArrayList<>();
        				for (int i = 0; i < elemanlar.length; i++){
        					komsu.add(Integer.parseInt(elemanlar[i]));
        				}
        				durum.add(komsu);
        			}

        			dosya.close();
            	
        			for (int i = 0; i < durum.size(); i++){
        				for (int j = 0; j < durum.get(i).size(); j++){
        					System.out.print(durum.get(i).get(j) + " ");
        				}
        				System.out.println();
        			}
            
        		} catch (IOException ex){
        			ex.printStackTrace();
        		}
            
        		int[][] R = new int [durum.size()][durum.size()];
            
        		int boyut = durum.size();
            
        		ilkdDurum.setToolTipText("baslangic dugum");   
        		sondDurum.setToolTipText("hedef dugum");   
        		itrdSay.setToolTipText("iterasyon sayisi");   
            
        		int ilkDurum = Integer.parseInt(ilkdDurum.getText());
        		int sonDurum = Integer.parseInt(sondDurum.getText());
        		int itrSay = Integer.parseInt(itrdSay.getText());
        		
        		if(ilkDurum<0 || ilkDurum>=durum.size()){
        			JOptionPane.showMessageDialog(frame, "Lutfen baslangic dugum degerini dogru giriniz");
        		}
        		else if(sonDurum<0 || sonDurum>=durum.size()){
        			JOptionPane.showMessageDialog(frame, "Lutfen hedef dugum degerini dogru giriniz");
        		}
        		else if(itrSay<1){
        			JOptionPane.showMessageDialog(frame, "Lutfen iterasyon sayisini dogru giriniz");
        		}
        		else{
        			for(int i=0; i < durum.size(); i++){
        				for(int j=0; j < durum.size(); j++){
        					R[i][j] = -1;
        				} 
        			}
    	
        			for(int i=0; i < durum.size(); i++){
        				for(int j=0; j < durum.get(i).size(); j++){
        					R[durum.get(i).get(j)][i] = 0; 
        				}
        			}
    	
        			for(int i=0; i<durum.get(sonDurum).size(); i++){
        				R[durum.get(sonDurum).get(i)][sonDurum] = 100; 
        			}
    	     
        			for(int i = 0; i < durum.size(); i++){
        				if(i==sonDurum)
        					R[i][i] = 100;             
        			}
                
        			double[][] Q = new double [durum.size()][durum.size()];
        			
        			for(int i=0; i < durum.size(); i++){
        				for(int j=0; j < durum.size(); j++)
        				{
        					Q[i][j] = 0;
        				} 
        			} 
        			
        			Random rand = new Random();
    	
        			for (int i=0; i < itrSay; i++){ 
        				
        				int konum = ilkDurum;
        				
        				while (konum != sonDurum){
        					
        					int[] hareket = new int[durum.get(konum).size()];
        					
        					for(int j=0; j<durum.get(konum).size(); j++){
        						hareket[j] = durum.get(konum).get(j);
        					}            	
            	
        					int index = rand.nextInt(hareket.length);
                
        					int sonrakiKonum = hareket[index];
                
        					int[] hareket2 = new int[durum.get(sonrakiKonum).size()];
                
        					for(int j=0; j<durum.get(sonrakiKonum).size(); j++){
        						hareket2[j] = durum.get(sonrakiKonum).get(j);
        					}  
                
        					double maxDeger = 0;
                
        					for (int j = 0; j < hareket2.length; j++){
        						
        						int sonrakiKonum2 = hareket2[j];
        						double deger = Q[sonrakiKonum][sonrakiKonum2];
         
        						if (deger > maxDeger)
        							maxDeger = deger;
        					}
 
        					Q[konum][sonrakiKonum] = R[konum][sonrakiKonum] + GAMMA * maxDeger;
                
        					konum = sonrakiKonum;
        				}
            
        				if(konum == sonDurum){
        					
        					int[] hareket = new int[durum.get(konum).size()];
            	
        					for(int j=0; j<durum.get(konum).size(); j++){
        						hareket[j] = durum.get(konum).get(j);
        					}            	
            	
        					int index = rand.nextInt(hareket.length);
                
        					int sonrakiKonum = hareket[index];
                
        					int[] hareket2 = new int[durum.get(sonrakiKonum).size()];
                
        					for(int j=0; j<durum.get(sonrakiKonum).size(); j++){
        						hareket2[j] = durum.get(sonrakiKonum).get(j);
        					}  
                
        					double maxDeger = 0;
                
        					for (int j = 0; j < hareket2.length; j++){
                	
        						int sonrakiKonum2 = hareket2[j];
        						double deger = Q[sonrakiKonum][sonrakiKonum2];
         
        						if (deger > maxDeger)
        							maxDeger = deger;
        					}
                
        					double maxDeger2 = 0;
                
        					for (int j = 0; j < hareket.length; j++){
                	
        						int sonrakiKonum3 = hareket[j];
        						double deger2 = Q[sonDurum][sonrakiKonum3];
         
        						if (deger2 > maxDeger2)
        							maxDeger2 = deger2;
        					}
 
        					Q[konum][sonrakiKonum] = R[konum][sonrakiKonum] + GAMMA * maxDeger;
                
        					Q[konum][konum] = R[konum][konum] + GAMMA * maxDeger2;
        				}
            
        			}
    	
        			System.out.println("\nR Matrisi: ");
    	
        			for(int i=0; i < durum.size(); i++){
        				for(int j=0; j < durum.size(); j++){
        					System.out.print(df.format(R[i][j]) + "\t");
        				} 
        				System.out.println();
        			} 
    	
        			System.out.println("\nQ Matrisi: ");
    	
        			for(int i=0; i < durum.size(); i++){
        				for(int j=0; j < durum.size(); j++){
        					System.out.print(df.format(Q[i][j]) + "\t");
        				} 
        				System.out.println();
        			} 
    	
        			System.out.println("\nYol:");
    		
        			int[] from = new int[durum.size()];
        			int[] to = new int[durum.size()];
        			int[] rota = new int[durum.size()];
    	
        			from[0] = ilkDurum;    
        			to[0] = ilkDurum;
        
        			double maxDeger = 0;
            
        			for (int i = 0; i < durum.size(); i++){
        				for(int j = 0; j<durum.get(from[i]).size(); j++){
        		
        					rota[i+1] = durum.get(from[i]).get(j);
        		
        					double deger = Q[from[i]][rota[i+1]];
        		
        					if(deger > maxDeger){
        						maxDeger = deger;
        						to[i] = rota[i+1];
        						from[i+1] = rota[i+1];
        					}
        				} 
        				
        				if(to[i]==sonDurum)
        					break;
        			}
        
        			int i=0;
        	        
        	        while(to[i]!=sonDurum){
        	        	System.out.print(from[i] + " - ");
        	        	i++;
        	        }
        	        System.out.print(from[i]+ " - " + to[i]);
        
        	        try{
        	        	File yazici1 = new File("C:\\Users\\**username**\\Desktop\\outR.txt");
        	        	PrintWriter yaz1 = new PrintWriter(yazici1);
            
        	        	for(int k=0; k < durum.size(); k++){
        	        		for(int l=0; l < durum.size(); l++){
        	        			yaz1.print(df.format(R[k][l]) + "\t");
        	        		} 
        	        		yaz1.println();
        	        	} 
            
        	        	yaz1.close();
        	        	
        	        	File yazici2 = new File("C:\\Users\\**username**\\Desktop\\outQ.txt");
        	        	PrintWriter yaz2 = new PrintWriter(yazici2);
            
        	        	for(int k=0; k < durum.size(); k++){
        	        		for(int l=0; l < durum.size(); l++){
        	        			String sf = String.format("%.3f" , Q[k][l]);  
        	        			yaz2.print(sf + "\t");
        	        		} 
        	        		yaz2.println();
        	        	} 
            
        	        	yaz2.close();
        
        	        	File yazici3 = new File("C:\\Users\\**username**\\Desktop\\outPath.txt");
        	        	PrintWriter yaz3 = new PrintWriter(yazici3);
            
        	        	int k=0;
            
        	        	while(to[k]!=sonDurum){
        	        		yaz3.print(from[k] + "-");
        	        		k++;
        	        	}
        	        	yaz3.print(from[k]+ "-" + to[k]);
            
        	        	yaz3.close();
        	        	
        	        } catch(IOException ex){
        	        	ex.printStackTrace();
        	        }
        	        
        	        ArrayList<Integer> path = new ArrayList<>();
        	        
        	        try{
        	        	BufferedReader dosya2 = new BufferedReader(new FileReader("C:\\Users\\**username**\\Desktop\\outPath.txt"));
        	        	String ayirici;
                
        	        	while ((ayirici = dosya2.readLine()) != null){
        	        		String[] dnm = ayirici.split("-");
        	        		for (int x = 0; x < dnm.length; x++){
        	        			path.add(Integer.parseInt(dnm[x]));
        	        		}
        	        	}
            
        	        	dosya2.close();
            
        	        } catch (IOException ex){
        	        	ex.printStackTrace();
        	        }
        
        	        JFrame frame2 = new JFrame();
        	        frame2.setTitle("Labirent Yolu");
        	        frame2.setBackground(WHITE);
        	        frame2.setVisible(true);
        	        frame2.setSize(400,400);
        	        frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        	        frame2.setLocation(100 , 300);
        
        	        JPanel panel = new JPanel(){ 
            
        	        	public void paintComponent(Graphics g){
        	        		
        	        		super.paintComponent(g);
        	        		Graphics2D g2 = (Graphics2D) g;
        	        		g2.setStroke(new BasicStroke(4));
        	        		setBackground(Color.white);
        	        		ArrayList<Integer> pass=new ArrayList<Integer>();
        	        		ArrayList<Integer> sag=new ArrayList<Integer>();
        	        		ArrayList<Integer> sol=new ArrayList<Integer>();
        	        		ArrayList<Integer> ust=new ArrayList<Integer>();
        	        		ArrayList<Integer> alt=new ArrayList<Integer>();
            
        	        		int x1=50, x2=50;
        	        		int y1=50, y2=50; 
        	        		int n= (int) Math.sqrt(boyut);
        	        		int total=0;
        	        		g.setColor(Color.BLACK);
            
        	        		for(int i=0; i < boyut; i++){ 
        	        			sag.add(0);
        	        			sol.add(0);
        	        			ust.add(0);
        	        			alt.add(0);
        	        		}
          
        	        		for(int i=0; i < boyut; i++){     
        	        			for (int j=0; j < boyut; j++){  
        	        				if (R[i][j] != -1)
        	        					pass.add(j);
        	        			}

        	        			for(int j=0; j < pass.size(); j++){
        	        				if (pass.get(j) == (i-1))
        	        					sol.add(i,1);
        	        				
        	        				if (pass.get(j) == (i+1))
        	        					sag.add(i,1);
        	        			
        	        				if ((pass.get(j) == (i-n)))
        	        					ust.add(i,1);
        	        			
        	        				if ((pass.get(j) == (i+n)))
        	        					alt.add(i,1);
        	        			}

        	        			if(i == ilkDurum){
        	        				if (i < n)
        	        					ust.add(i,1);
        	        			
        	        				if (i >= boyut-n)
        	        					alt.add(i,1);
        	        			}

        	        			if (i == sonDurum){
        	        				if (i < n)
        	        					ust.add(i,1);

        	        				if (i >= boyut-n)
        	        					alt.add(i,1);
        	        			}

        	        			pass.clear();
        	        		}
          
        	        		total=0;

        	        		int xYol=x1+20, yYol=y1+20, x2Yol=x2+20, y2Yol=y2+20;

        	        		for(int i=0; i < n; i++){
        	        			for(int j=i; j < n+i; j++){
        	        				if(sol.get(total) == 0)
        	        				{
        	        					g.setColor(Color.BLACK);
        	        					g.drawLine(x1,y1,x2,y2+50);
        	        				}
        	        				if(sag.get(total) == 0)
        	        				{
        	        					g.setColor(Color.BLACK);
        	        					g.drawLine(x1+50,y1,x2+50,y2+50);
        	        				}
        	        				if(ust.get(total) == 0)
        	        				{
        	        					g.setColor(Color.BLACK);
        	        					g.drawLine(x1,y1,x2+50,y2);
        	        				}
        	        				if(alt.get(total) == 0)
        	        				{
        	        					g.setColor(Color.BLACK);
        	        					g.drawLine(x1,y1+50,x2+50,y2+50);
        	        				}

        	        				x1=x1+50;
        	        				x2=x2+50;
        	        				total++;
        	        			}
               
        	        			x1=x1-(50*n);
        	        			y1=y1+50;
        	        			x2=x2-(50*n);
        	        			y2=y2+50;
        	        		}
        	        		
        	        		g2.setStroke(new BasicStroke(30));
        	        		int tnp=0;

        	        		for(int i=0; i < boyut; i=i+n){
        	        			for (int j=i; j < i+n; j++){
        	        				
        	        				if(ilkDurum == j){
        	        					xYol = xYol+(50*(ilkDurum-i));
        	        					x2Yol = x2Yol+(50*(ilkDurum-i));
        	        					tnp++;
        	        				}
        	        			}

        	        			if (tnp == 0){
        	        				yYol = yYol+50;
        	        				y2Yol = y2Yol+50;
        	        			}
        	        			
        	        			else
        	        				break;
        	        		}

        	        		for(int i=0; i < path.size()-1; i++){
        	        			int anlikyer=path.get(i),anlik2=path.get(i+1);

        	        			if (anlikyer == (anlik2-1))
        	        				x2Yol = x2Yol+50;
        	        			
        	        			if (anlikyer == (anlik2+1))
        	        				x2Yol = x2Yol-50;
        	        			
        	        			if (anlikyer == (anlik2+n))
        	        				y2Yol = y2Yol-50;
        	        			
        	        			if (anlikyer == (anlik2-n))        	        			
        	        				y2Yol = y2Yol+50;
        	        			
        	        			g.setColor(Color.GREEN);
        	        			g2.draw(new Line2D.Float(xYol,yYol,x2Yol,y2Yol));
            
        	        			xYol=x2Yol;
        	        			yYol=y2Yol;
        	        		}
        	        	}
        	        };
        
        	        frame2.add(panel);
        	        
        		}  
            }  
        });  
    }
}