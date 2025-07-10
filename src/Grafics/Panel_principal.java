package Grafics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class Panel_principal extends JPanel{

    private static final int ANCHO_BOTON = 40;
    private static final int ALTO_BOTON = 40;
    private static final int ANCHO_PANEL_MENU = 150;

    private boolean flag_menu = false;              // Esta bandera se usa para el menu
    private ImageIcon imagen_menu;          // La imagen para el boton del menu
    private ImageIcon imagen_inventario;    // la imagen para el boton inventario
    private ImageIcon imagen_compras;       // la imagen para el boton compras
    private ImageIcon imagen_ventas;        // La imagen para el boton ventas
    private JPanel panel_menu;              // El panel a utilizar para el menu
    private JPanel panel_central;           // El panel central, es donde va a ir la tabla
    private JButton boton_menu;             // Boton para desplegar el menu
    private JButton boton_inventario;       // Boton para desplegar el inventario
    private JButton boton_compras;          // Boton para desplegar las compras
    private JButton boton_ventas;           // Boton para desplegar las ventas
    private JLabel label_menu;              // Label para el boton menu
    private JLabel label_inventario;        // Label para el boton inventario
    private JLabel label_comrpas;           // Label para el boton comrpas
    private JLabel label_ventas;            // Label para el boton ventas

    public Panel_principal(){

        super(new BorderLayout());

        cargar_imagenes();
        iniciar_componentes();

    }

    private void iniciar_componentes(){

        /*
         * Establece la configuracion para el panel del menu
         * de opciones, donde se expande, y muestra las demas
         * opocones del menu.
         */
        panel_menu = new JPanel(null);
        panel_menu.setPreferredSize(new Dimension(ANCHO_BOTON,getPreferredSize().height));
        panel_menu.setBackground(Color.gray);
        
        config_boton_menu();
        config_boton_inventario();
        config_boton_compras();
        config_boton_ventas();
        
        label_menu = new JLabel("Menu");
        config_label(label_menu, boton_menu);

        label_inventario = new JLabel("Inventario");
        config_label(label_inventario, boton_inventario);

        label_comrpas = new JLabel("Compras");
        config_label(label_comrpas, boton_compras);

        label_ventas = new JLabel("Ventas");
        config_label(label_ventas, boton_ventas);

        panel_menu.add(boton_menu);
        panel_menu.add(boton_inventario);
        panel_menu.add(boton_compras);
        panel_menu.add(boton_ventas);

        panel_menu.add(label_menu);
        panel_menu.add(label_inventario);
        panel_menu.add(label_comrpas);
        panel_menu.add(label_ventas);


        /*
         * Establece la configuracion para el panel
         * de adicionar elementos
         */
        panel_central = new Panel_inventario();
        add(panel_menu,BorderLayout.WEST);
        add(panel_central,BorderLayout.CENTER);

    }

    /**
     * Este metodo se encarga de configurar
     * por completo el boton menu
     */
    private void config_boton_menu(){

        boton_menu = new JButton(); // Crea el boton

        config_boton(boton_menu, imagen_menu);  // Configura el boton de forma general

        boton_menu.addActionListener(_->{   // La accion que realizara el boton menu

            if(flag_menu){  // Contrae el menu si esta desplegado

                panel_menu.setPreferredSize(    // Modifica el tamaño del panel
                    (new Dimension(
                        ANCHO_BOTON,                             // Modifica el ancho
                        getPreferredSize().height)));   // Deja por defecto el alto

                panel_menu.revalidate();
                panel_menu.repaint();

                flag_menu =!flag_menu;  // Modifica la bandera para la proxima vez que se oprima el boton
                
            }else{  // Despliega el menu si esta contraido

                panel_menu.setPreferredSize(    // Modifica el tamaño del panel
                    (new Dimension(
                        ANCHO_PANEL_MENU,                        // Modifica el ancho del panel
                        getPreferredSize().height)));   // Deja por defecto el alto

                panel_menu.revalidate();
                panel_menu.repaint();

                flag_menu =!flag_menu;  // Modifica la bandera para la proxima vez que se oprima el boton
            }

        });
        
        boton_menu.setBounds(   // Establece la posicion absoluta del boton
            0,          // Establece la posicion en x
            0,          // Establece la posicion en y
            ANCHO_BOTON,     // Establece el ancho
            ALTO_BOTON);    // Establece el alto
    }

    /**
     * Este metodo se encarga de configurar
     * por completo el boton inventario
     * junto con su respectiva accion
     */
    private void config_boton_inventario(){

        boton_inventario = new JButton(); // Crea el boton

        config_boton(boton_inventario, imagen_inventario);  // Configura el boton de forma general

        boton_inventario.addActionListener(_->{   // La accion que realizara el boton menu
            
            remove(panel_central);
            panel_central = new Panel_inventario();
            add(panel_central,BorderLayout.CENTER);
            panel_central.revalidate();
            panel_central.repaint();
            
        });
        
        boton_inventario.setBounds(   // Establece la posicion absoluta del boton
            0,          // Establece la posicion en x
            boton_menu.getY() + boton_menu.getHeight() +5,          // Establece la posicion en y
            ANCHO_BOTON,     // Establece el ancho
            ALTO_BOTON);    // Establece el alto

    }

    /**
     * Este metodo se encarga de configurar
     * por completo el boton compras
     * junto con sus respectiva accion
     */
    private void config_boton_compras(){

        boton_compras = new JButton(); // Crea el boton

        config_boton(boton_compras, imagen_compras);  // Configura el boton de forma general

        boton_compras.addActionListener(_->{   // La accion que realizara el boton menu

            remove(panel_central);
            //panel_central = new Panel_inventario();
            panel_central = new JPanel();
            add(panel_central,BorderLayout.CENTER);
            panel_central.revalidate();
            panel_central.repaint();

        });
        
        boton_compras.setBounds(   // Establece la posicion absoluta del boton
            0,          // Establece la posicion en x
            boton_inventario.getY() + boton_inventario.getHeight() +5,          // Establece la posicion en y
            ANCHO_BOTON,     // Establece el ancho
            ALTO_BOTON);    // Establece el alto

    }

    /**
     * Este metodo se encarga de configurar
     * por completo el boton ventas
     * junto con su respectiva accion
     */
    private void config_boton_ventas(){

        boton_ventas = new JButton(); // Crea el boton

        config_boton(boton_ventas, imagen_ventas);  // Configura el boton de forma general

        boton_ventas.addActionListener(_->{   // La accion que realizara el boton menu

            remove(panel_central);
            //panel_central = new Panel_inventario();
            panel_central = new JPanel();
            add(panel_central,BorderLayout.CENTER);
            panel_central.revalidate();
            panel_central.repaint();

        });
        
        boton_ventas.setBounds(   // Establece la posicion absoluta del boton
            0,          // Establece la posicion en x
            boton_compras.getY() + boton_compras.getHeight() +5,          // Establece la posicion en y
            ANCHO_BOTON,     // Establece el ancho
            ALTO_BOTON);    // Establece el alto

    }


    /**
     * Este metodo se utiliza para la configuracion general
     * de un boton para la parte del menu
     * @param boton Debe ser el boton tipo JButton
     * @param imagen Debe ser la imagen del boton
     * tipo ImageIcon
     */
    public static void config_boton(JButton boton, ImageIcon imagen){

        boton.setBackground(Color.black);       // Cambia el color del fondo del boton
        boton.setBorderPainted(false);      // Hace que el borden no se vea
        boton.setContentAreaFilled(false);  // Hace que no haya un fondo en el boton
        boton.setFocusPainted(false);       // Hace que no se vea el centro en el boton

        boton.addMouseListener(new MouseAdapter() {
            
            @Override
            public void mouseEntered(MouseEvent e) {    // Detecta si el mouse esta por encima del boton
                boton.setContentAreaFilled(true);       // Hace que el area de color sea visible

                boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));    // Define el cursor a utilizar
                
            }

            @Override
            public void mouseExited(MouseEvent e) {     // Detecta si el mouse sale del boton
                boton.setContentAreaFilled(false);      // Hace que no se vea el area de color

                boton.setCursor(Cursor.getDefaultCursor()); // Establece el estado normal del cursor

            }   
        });

        Image img = imagen.getImage();  // Obtiene la imagen 

        Image newImg = img.getScaledInstance(   // Cambia la escala de la imagen a mostar en el boton
                                            ANCHO_BOTON - 5, // El ancho de la imagen
                                            ALTO_BOTON - 5, // El alto del la imagen
                                            java.awt.Image.SCALE_SMOOTH);   // El tipo de renderizado de la imagen

        boton.setIcon(new ImageIcon(newImg));   // Agrega la imagen al boton
        
    }
    
    
    /**
     * Este metodo se encarga de configurar un 
     * label, para asignarlo a un costado de un boton
     * en la parte del menu
     * @param label Este deberia ser el label a configurar
     * @param boton Este deberia ser el boton a asignar
     */
    private static void config_label(JLabel label, JButton boton){

        label.setBounds(
            boton.getX() + boton.getWidth() + 10,
            boton.getY() + (boton.getHeight() - 20)/2,
            100,
            20);

        label.setForeground(Color.black);

    }

    /**
     * Este metodo se encarga de cargar las imagenes 
     * correspondientes apra su uso en los botones
     */
    private void cargar_imagenes(){
        imagen_menu = new ImageIcon("src\\Grafics\\Recursos\\menu.png");
        imagen_inventario = new ImageIcon("src\\Grafics\\Recursos\\imagen_inventario.png");
        imagen_compras = new ImageIcon("src\\Grafics\\Recursos\\imagen_canasta.png");
        imagen_ventas = new ImageIcon("src\\Grafics\\Recursos\\imagen_ventas.png");
    }
}
