package com.mycompany.arduinojava;


import java.awt.Image;
import java.awt.Toolkit;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import com.panamahitek.*;
import java.awt.*;
import java.util.List;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.dial.*;
import org.jfree.data.general.DefaultValueDataset;
import org.jfree.data.general.ValueDataset;
import org.jfree.ui.GradientPaintTransformType;
import org.jfree.ui.StandardGradientPaintTransformer;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Hp
 */
public class Principal extends javax.swing.JFrame {

    /**
     * Creates new form Principal
     */
    PanamaHitek_Arduino Arduino = new PanamaHitek_Arduino();
    public int estadoDisplay = 1;
    public String textoConsola ="";
    Consola consola = new Consola();
    GasolinaDial gasosilaDial;
    VelocidadTemperaturaDial velocidadTemperaturaDial;
    RpmDial rpmDial;
    SerialPortEventListener listener = new SerialPortEventListener(){
        @Override
        //
        public void serialEvent(SerialPortEvent serialPortEvent) {
            try{
                if(Arduino.isMessageAvailable()){
                    textoConsola+=Arduino.printMessage()+"\n";
                    if(consola.isVisible()){
                        consola.setTxtArea(textoConsola);
                    }else{
                        consola.clearTxtArea();
                        textoConsola = "";
                    }
                }
            }catch(SerialPortException | ArduinoException ex){
                   JOptionPane.showConfirmDialog(null, ex, "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
            }
        }
    };
    /**********CLASES PARA GRAFICOS   ****************/
    static class GasolinaDial extends JPanel implements ChangeListener{
        JSlider slider;
        DefaultValueDataset dataset;
        Principal prin;
        
        public void stateChanged(ChangeEvent changeevent){
            prin.brraFuelTankLevel.setValue(slider.getValue());
            prin.txtFueTan.setText(String.valueOf(slider.getValue()));
            dataset.setValue(new Integer(slider.getValue()));
            
            
        } 
        
        public GasolinaDial(Principal p){
            super(new BorderLayout());
            prin = p;
            dataset = new DefaultValueDataset(0D);
            DialPlot dialplot = new DialPlot();
            dialplot.setView(0.20999999999999999D, 0.0D, 0.57999999999999996D, 0.29999999999999999D);
            dialplot.setDataset(dataset);
            ArcDialFrame arcdialframe = new ArcDialFrame(60D, 60D);
            arcdialframe.setInnerRadius(0.59999999999999998D);
            arcdialframe.setOuterRadius(0.90000000000000002D);
            arcdialframe.setForegroundPaint(Color.darkGray);
            arcdialframe.setStroke(new BasicStroke(3F));
            dialplot.setDialFrame(arcdialframe);
            GradientPaint gradientpaint = new GradientPaint(new Point(), new Color(255, 255, 255), new Point(), new Color(240, 240, 240));
            DialBackground dialbackground = new DialBackground(gradientpaint);
            dialbackground.setGradientPaintTransformer(new StandardGradientPaintTransformer(GradientPaintTransformType.VERTICAL));
            dialplot.addLayer(dialbackground);
            StandardDialScale standarddialscale = new StandardDialScale(0.0D, 100D, 115D, -50D, 10D, 4);
            standarddialscale.setTickRadius(0.88D);
            standarddialscale.setTickLabelOffset(0.070000000000000007D);
            standarddialscale.setMajorTickIncrement(25D);
            dialplot.addScale(0, standarddialscale);
            org.jfree.chart.plot.dial.DialPointer.Pin pin = new org.jfree.chart.plot.dial.DialPointer.Pin();
            pin.setRadius(0.81999999999999995D);
            dialplot.addLayer(pin);
            JFreeChart jfreechart = new JFreeChart(dialplot);
            jfreechart.setTitle("Fuel Tank Level");
            ChartPanel chartpanel = new ChartPanel(jfreechart);
            chartpanel.setPreferredSize(new Dimension(273, 200));
            slider = new JSlider(0, 100);
            slider.setMajorTickSpacing(10);
            slider.setPaintLabels(true);
            slider.addChangeListener(this);
            add(chartpanel);
            add(slider, "South");
        }
    }
    /******/
    static class RpmDial extends JPanel
		implements ChangeListener
	{

		JSlider slider;
		DefaultValueDataset dataset;
                Principal prin;
		public static JFreeChart createStandardDialChart(String s, String s1, ValueDataset valuedataset, double d, double d1, double d2, int i)
		{
			DialPlot dialplot = new DialPlot();
			dialplot.setDataset(valuedataset);
			dialplot.setDialFrame(new StandardDialFrame());
			dialplot.setBackground(new DialBackground());
			DialTextAnnotation dialtextannotation = new DialTextAnnotation(s1);
			dialtextannotation.setFont(new Font("Dialog", 1, 14));
			dialtextannotation.setRadius(0.69999999999999996D);
			dialplot.addLayer(dialtextannotation);
			DialValueIndicator dialvalueindicator = new DialValueIndicator(0);
			dialplot.addLayer(dialvalueindicator);
			StandardDialScale standarddialscale = new StandardDialScale(d, d1, -120D, -300D, 10D, 4);
			standarddialscale.setMajorTickIncrement(d2);
			standarddialscale.setMinorTickCount(i);
			standarddialscale.setTickRadius(0.88D);
			standarddialscale.setTickLabelOffset(0.14999999999999999D);
			standarddialscale.setTickLabelFont(new Font("Dialog", 0, 14));
			dialplot.addScale(0, standarddialscale);
			dialplot.addPointer(new org.jfree.chart.plot.dial.DialPointer.Pin());
			DialCap dialcap = new DialCap();
			dialplot.setCap(dialcap);
			return new JFreeChart(s, dialplot);
		}

		public void stateChanged(ChangeEvent changeevent)
		{
			dataset.setValue(new Integer(slider.getValue()));
                        prin.brraEngineSpeed.setValue(slider.getValue());
                        prin.txtEngSpeed.setText(String.valueOf(slider.getValue()));
                        
		}

		public RpmDial(Principal p)
		{       super(new BorderLayout());
			prin = p;
                        dataset = new DefaultValueDataset(0D);
			JFreeChart jfreechart = createStandardDialChart("", "RPM", dataset, 0D, 16383D, 1000D, 4);
			DialPlot dialplot = (DialPlot)jfreechart.getPlot();
			StandardDialRange standarddialrange = new StandardDialRange(12000D, 16383D, Color.red);
			standarddialrange.setInnerRadius(0.52000000000000002D);
			standarddialrange.setOuterRadius(0.55000000000000004D);
			dialplot.addLayer(standarddialrange);
			StandardDialRange standarddialrange1 = new StandardDialRange(6000D, 12000D, Color.orange);
			standarddialrange1.setInnerRadius(0.52000000000000002D);
			standarddialrange1.setOuterRadius(0.55000000000000004D);
			dialplot.addLayer(standarddialrange1);
			StandardDialRange standarddialrange2 = new StandardDialRange(0D, 6000D, Color.green);
			standarddialrange2.setInnerRadius(0.52000000000000002D);
			standarddialrange2.setOuterRadius(0.55000000000000004D);
			dialplot.addLayer(standarddialrange2);
			GradientPaint gradientpaint = new GradientPaint(new Point(), new Color(255, 255, 255), new Point(), new Color(170, 170, 220));
			DialBackground dialbackground = new DialBackground(gradientpaint);
			dialbackground.setGradientPaintTransformer(new StandardGradientPaintTransformer(GradientPaintTransformType.VERTICAL));
			dialplot.setBackground(dialbackground);
			dialplot.removePointer(0);
			org.jfree.chart.plot.dial.DialPointer.Pointer pointer = new org.jfree.chart.plot.dial.DialPointer.Pointer();
			pointer.setFillPaint(Color.yellow);
			dialplot.addPointer(pointer);
			ChartPanel chartpanel = new ChartPanel(jfreechart);
			chartpanel.setPreferredSize(new Dimension(600, 400));
			slider = new JSlider(0, 16383);
			slider.setMajorTickSpacing(1000);
			slider.setPaintLabels(false);
			slider.addChangeListener(this);
			add(chartpanel);
			add(slider, "South");
		}
	}
    
    /*****/
    static class VelocidadTemperaturaDial extends JPanel
                implements ChangeListener
        {

                DefaultValueDataset dataset1;
                DefaultValueDataset dataset2;
                JSlider slider1;
                JSlider slider2;
                Principal prin;
                public void stateChanged(ChangeEvent changeevent)
                {
                        dataset1.setValue(new Integer(slider1.getValue()));
                        dataset2.setValue(new Integer(slider2.getValue()));
                        prin.brraVehiculeSpeed.setValue(slider1.getValue());
                        prin.txtVehSpe.setText(String.valueOf(slider1.getValue()));
                        prin.brraCoolantTemperature.setValue(slider2.getValue());
                        prin.txtCooTem.setText(String.valueOf(slider2.getValue()));
                }

                public VelocidadTemperaturaDial(Principal p)
                {       super(new BorderLayout());
                        prin = p;
                        dataset1 = new DefaultValueDataset(0D);
                        dataset2 = new DefaultValueDataset(0D);
                        
                        DialPlot dialplot = new DialPlot();
                        
                        dialplot.setView(0.0D, 0.0D, 1.0D, 1.0D);
                        dialplot.setDataset(0, dataset1);
                        dialplot.setDataset(1, dataset2);
                        
                        StandardDialFrame standarddialframe = new StandardDialFrame();
                        standarddialframe.setBackgroundPaint(Color.lightGray);
                        standarddialframe.setForegroundPaint(Color.darkGray);
                        dialplot.setDialFrame(standarddialframe);
                        
                        GradientPaint gradientpaint = new GradientPaint(new Point(), new Color(255, 255, 255), new Point(), new Color(170, 170, 220));
                        DialBackground dialbackground = new DialBackground(gradientpaint);
                        
                        dialbackground.setGradientPaintTransformer(new StandardGradientPaintTransformer(GradientPaintTransformType.VERTICAL));
                        dialplot.setBackground(dialbackground);
                        
                        DialTextAnnotation dialtextannotation = new DialTextAnnotation("Km/h - °C");
                        dialtextannotation.setFont(new Font("Dialog", 1, 14));
                        dialtextannotation.setRadius(0.69999999999999996D);
                        dialplot.addLayer(dialtextannotation);
                        
                        DialValueIndicator dialvalueindicator = new DialValueIndicator(0);
                        dialvalueindicator.setFont(new Font("Dialog", 0, 10));
                        dialvalueindicator.setOutlinePaint(Color.darkGray);
                        dialvalueindicator.setRadius(0.59999999999999998D);
                        dialvalueindicator.setAngle(-103D);
                        dialplot.addLayer(dialvalueindicator);
                        
                        DialValueIndicator dialvalueindicator1 = new DialValueIndicator(1);
                        dialvalueindicator1.setFont(new Font("Dialog", 0, 10));
                        dialvalueindicator1.setOutlinePaint(Color.red);
                        dialvalueindicator1.setRadius(0.59999999999999998D);
                        dialvalueindicator1.setAngle(-77D);
                        dialplot.addLayer(dialvalueindicator1);
                        
                        StandardDialScale standarddialscale = new StandardDialScale(0D, 255D, -120D, -300D, 10D, 4);
                        standarddialscale.setTickRadius(0.88D);
                        standarddialscale.setTickLabelOffset(0.14999999999999999D);
                        standarddialscale.setTickLabelFont(new Font("Dialog", 0, 14));
                        dialplot.addScale(0, standarddialscale);
                        
                        StandardDialScale standarddialscale1 = new StandardDialScale(-40D, 100D, -120D, -300D, 10D, 4);
                        standarddialscale1.setTickRadius(0.5D);
                        standarddialscale1.setTickLabelOffset(0.14999999999999999D);
                        standarddialscale1.setTickLabelFont(new Font("Dialog", 0, 10));
                        standarddialscale1.setMajorTickPaint(Color.red);
                        standarddialscale1.setMinorTickPaint(Color.red);
                        dialplot.addScale(1, standarddialscale1);
                        
                        dialplot.mapDatasetToScale(1, 1);
                        
                        StandardDialRange standarddialrange = new StandardDialRange(90D, 100D, Color.blue);
                        standarddialrange.setScaleIndex(1);
                        standarddialrange.setInnerRadius(0.58999999999999997D);
                        standarddialrange.setOuterRadius(0.58999999999999997D);
                        dialplot.addLayer(standarddialrange);
                        
                        org.jfree.chart.plot.dial.DialPointer.Pin pin = new org.jfree.chart.plot.dial.DialPointer.Pin(1);
                        pin.setRadius(0.55000000000000004D);
                        dialplot.addPointer(pin);
                        
                        org.jfree.chart.plot.dial.DialPointer.Pointer pointer = new org.jfree.chart.plot.dial.DialPointer.Pointer(0);
                        dialplot.addPointer(pointer);
                        
                        DialCap dialcap = new DialCap();
                        dialcap.setRadius(0.10000000000000001D);
                        dialplot.setCap(dialcap);
                        
                        //ChartFactory.create
                        
                        JFreeChart jfreechart = new JFreeChart(dialplot);
                        jfreechart.setTitle("");
                        ChartPanel chartpanel = new ChartPanel(jfreechart);
                        chartpanel.setPreferredSize(new Dimension(400, 400));
                        JPanel jpanel = new JPanel(new GridLayout(2, 2));
                        jpanel.add(new JLabel("Velocidad:"));
                        jpanel.add(new JLabel("Temperature:"));
                        slider1 = new JSlider(0, 255);
                        slider1.setMajorTickSpacing(20);
                        slider1.setPaintTicks(true);
                        slider1.setPaintLabels(false);
                        slider1.addChangeListener(this);
                        jpanel.add(slider1);
                        jpanel.add(slider1);
                        slider2 = new JSlider(-40, 100);
                        slider2.setMajorTickSpacing(20);
                        slider2.setPaintTicks(true);
                        slider2.setPaintLabels(true);
                        slider2.addChangeListener(this);
                        jpanel.add(slider2);
                        add(chartpanel);
                        add(jpanel, "South");
                }
        }
    
    /********* FIN CLASES PARA GRAFICOS ***************/
    public Principal() {
        initComponents();
        gasosilaDial = new GasolinaDial(this);
        gasosilaDial.setSize(273,200);
        this.jPanelGasolina.add(gasosilaDial);
        this.jPanelGasolina.revalidate();
        this.jPanelGasolina.repaint();
        rpmDial = new RpmDial(this);
        rpmDial.setSize(300,300);
        this.jPanelRpm.add(rpmDial);
        this.jPanelRpm.revalidate();
        this.jPanelRpm.repaint();
        velocidadTemperaturaDial = new VelocidadTemperaturaDial(this);
        velocidadTemperaturaDial.setSize(330,350);
        this.jPanelVelocimetro.add(velocidadTemperaturaDial);
        this.jPanelVelocimetro.revalidate();
        this.jPanelVelocimetro.repaint();        
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dialogHelp = new javax.swing.JDialog();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel18 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel19 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cbmPuerto = new javax.swing.JComboBox<>();
        btnActualizar = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        cbnBaudrate = new javax.swing.JComboBox<>();
        bntConectar = new javax.swing.JButton();
        btnDesconectar = new javax.swing.JButton();
        txtEstado = new javax.swing.JLabel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        brraDistanceTraveled = new javax.swing.JSlider();
        brraVehiculeSpeed = new javax.swing.JSlider();
        brraCoolantTemperature = new javax.swing.JSlider();
        brraEngineRunTime = new javax.swing.JSlider();
        brraThrottlePosition = new javax.swing.JSlider();
        brraIAT = new javax.swing.JSlider();
        brraMAF = new javax.swing.JSlider();
        brraMAP = new javax.swing.JSlider();
        brraFuelTankLevel = new javax.swing.JSlider();
        brraEngineSpeed = new javax.swing.JSlider();
        txtDistTrav = new javax.swing.JLabel();
        txtCooTem = new javax.swing.JLabel();
        txtVehSpe = new javax.swing.JLabel();
        txtEngRun = new javax.swing.JLabel();
        txtThrPos = new javax.swing.JLabel();
        txtIAT = new javax.swing.JLabel();
        txtMAF = new javax.swing.JLabel();
        txtMAP = new javax.swing.JLabel();
        txtFueTan = new javax.swing.JLabel();
        txtEngSpeed = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jPanelRpm = new javax.swing.JPanel();
        jPanelVelocimetro = new javax.swing.JPanel();
        jPanelGasolina = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        statusBar = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        txtSBDisplay = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        mnuArchivo = new javax.swing.JMenu();
        mnuSalir = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        mnuDisplay = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        mnuAyuda = new javax.swing.JMenu();
        mnuAcercade = new javax.swing.JMenuItem();

        dialogHelp.setTitle("Ayuda");
        dialogHelp.setModal(true);
        dialogHelp.setResizable(false);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"31", "2", "Distancia recorrida desde que se borraron los fallas", "0", "65,535 ", "Km", "256A+B "},
                {"0D", "1", "Velocidad del vehículo ", "0", "255", "km/h", "A"},
                {"05", "1", " 	Temperatura del líquido de enfriamiento del motor ", "-40", "215", "°C", "A-40"},
                {"1F", "2", "Tiempo desde que se puso en marcha el motor ", "0", "65535", "SEC", "256A+B"},
                {"11", "1", "Posición del acelerador ", "0", "100", "%", "A/2.55"},
                {"0F", "1", "Temperatura del aire del colector de admisión ", "-40", "215", "°C", "A-40"},
                {"10", "2", "Velocidad del flujo del aire MAF", "0", "655.35", "gr/sec", "(256A+B)/100"},
                {"0B", "1", "Presión absoluta del colector de admisión ", "0", "255", "kPa", "A"},
                {"2F", "1", "Nivel de entrada del tanque de combustible ", "0", "100", "%", "A/2.55"},
                {"0C", "2", "RPM del motor ", "0", "16383.75", "rpm", "(256A+B)/4 "},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "PID (hex)", "Bytes de respuesta", "Descripción", "Valor Minimo", "Valor Máximo", "Unidad", "Formula"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, true, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jTable1);

