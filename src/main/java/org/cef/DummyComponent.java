//Created by montoyo for MCEF

package org.cef;

import java.awt.Component;
import java.awt.Point;

public class DummyComponent extends Component {

    @Override
    public Point getLocationOnScreen() {
        return new Point(0, 0);
    }

}
