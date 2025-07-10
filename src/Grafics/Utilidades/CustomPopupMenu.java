package Grafics.Utilidades;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import javax.swing.JPopupMenu;
import javax.swing.JViewport;

/*
 * Esta clase se encarga de personalizar un popup menu,
 * para no tener errores con las ventanas en cualquier
 * entorno, puesto que al parecer al salirce de las ventanas
 * el programa presenta bugs graficos.
 */
public class CustomPopupMenu extends JPopupMenu {

    @Override
    public void show(Component invoker, int x, int y) {
        if (invoker != null && invoker.getParent() instanceof JViewport) {
            JViewport viewport = (JViewport) invoker.getParent();
            Dimension viewportSize = viewport.getSize();
            Point viewportPosition = viewport.getViewPosition();
            Dimension popupSize = this.getPreferredSize();

            // Ajustar la posición para que el menú emergente esté dentro del viewport
            if (x + popupSize.width > viewportPosition.x + viewportSize.width) {
                x = (viewportPosition.x + viewportSize.width) - popupSize.width;
            }
            if (y + popupSize.height > viewportPosition.y + viewportSize.height) {
                y = (viewportPosition.y + viewportSize.height) - popupSize.height;
            }

            // Ajustar la posición para no mostrar el popup fuera del JScrollPane
            if (x < viewportPosition.x) {
                x = viewportPosition.x;
            }
            if (y < viewportPosition.y) {
                y = viewportPosition.y;
            }
        }

        // Llamar al método original para mostrar el popup
        super.show(invoker, x, y);
    }

    
}