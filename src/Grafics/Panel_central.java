package Grafics;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JScrollPane;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import Grafics.Utilidades.Key_adapter;
import Grafics.Utilidades.CustomPopupMenu;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Window;
import static Grafics.Panel_principal.config_boton;
public abstract class Panel_central extends JPanel{

    private JPanel panel_busqueda;
    private JPanel panel_adicionar;
    private JLabel label_bucar;
    protected JLabel label_panel;
    protected JTextField text_bucar;
    protected JScrollPane scroll;
    protected CustomPopupMenu pop_menu;
    protected JTable tabla;
    protected JButton boton_adicionar;
    protected Window window;
    protected JMenuItem item_eliminar;
    protected JMenuItem item_modificar;
    protected ImageIcon imagen_adicionar;


    /**
     * Constructor principal de la clase
     * Panel_central
     */
    public Panel_central(){

        super();    // Llama la super clase de JPanel

        this.window = SwingUtilities.getWindowAncestor(this);
        imagen_adicionar = new ImageIcon(getClass().getResource("/Grafics/Recursos/imagen_adicionar.png"));

        setLayout(new BorderLayout());  // Establece el Layout a utilizar
        scroll = new JScrollPane();     // Inicializa el scroll que se va a utilizar

        configuracion_panel_busqueda(); // Configura la busqueda
        config_pop_menu();              // Configura el pop_menu
        cargar_datos_tabla();           // Cara los datos a la tabla
        config_panel_adicionar();       // configura el adicionamiento de registros
        tabla.setComponentPopupMenu(pop_menu);  // Establece el pop_menu que se vera en la tabla

        scroll.setViewportView(tabla);  // Establece la tabla que se va a visualizar
        
        add(panel_busqueda, BorderLayout.NORTH);    // Agrega el componente al norte del panel
        add(scroll,BorderLayout.CENTER);            // Agrega el componente al centro del panel
        add(panel_adicionar,BorderLayout.SOUTH);    // Agrega el componente al sur del panel
        
    }

    
    /**
     * Este metodo se utiliza para preconfigurar
     * el panel de adicionamiento
     */
    private void config_panel_adicionar(){

        ToolTipManager.sharedInstance().setInitialDelay(100);    // 100ms en lugar de 750ms
        ToolTipManager.sharedInstance().setDismissDelay(10000);  // Cuánto tiempo permanece visible
        ToolTipManager.sharedInstance().setReshowDelay(50);      // Tiempo entre tooltips
        
        
        panel_adicionar = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // Instanciacion del nuevo panel

        boton_adicionar = new JButton();                    // Instanciacion del boton
        config_boton(boton_adicionar, imagen_adicionar);    // Configuracion por defectod el boton con una imagen
        boton_adicionar.setPreferredSize(new Dimension(40,40)); // Modfiicacion del tamaño preferido del boton
        boton_adicionar.addActionListener(_->{
            accion_adicionar();
        });
        
        panel_adicionar.add(boton_adicionar);   // Adicion del boton al panel para el boton

    }


    /**
     * Este metodo se encarga de configurar el text_field
     * para que realice una accion de buscar dependiendo la
     * configuracion del usuario, adicionalmente
     */
    private void configuracion_panel_busqueda(){

        panel_busqueda = new JPanel(null);  // Establece
        label_bucar = new JLabel("Buscar:");    // Etiqueta del label
        text_bucar = new JTextField();              // Inicializacion del textfield
        label_panel = new JLabel();
        label_panel.setFont(new Font("Arial", Font.ITALIC, 18));

        // Configuracion panel busqueda
        label_bucar.setBounds(  // Ubicacion del label
            10,         // Ubicacion en x
            2,          // Ubicacion en y
            50,     // Ancho del label
            20);    // Alto del label

        text_bucar.setBounds(
            label_bucar.getX() + label_bucar.getWidth() + 2 ,   // Posicion en x dependiendo del label
            2,          // Posicion en y
            300,    // Ancho del text field
            20);    // Alto del text field

        label_panel.setBounds(
            text_bucar.getX() + text_bucar.getWidth() + 100,
            2,
            300,
            20
        );

        panel_busqueda.add(label_bucar);    // Adicionamiento de label en el panel busqueda
        panel_busqueda.add(text_bucar);     // Adicionamiento de textfield en el panel busqueda
        panel_busqueda.add(label_panel);
        panel_busqueda.setPreferredSize(new Dimension(700,24)); // Modificacion del tamaño por defecto del panel

        panel_busqueda.setBackground(Color.LIGHT_GRAY);
        

        // Establece el escuchador para la busqueda
        text_bucar.addKeyListener(new Key_adapter() {
           
            @Override
            public void accion(){
                accion_text_bucar();
            }

            @Override
            public void accion2(){}

        });

    }
    
    /**
     * Este metodo se utiliza configurar un pop menu
     * para una tabla, y poder realizar modificaciones
     * y eliminaciones de registros en la base de datos
     */
    protected void config_pop_menu(){
        pop_menu = null;
        pop_menu = new CustomPopupMenu();   // Instancia de pop_menu

        item_modificar = new JMenuItem("Modificar");    // Instancia del item para modificaciones
        item_eliminar = new JMenuItem("Eliminar");      // Instancia del item para eliminaciones

        pop_menu.add(item_modificar);   // Adicionamiento del item para modificaciones
        pop_menu.add(item_eliminar);    // Adicionamiento del item para eliminaciones

        config_listener_pop_menu(); // Configura los excuchadores para los item del pop_menu
    }

    /**
     * Este metodo se tendra que encargar de cargar
     * los datos que se van a mostrar al usuario.
     */
    protected abstract void cargar_datos_tabla();

    /**
     * Este metodo se encarga de establecer la accion que se va
     * a  realizar cuando se detecte un evento en el text busqueda
     */
    protected abstract void accion_text_bucar();

    /**
     * Este metodo se encarga de configurar los listener
     * que va a tener el popup menu
     */
    protected abstract void config_listener_pop_menu();

    /**
     * Este metodo se utiliza para el boton adicionar
     * para que realice la accion que se prefiera al
     * precionar dicho boton
     */
    protected abstract void accion_adicionar();

    protected Window get_window(){
        return SwingUtilities.getWindowAncestor(this);
    }
    
}
