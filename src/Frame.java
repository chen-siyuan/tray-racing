import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;

public class Frame extends JFrame implements Runnable {

    private final VisionPanel visionPanel;
    private final MapPanel mapPanel;
    private final ControlPanel controlPanel;
    private final Set<Edge> map;
    private final Observer observer;

    private boolean forward;        // W
    private boolean backward;       // S
    private boolean left;           // A
    private boolean right;          // D
    private boolean anticlockwise;  // LEFT_ARROW
    private boolean clockwise;      // RIGHT_ARROW

    // TODO: 9/18/20 explore non-linear options for this; try implementing drifting?

    public Frame() {

        visionPanel = new VisionPanel();
        mapPanel = new MapPanel();
        controlPanel = new ControlPanel();

        map = new HashSet<>();
        observer = new Observer(Main.OBSERVER_RAYS, Main.OBSERVER_SPAN);

    }

    public Set<Edge> getMap() {
        return map;
    }

    public Observer getObserver() {
        return observer;
    }

    public void initUI() {

        setTitle("ray-tracing");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(visionPanel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        add(mapPanel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        add(controlPanel, gbc);

        pack();
        setLocationRelativeTo(null);

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                switch(e.getExtendedKeyCode()) {
                    case KeyEvent.VK_W:
                        forward = true;
                        break;
                    case KeyEvent.VK_S:
                        backward = true;
                        break;
                    case KeyEvent.VK_A:
                        left = true;
                        break;
                    case KeyEvent.VK_D:
                        right = true;
                        break;
                    case KeyEvent.VK_LEFT:
                        anticlockwise = true;
                        break;
                    case KeyEvent.VK_RIGHT:
                        clockwise = true;
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                switch(e.getExtendedKeyCode()) {
                    case KeyEvent.VK_W:
                        forward = false;
                        break;
                    case KeyEvent.VK_S:
                        backward = false;
                        break;
                    case KeyEvent.VK_A:
                        left = false;
                        break;
                    case KeyEvent.VK_D:
                        right = false;
                        break;
                    case KeyEvent.VK_LEFT:
                        anticlockwise = false;
                        break;
                    case KeyEvent.VK_RIGHT:
                        clockwise = false;
                        break;
                }
            }
        });

    }

    @Override
    public void addNotify() {
        super.addNotify();
        new Thread(this).start();
    }

    @Override
    public void run() {

        while(!Thread.currentThread().isInterrupted()) {

            Point position = observer.getPosition();
            Angle direction = observer.getDirection();

            if(forward != backward) {
                double factor = Main.OBSERVER_MOVING_SPEED / Main.FRAME_RATE * (forward ? 1 : -1);
                observer.setPosition(new Point(position.getX() + direction.cos() * factor, position.getY() + direction.sin() * factor));
            }

            position = observer.getPosition();

            if(left != right) {
                double factor = Main.OBSERVER_MOVING_SPEED / Main.FRAME_RATE * (left ? 1 : -1);
                observer.setPosition(new Point(position.getX() + direction.sin() * factor, position.getY() + -direction.cos() * factor));
            }

            if(anticlockwise != clockwise) {
                Angle increment = new Angle(Main.OBSERVER_TURNING_SPEED / Main.FRAME_RATE * (anticlockwise ? -1 : 1));
                observer.setDirection(direction.add(increment));
            }

            // TODO: 9/17/20 edit map (add/remove edges)

            // TODO: 9/17/20 paint map

            visionPanel.setHeights(observer.detect(map));
            visionPanel.repaint();

            try {
                Thread.sleep((int)Math.round(1000. / Main.FRAME_RATE));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

}