        jLabel18.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel18.setText("PIDs estándar");

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(30);
        jTextArea1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTextArea1.setRows(5);
        jTextArea1.setText("La Sociedad de Ingenieros Automotrices (SAE) definió códigos PID para cada modo de operación bajo el estándar J1939. \nEl fabricante del vehículo no está obligado a implementar todos los modos de operación o códigos, y tiene la libertad de añadir \nsus propios.\n\nEl técnico automotriz usa un dispositivo, escáner, para comunicarse con el sistema OBD-II, lo cual  le permite obtener información \no borrar los códigos de falla. Algunos escáneres tienen interfaces “amigables al humano”, con descripciones y gráficos. \nOtros dan acceso básico al sistema usando números binarios o hexadecimales. Este último tipo de comunicación es el que se describe a \ncontinuación.\n\nLos bytes de respuesta se representan con las letras A, B, C, etc. A es el byte más significativo. \nLos bits de cada byte se representan del más significativo al menos con los números del 7 al 0: ");
        jTextArea1.setAutoscrolls(false);
        jScrollPane1.setViewportView(jTextArea1);

        jLabel19.setText("Fuente: https://es.wikipedia.org/wiki/OBD-II_PID");

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/Exit_24.png"))); // NOI18N
        jButton2.setText("Cerrar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout dialogHelpLayout = new javax.swing.GroupLayout(dialogHelp.getContentPane());
        dialogHelp.getContentPane().setLayout(dialogHelpLayout);
        dialogHelpLayout.setHorizontalGroup(
            dialogHelpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dialogHelpLayout.createSequentialGroup()
                .addGroup(dialogHelpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(dialogHelpLayout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addGroup(dialogHelpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 809, Short.MAX_VALUE)
                            .addGroup(dialogHelpLayout.createSequentialGroup()
                                .addGroup(dialogHelpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel18))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dialogHelpLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        dialogHelpLayout.setVerticalGroup(
            dialogHelpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dialogHelpLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 193, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addGap(16, 16, 16))
        );

        dialogHelp.getAccessibleContext().setAccessibleParent(this);

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Simulador OBD2 Instituto de Investigacion Ingenieria de Sistemas");
        setIconImage(getIconImage());
        setIconImages(getIconImages());
        setLocationByPlatform(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jLabel1.setText("Puerto de conección:");

        cbmPuerto.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Escaner los puertos libres..." }));
        cbmPuerto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbmPuertoActionPerformed(evt);
            }
        });

        btnActualizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/refresh.png"))); // NOI18N
        btnActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarActionPerformed(evt);
            }
        });

        jLabel2.setText("baudRate :");

        cbnBaudrate.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "2400", "4800", "9600", "19200", "38400", "115200" }));
        cbnBaudrate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbnBaudrateActionPerformed(evt);
            }
        });

        bntConectar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/connect.png"))); // NOI18N
        bntConectar.setText("Conectar");
        bntConectar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bntConectarActionPerformed(evt);
            }
        });

        btnDesconectar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/disconnect.png"))); // NOI18N
        btnDesconectar.setEnabled(false);
        btnDesconectar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDesconectarActionPerformed(evt);
            }
        });

        txtEstado.setForeground(new java.awt.Color(255, 0, 51));
        txtEstado.setText("Desconectado...");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbmPuerto, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbnBaudrate, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(bntConectar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnDesconectar, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnActualizar, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel1)
                                .addComponent(cbmPuerto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtEstado, javax.swing.GroupLayout.Alignment.LEADING))))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(cbnBaudrate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnDesconectar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bntConectar, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())))
        );

        jTabbedPane1.addTab("Dispositivo", jPanel1);

        jPanel2.setPreferredSize(new java.awt.Dimension(711, 500));

        jPanel3.setBackground(new java.awt.Color(204, 204, 204));
        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel3.setEnabled(false);

        brraDistanceTraveled.setBackground(new java.awt.Color(204, 255, 204));
        brraDistanceTraveled.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        brraDistanceTraveled.setMaximum(65000);
        brraDistanceTraveled.setPaintTicks(true);
        brraDistanceTraveled.setSnapToTicks(true);
        brraDistanceTraveled.setToolTipText("Distance Traveled");
        brraDistanceTraveled.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        brraDistanceTraveled.setEnabled(false);
        brraDistanceTraveled.setName(""); // NOI18N
        brraDistanceTraveled.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                brraDistanceTraveledMouseReleased(evt);
            }
        });

        brraVehiculeSpeed.setBackground(new java.awt.Color(204, 255, 204));
        brraVehiculeSpeed.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        brraVehiculeSpeed.setMaximum(255);
        brraVehiculeSpeed.setPaintTicks(true);
        brraVehiculeSpeed.setSnapToTicks(true);
        brraVehiculeSpeed.setToolTipText("Vehicule Speed");
        brraVehiculeSpeed.setValue(0);
        brraVehiculeSpeed.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        brraVehiculeSpeed.setEnabled(false);
        brraVehiculeSpeed.setName(""); // NOI18N
        brraVehiculeSpeed.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                brraVehiculeSpeedMouseReleased(evt);
            }
        });

        brraCoolantTemperature.setBackground(new java.awt.Color(204, 255, 204));
        brraCoolantTemperature.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        brraCoolantTemperature.setMaximum(215);
        brraCoolantTemperature.setMinimum(-40);
        brraCoolantTemperature.setPaintTicks(true);
        brraCoolantTemperature.setSnapToTicks(true);
        brraCoolantTemperature.setToolTipText("Coolant Temperature");
        brraCoolantTemperature.setValue(0);
        brraCoolantTemperature.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        brraCoolantTemperature.setEnabled(false);
        brraCoolantTemperature.setName(""); // NOI18N
        brraCoolantTemperature.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                brraCoolantTemperatureMouseReleased(evt);
            }
        });

        brraEngineRunTime.setBackground(new java.awt.Color(204, 255, 204));
        brraEngineRunTime.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        brraEngineRunTime.setMaximum(65535);
        brraEngineRunTime.setPaintTicks(true);
        brraEngineRunTime.setSnapToTicks(true);
        brraEngineRunTime.setToolTipText("Engine Run Time");
        brraEngineRunTime.setValue(0);
        brraEngineRunTime.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        brraEngineRunTime.setEnabled(false);
        brraEngineRunTime.setName(""); // NOI18N
        brraEngineRunTime.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                brraEngineRunTimeMouseReleased(evt);
            }
        });

        brraThrottlePosition.setBackground(new java.awt.Color(204, 255, 204));
        brraThrottlePosition.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        brraThrottlePosition.setPaintTicks(true);
        brraThrottlePosition.setSnapToTicks(true);
        brraThrottlePosition.setToolTipText("ThrottlePosition");
        brraThrottlePosition.setValue(0);
        brraThrottlePosition.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        brraThrottlePosition.setEnabled(false);
        brraThrottlePosition.setName(""); // NOI18N
        brraThrottlePosition.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                brraThrottlePositionMouseReleased(evt);
            }
        });

        brraIAT.setBackground(new java.awt.Color(204, 255, 204));
        brraIAT.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        brraIAT.setMaximum(215);
        brraIAT.setMinimum(-40);
        brraIAT.setPaintTicks(true);
        brraIAT.setSnapToTicks(true);
        brraIAT.setToolTipText("IAT");
        brraIAT.setValue(0);
        brraIAT.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        brraIAT.setEnabled(false);
        brraIAT.setName(""); // NOI18N
        brraIAT.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                brraIATMouseReleased(evt);
            }
        });

        brraMAF.setBackground(new java.awt.Color(204, 255, 204));
        brraMAF.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        brraMAF.setMaximum(65535);
        brraMAF.setPaintTicks(true);
        brraMAF.setSnapToTicks(true);
        brraMAF.setToolTipText("MAF");
        brraMAF.setValue(0);
        brraMAF.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        brraMAF.setEnabled(false);
        brraMAF.setName(""); // NOI18N
        brraMAF.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                brraMAFMouseReleased(evt);
            }
        });

        brraMAP.setBackground(new java.awt.Color(204, 255, 204));
        brraMAP.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        brraMAP.setMaximum(255);
        brraMAP.setPaintTicks(true);
        brraMAP.setSnapToTicks(true);
        brraMAP.setToolTipText("MAP");
        brraMAP.setValue(0);
        brraMAP.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        brraMAP.setEnabled(false);
        brraMAP.setName(""); // NOI18N
        brraMAP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                brraMAPMouseReleased(evt);
            }
        });

        brraFuelTankLevel.setBackground(new java.awt.Color(204, 255, 204));
        brraFuelTankLevel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        brraFuelTankLevel.setPaintTicks(true);
        brraFuelTankLevel.setSnapToTicks(true);
        brraFuelTankLevel.setToolTipText("Fuel Tank Level");
        brraFuelTankLevel.setValue(0);
        brraFuelTankLevel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        brraFuelTankLevel.setEnabled(false);
        brraFuelTankLevel.setName(""); // NOI18N
        brraFuelTankLevel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                brraFuelTankLevelMouseReleased(evt);
            }
        });

        brraEngineSpeed.setBackground(new java.awt.Color(204, 255, 204));
        brraEngineSpeed.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        brraEngineSpeed.setMaximum(16383);
        brraEngineSpeed.setPaintTicks(true);
        brraEngineSpeed.setSnapToTicks(true);
        brraEngineSpeed.setToolTipText("Engine Speed");
        brraEngineSpeed.setValue(0);
        brraEngineSpeed.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        brraEngineSpeed.setEnabled(false);
        brraEngineSpeed.setName(""); // NOI18N
        brraEngineSpeed.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                brraEngineSpeedMouseReleased(evt);
            }
        });

        txtDistTrav.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtDistTrav.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        txtDistTrav.setText("0");

        txtCooTem.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtCooTem.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        txtCooTem.setText("0");

        txtVehSpe.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtVehSpe.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        txtVehSpe.setText("0");

        txtEngRun.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtEngRun.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        txtEngRun.setText("0");

        txtThrPos.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtThrPos.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        txtThrPos.setText("0");

        txtIAT.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtIAT.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        txtIAT.setText("0");

        txtMAF.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtMAF.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        txtMAF.setText("0");

        txtMAP.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtMAP.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        txtMAP.setText("0");

        txtFueTan.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtFueTan.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        txtFueTan.setText("0");

        txtEngSpeed.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtEngSpeed.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        txtEngSpeed.setText("0");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(brraMAF, javax.swing.GroupLayout.PREFERRED_SIZE, 585, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtMAF, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(brraMAP, javax.swing.GroupLayout.PREFERRED_SIZE, 585, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtMAP, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(brraFuelTankLevel, javax.swing.GroupLayout.PREFERRED_SIZE, 585, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtFueTan, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(brraEngineSpeed, javax.swing.GroupLayout.PREFERRED_SIZE, 585, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtEngSpeed, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(brraDistanceTraveled, javax.swing.GroupLayout.PREFERRED_SIZE, 585, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtDistTrav, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(brraVehiculeSpeed, javax.swing.GroupLayout.PREFERRED_SIZE, 585, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtVehSpe, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(brraCoolantTemperature, javax.swing.GroupLayout.PREFERRED_SIZE, 585, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtCooTem, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(brraEngineRunTime, javax.swing.GroupLayout.PREFERRED_SIZE, 585, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtEngRun, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(brraIAT, javax.swing.GroupLayout.PREFERRED_SIZE, 585, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtIAT, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(brraThrottlePosition, javax.swing.GroupLayout.PREFERRED_SIZE, 585, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtThrPos, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel3Layout.createSequentialGroup()
                                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                            .addGroup(jPanel3Layout.createSequentialGroup()
                                                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                    .addComponent(brraDistanceTraveled, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                    .addComponent(txtDistTrav, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                    .addComponent(brraVehiculeSpeed, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                    .addComponent(txtVehSpe, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                                    .addComponent(brraCoolantTemperature, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                    .addComponent(txtCooTem, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(brraEngineRunTime, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                            .addGroup(jPanel3Layout.createSequentialGroup()
                                                                .addGap(0, 0, Short.MAX_VALUE)
                                                                .addComponent(txtEngRun, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(brraThrottlePosition, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                                        .addGap(0, 0, Short.MAX_VALUE)
                                                        .addComponent(txtThrPos, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(brraIAT, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(jPanel3Layout.createSequentialGroup()
                                                .addGap(0, 0, Short.MAX_VALUE)
                                                .addComponent(txtIAT, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(brraMAF, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addComponent(txtMAF, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(brraMAP, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(txtMAP, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(brraFuelTankLevel, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(txtFueTan, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(brraEngineSpeed, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtEngSpeed, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel4.setBackground(new java.awt.Color(204, 204, 204));
        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel8.setText("(0x31) Distance Traveled ");
        jLabel8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel8MouseClicked(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel9.setText("(0x0D) Vehicule Speed");

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel10.setText("(0x1F) Engine Run Time");

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel11.setText("(0x05) Coolant Temperature");

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel12.setText("(0x11) Throttle Position");

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel13.setText("(0x10) MAF");

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel14.setText("(0x0F) IAT");

        jLabel15.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel15.setText("(0x0B) MAP");

        jLabel16.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel16.setText("(0x2F) Fuel Tank Level");

        jLabel17.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel17.setText("(0x0C) Engine Speed");

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/help2.png"))); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel17)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(jLabel8))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jLabel9)
                .addGap(18, 18, 18)
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel10)
                .addGap(18, 18, 18)
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(13, 13, 13)
                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Parametros OBDII", jPanel2);

        jPanelRpm.setBackground(new java.awt.Color(204, 204, 204));
        jPanelRpm.setPreferredSize(new java.awt.Dimension(300, 300));

        javax.swing.GroupLayout jPanelRpmLayout = new javax.swing.GroupLayout(jPanelRpm);
        jPanelRpm.setLayout(jPanelRpmLayout);
        jPanelRpmLayout.setHorizontalGroup(
            jPanelRpmLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
        jPanelRpmLayout.setVerticalGroup(
            jPanelRpmLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        jPanelVelocimetro.setBackground(new java.awt.Color(204, 204, 204));
        jPanelVelocimetro.setPreferredSize(new java.awt.Dimension(300, 300));

        javax.swing.GroupLayout jPanelVelocimetroLayout = new javax.swing.GroupLayout(jPanelVelocimetro);
        jPanelVelocimetro.setLayout(jPanelVelocimetroLayout);
        jPanelVelocimetroLayout.setHorizontalGroup(
            jPanelVelocimetroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 334, Short.MAX_VALUE)
        );
        jPanelVelocimetroLayout.setVerticalGroup(
            jPanelVelocimetroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 350, Short.MAX_VALUE)
        );

        jPanelGasolina.setBackground(new java.awt.Color(204, 204, 204));

        javax.swing.GroupLayout jPanelGasolinaLayout = new javax.swing.GroupLayout(jPanelGasolina);
        jPanelGasolina.setLayout(jPanelGasolinaLayout);
        jPanelGasolinaLayout.setHorizontalGroup(
            jPanelGasolinaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 269, Short.MAX_VALUE)
        );
        jPanelGasolinaLayout.setVerticalGroup(
            jPanelGasolinaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 200, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanelRpm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelGasolina, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelVelocimetro, javax.swing.GroupLayout.DEFAULT_SIZE, 334, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanelVelocimetro, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jPanelRpm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jPanelGasolina, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(33, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Informacion gráfica", jPanel5);

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/sistemas.png"))); // NOI18N
        jLabel4.setText("jLabel3");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 51, 153));
        jLabel7.setText("> SimOBD2-IIS <");

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/obd2.png"))); // NOI18N
        jLabel3.setText("jLabel3");

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/auto.png"))); // NOI18N

        statusBar.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel5.setText("::: Barra de estado :::");

        txtSBDisplay.setText("Display Encendido");

        javax.swing.GroupLayout statusBarLayout = new javax.swing.GroupLayout(statusBar);
        statusBar.setLayout(statusBarLayout);
        statusBarLayout.setHorizontalGroup(
            statusBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusBarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtSBDisplay)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        statusBarLayout.setVerticalGroup(
            statusBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                .addComponent(txtSBDisplay))
        );

        mnuArchivo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/folder.png"))); // NOI18N
        mnuArchivo.setText("Archivo");

        mnuSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/Exit_24.png"))); // NOI18N
        mnuSalir.setText("Salir");
        mnuSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuSalirActionPerformed(evt);
            }
        });
        mnuArchivo.add(mnuSalir);
        mnuSalir.getAccessibleContext().setAccessibleName("mnuSalir");

        jMenuBar1.add(mnuArchivo);
        mnuArchivo.getAccessibleContext().setAccessibleName("mnuArchivo");
        mnuArchivo.getAccessibleContext().setAccessibleDescription("");

        jMenu1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/cog.png"))); // NOI18N
        jMenu1.setText("Configuración");

        mnuDisplay.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/display.png"))); // NOI18N
        mnuDisplay.setText("Display");
        mnuDisplay.setEnabled(false);
        mnuDisplay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuDisplayActionPerformed(evt);
            }
        });
        jMenu1.add(mnuDisplay);

        jMenuItem1.setText("Consola");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        mnuAyuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/help.png"))); // NOI18N
        mnuAyuda.setText("Ayuda");

        mnuAcercade.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/device.png"))); // NOI18N
        mnuAcercade.setText("Acerca de...");
        mnuAcercade.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuAcercadeActionPerformed(evt);
            }
        });
        mnuAyuda.add(mnuAcercade);

        jMenuBar1.add(mnuAyuda);
        mnuAyuda.getAccessibleContext().setAccessibleName("mnuAyuda");
        mnuAyuda.getAccessibleContext().setAccessibleDescription("");

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 484, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jTabbedPane2)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 449, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                .addComponent(statusBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents
    @Override
    public Image getIconImage() {
    Image retValue = Toolkit.getDefaultToolkit().
          getImage(ClassLoader.getSystemResource("iconos/icons8arduino.png"));
    return retValue;
    }

    
    private void mnuSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuSalirActionPerformed
        int respuesta = JOptionPane.showConfirmDialog(null, "Realmente desea salir de Ejmeplo de Conexion Arduino?", "Confirmar salida", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				System.out.println(respuesta);
				if(respuesta==0) {
					setVisible(false);
					dispose(); 
				}
    }//GEN-LAST:event_mnuSalirActionPerformed

    private void mnuAcercadeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuAcercadeActionPerformed
            // TODO add your handling code here:
            Acercade acerca = new Acercade();
            acerca.setVisible(true);
    }//GEN-LAST:event_mnuAcercadeActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        int respuesta = JOptionPane.showConfirmDialog(null, "Realmente desea salir de Ejmeplo de Conexion Arduino?", "Confirmar salida", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				System.out.println(respuesta);
				if(respuesta==0) {
					setVisible(false);
					dispose(); 
				}else{
                                    
                                }
    }//GEN-LAST:event_formWindowClosing

    private void cbmPuertoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbmPuertoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbmPuertoActionPerformed

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed
        btnActualizar.enable(false);
        cbmPuerto.removeAllItems();
        btnActualizar.setEnabled(false);
        cbmPuerto.addItem("...");
        // Buscamos todos los puertos libres
        List<String> listadoPuertos =  Arduino.getSerialPorts();
        cbmPuerto.removeAllItems();
        for(String str : listadoPuertos){
            cbmPuerto.addItem(str);
        }
       /*while (puertos.hasMoreElements()) { //para recorrer el numero de los puertos, y especificar con cual quiero trabajar 
           portId = (CommPortIdentifier) puertos.nextElement();
           cbmPuerto.addItem(portId.getName());
       } */   
       btnActualizar.setEnabled(true);
    }//GEN-LAST:event_btnActualizarActionPerformed

    private void cbnBaudrateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbnBaudrateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbnBaudrateActionPerformed

    private void bntConectarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bntConectarActionPerformed
        // TODO add your handling code here:
        if(cbmPuerto.getItemCount()>=1){
            try{
                String COMX = cbmPuerto.getSelectedItem().toString();
                int baudRate = Integer.parseInt(cbnBaudrate.getSelectedItem().toString());
                Arduino.arduinoRXTX(COMX, baudRate, listener);
                bntConectar.setEnabled(false);
                btnDesconectar.setEnabled(true);
                cbmPuerto.setEnabled(false);
                cbnBaudrate.setEnabled(false);
                btnActualizar.setEnabled(false);
                mnuDisplay.setEnabled(true);
                txtEstado.setText("Conectado:"+COMX+"-"+baudRate);
                txtEstado.setForeground(Color.BLUE);
                this.brraDistanceTraveled.setEnabled(true);
                this.brraCoolantTemperature.setEnabled(true);
                this.brraEngineRunTime.setEnabled(true);
                this.brraEngineRunTime.setEnabled(true);
                this.brraEngineSpeed.setEnabled(true);
                this.brraFuelTankLevel.setEnabled(true);
                this.brraIAT.setEnabled(true);
                this.brraMAF.setEnabled(true);
                this.brraMAP.setEnabled(true);
                this.brraThrottlePosition.setEnabled(true);
                this.brraVehiculeSpeed.setEnabled(true);
                   
            }catch(ArduinoException ex){
                JOptionPane.showConfirmDialog(null, ex, "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
            }
        }else{
            JOptionPane.showConfirmDialog(null, "Seleccione un puerto correcto", "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_bntConectarActionPerformed

    private void btnDesconectarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDesconectarActionPerformed
        try {
            // TODO add your handling code here:
            Arduino.killArduinoConnection();
            bntConectar.setEnabled(true);
            btnDesconectar.setEnabled(false);
            cbmPuerto.setEnabled(true);
            cbnBaudrate.setEnabled(true);
            btnActualizar.setEnabled(true); 
            mnuDisplay.setEnabled(false);
            txtEstado.setText("Desconectado...");
            txtEstado.setForeground(Color.red);
                this.brraDistanceTraveled.setEnabled(false);
                this.brraCoolantTemperature.setEnabled(false);
                this.brraEngineRunTime.setEnabled(false);
                this.brraEngineRunTime.setEnabled(false);
                this.brraEngineSpeed.setEnabled(false);
                this.brraFuelTankLevel.setEnabled(false);
                this.brraIAT.setEnabled(false);
                this.brraMAF.setEnabled(false);
                this.brraMAP.setEnabled(false);
                this.brraThrottlePosition.setEnabled(false);
                this.brraVehiculeSpeed.setEnabled(false);
        } catch (ArduinoException ex) {
            JOptionPane.showConfirmDialog(null, ex, "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
        }
        
    }//GEN-LAST:event_btnDesconectarActionPerformed

    private void mnuDisplayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuDisplayActionPerformed
        // TODO add your handling code here:
            //DisplayConfiguration display = new DisplayConfiguration();
            //display.setVisible(true);
            Display display = new Display(this, true, estadoDisplay, Arduino);
            display.setLocationRelativeTo(this);
            display.setVisible(true);
            this.estadoDisplay = display.getEstadoDisplay(); 
            this.setSBDisplay(this.estadoDisplay);
            
    }//GEN-LAST:event_mnuDisplayActionPerformed

    private void brraDistanceTraveledMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_brraDistanceTraveledMouseReleased
        // TODO add your handling code here:
        this.txtDistTrav.setText(String.valueOf(this.brraDistanceTraveled.getValue()));
         try { 
              Arduino.sendData("0x31"+String.valueOf(this.brraDistanceTraveled.getValue()));
            } catch (ArduinoException ex) {
                JOptionPane.showConfirmDialog(null, ex, "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
            } catch (SerialPortException ex) {
                JOptionPane.showConfirmDialog(null, ex, "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
            }
    }//GEN-LAST:event_brraDistanceTraveledMouseReleased

    private void brraVehiculeSpeedMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_brraVehiculeSpeedMouseReleased
        // TODO add your handling code here:
        this.txtVehSpe.setText(String.valueOf(this.brraVehiculeSpeed.getValue()));
        velocidadTemperaturaDial.slider1.setValue(this.brraVehiculeSpeed.getValue());
        try { 
              Arduino.sendData("0x0D"+String.valueOf(this.brraVehiculeSpeed.getValue()));
            } catch (ArduinoException ex) {
                JOptionPane.showConfirmDialog(null, ex, "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
            } catch (SerialPortException ex) {
                JOptionPane.showConfirmDialog(null, ex, "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
            }
    }//GEN-LAST:event_brraVehiculeSpeedMouseReleased

    private void brraCoolantTemperatureMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_brraCoolantTemperatureMouseReleased
        // TODO add your handling code here:
        this.txtCooTem.setText(String.valueOf(this.brraCoolantTemperature.getValue()));
        velocidadTemperaturaDial.slider2.setValue(this.brraCoolantTemperature.getValue());
        try { 
              Arduino.sendData("0x05"+String.valueOf(this.brraCoolantTemperature.getValue()));
            } catch (ArduinoException ex) {
                JOptionPane.showConfirmDialog(null, ex, "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
            } catch (SerialPortException ex) {
                JOptionPane.showConfirmDialog(null, ex, "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
            }
    }//GEN-LAST:event_brraCoolantTemperatureMouseReleased

    private void brraEngineRunTimeMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_brraEngineRunTimeMouseReleased
        // TODO add your handling code here:
        this.txtEngRun.setText(String.valueOf(this.brraEngineRunTime.getValue()));
        try { 
              Arduino.sendData("0x1F"+String.valueOf(this.brraEngineRunTime.getValue()));
            } catch (ArduinoException ex) {
                JOptionPane.showConfirmDialog(null, ex, "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
            } catch (SerialPortException ex) {
                JOptionPane.showConfirmDialog(null, ex, "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
            }
    }//GEN-LAST:event_brraEngineRunTimeMouseReleased

    private void brraThrottlePositionMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_brraThrottlePositionMouseReleased
        // TODO add your handling code here:
        this.txtThrPos.setText(String.valueOf(this.brraThrottlePosition.getValue()));
        try { 
              Arduino.sendData("0x11"+String.valueOf(this.brraThrottlePosition.getValue()));
            } catch (ArduinoException ex) {
                JOptionPane.showConfirmDialog(null, ex, "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
            } catch (SerialPortException ex) {
                JOptionPane.showConfirmDialog(null, ex, "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
            }
    }//GEN-LAST:event_brraThrottlePositionMouseReleased

    private void brraIATMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_brraIATMouseReleased
        // TODO add your handling code here:
        this.txtIAT.setText(String.valueOf(this.brraIAT.getValue()));
        try { 
              Arduino.sendData("0x0F"+String.valueOf(this.brraIAT.getValue()));
            } catch (ArduinoException ex) {
                JOptionPane.showConfirmDialog(null, ex, "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
            } catch (SerialPortException ex) {
                JOptionPane.showConfirmDialog(null, ex, "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
            }
    }//GEN-LAST:event_brraIATMouseReleased

    private void brraMAFMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_brraMAFMouseReleased
        // TODO add your handling code here:
        this.txtMAF.setText(String.valueOf(this.brraMAF.getValue()));
        try { 
              Arduino.sendData("0x10"+String.valueOf(this.brraMAF.getValue()));
            } catch (ArduinoException ex) {
                JOptionPane.showConfirmDialog(null, ex, "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
            } catch (SerialPortException ex) {
                JOptionPane.showConfirmDialog(null, ex, "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
            }
    }//GEN-LAST:event_brraMAFMouseReleased

    private void brraMAPMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_brraMAPMouseReleased
        // TODO add your handling code here:
        this.txtMAP.setText(String.valueOf(this.brraMAP.getValue()));
        try { 
              Arduino.sendData("0x0B"+String.valueOf(this.brraMAP.getValue()));
            } catch (ArduinoException ex) {
                JOptionPane.showConfirmDialog(null, ex, "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
            } catch (SerialPortException ex) {
                JOptionPane.showConfirmDialog(null, ex, "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
            }
    }//GEN-LAST:event_brraMAPMouseReleased

    private void brraFuelTankLevelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_brraFuelTankLevelMouseReleased
        // TODO add your handling code here:
        this.txtFueTan.setText(String.valueOf(this.brraFuelTankLevel.getValue()));
        gasosilaDial.slider.setValue(this.brraFuelTankLevel.getValue());
        try { 
              Arduino.sendData("0x2F"+String.valueOf(this.brraFuelTankLevel.getValue()));
              //Arduino.sendData(String.valueOf(this.brraFuelTankLevel.getValue()));
            } catch (ArduinoException ex) {
                JOptionPane.showConfirmDialog(null, ex, "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
            } catch (SerialPortException ex) {
                JOptionPane.showConfirmDialog(null, ex, "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
            }
    }//GEN-LAST:event_brraFuelTankLevelMouseReleased

    private void brraEngineSpeedMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_brraEngineSpeedMouseReleased
        // TODO add your handling code here:
        this.txtEngSpeed.setText(String.valueOf(this.brraEngineSpeed.getValue()));
        rpmDial.slider.setValue(this.brraEngineSpeed.getValue());
        try {   
              Arduino.sendData("0x0C"+String.valueOf(this.brraEngineSpeed.getValue()));
            } catch (ArduinoException ex) {
                JOptionPane.showConfirmDialog(null, ex, "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
            } catch (SerialPortException ex) {
                JOptionPane.showConfirmDialog(null, ex, "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
            }
    }//GEN-LAST:event_brraEngineSpeedMouseReleased

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        dialogHelp.setLocationRelativeTo(this);
        dialogHelp.setSize(841, 465);
        dialogHelp.setVisible(true);
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jLabel8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel8MouseClicked
        // TODO add your handling code here:
       
    }//GEN-LAST:event_jLabel8MouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        dialogHelp.setVisible(false);
	dialogHelp.dispose(); 
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
            consola.setLocationRelativeTo(this);
            consola.setVisible(true);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    /*Establece los valores del statusbar*/
    public void setSBDisplay(int estado){
        if(estado==1){
            txtSBDisplay.setText("Display encendido");
        }else{
            txtSBDisplay.setText("Display apagado");
        }
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Principal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bntConectar;
    private javax.swing.JSlider brraCoolantTemperature;
    private javax.swing.JSlider brraDistanceTraveled;
    private javax.swing.JSlider brraEngineRunTime;
    private javax.swing.JSlider brraEngineSpeed;
    private javax.swing.JSlider brraFuelTankLevel;
    private javax.swing.JSlider brraIAT;
    private javax.swing.JSlider brraMAF;
    private javax.swing.JSlider brraMAP;
    private javax.swing.JSlider brraThrottlePosition;
    private javax.swing.JSlider brraVehiculeSpeed;
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnDesconectar;
    private javax.swing.JComboBox<String> cbmPuerto;
    private javax.swing.JComboBox<String> cbnBaudrate;
    private javax.swing.JDialog dialogHelp;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanelGasolina;
    private javax.swing.JPanel jPanelRpm;
    private javax.swing.JPanel jPanelVelocimetro;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JMenuItem mnuAcercade;
    private javax.swing.JMenu mnuArchivo;
    private javax.swing.JMenu mnuAyuda;
    private javax.swing.JMenuItem mnuDisplay;
    private javax.swing.JMenuItem mnuSalir;
    private javax.swing.JPanel statusBar;
    private javax.swing.JLabel txtCooTem;
    private javax.swing.JLabel txtDistTrav;
    private javax.swing.JLabel txtEngRun;
    private javax.swing.JLabel txtEngSpeed;
    private javax.swing.JLabel txtEstado;
    private javax.swing.JLabel txtFueTan;
    private javax.swing.JLabel txtIAT;
    private javax.swing.JLabel txtMAF;
    private javax.swing.JLabel txtMAP;
    private javax.swing.JLabel txtSBDisplay;
    private javax.swing.JLabel txtThrPos;
    private javax.swing.JLabel txtVehSpe;
    // End of variables declaration//GEN-END:variables
}
