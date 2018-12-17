package GUI;

import javax.swing.*;
import java.awt.*;

public class JLabelCellRenderer extends JLabel implements ListCellRenderer<JLabel> {
    @Override
    public Component getListCellRendererComponent(JList<? extends JLabel> list, JLabel value, int index, boolean isSelected, boolean cellHasFocus) {
        setForeground(value.getForeground());
        setBackground(value.getBackground());
        setText(value.getText());
        return this;
    }
}
