/*package TPEntrega1;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import net.sourceforge.chart2d.Chart2DProperties;
import net.sourceforge.chart2d.Dataset;
import net.sourceforge.chart2d.GraphChart2DProperties;
import net.sourceforge.chart2d.GraphProperties;
import net.sourceforge.chart2d.LBChart2D;
import net.sourceforge.chart2d.LegendProperties;
import net.sourceforge.chart2d.MultiColorsProperties;
import net.sourceforge.chart2d.Object2DProperties;


public class Histograma extends JPanel {

    private BufferedImage image = getImage("OptionPane.warningIcon");
    private BufferedImage gray = getGray(image);

    public Histograma() {
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        panel.add(new JLabel(new ImageIcon(image)));
        panel.add(new JLabel(new ImageIcon(gray)));
        this.setLayout(new BorderLayout());
        this.add(panel, BorderLayout.WEST);
        this.add(createChart(gray, 20), BorderLayout.CENTER);
    }

    private BufferedImage getImage(String name) {
        Icon icon = UIManager.getIcon(name);
        int w = icon.getIconWidth();
        int h = icon.getIconHeight();
        this.setPreferredSize(new Dimension(w, h));
        BufferedImage i = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = (Graphics2D) i.getGraphics();
        g2d.setPaint(new GradientPaint(
            0, 0, Color.blue, w, h, Color.green, true));
        g2d.fillRect(0, 0, w, h);
        icon.paintIcon(null, g2d, 0, 0);
        g2d.dispose();
        return i;
    }

    private BufferedImage getGray(BufferedImage image) {
        BufferedImage g = new BufferedImage(
            image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        ColorConvertOp op = new ColorConvertOp(
            image.getColorModel().getColorSpace(),
            g.getColorModel().getColorSpace(), null);
        op.filter(image, g);
        return g;
    }

    private LBChart2D createChart(BufferedImage gray, int buckets) {
        // Chart2D configuration
        Object2DProperties object2DProps = new Object2DProperties();
        object2DProps.setObjectTitleText("Gray Histogram");
        Chart2DProperties chart2DProps = new Chart2DProperties();
        chart2DProps.setChartDataLabelsPrecision(-1);
        LegendProperties legendProps = new LegendProperties();
        String[] legendLabels = {"Gray"};
        legendProps.setLegendLabelsTexts(legendLabels);
        GraphChart2DProperties graphChart2DProps = new GraphChart2DProperties();
        graphChart2DProps.setLabelsAxisTitleText("Gray");
        graphChart2DProps.setNumbersAxisTitleText("Count");

        // Dataset
        String[] labelsAxisLabels = new String[buckets];
        for (int i = 0; i < labelsAxisLabels.length; i++) {
            labelsAxisLabels[i] = String.valueOf(i * 256 / buckets);
        }
        graphChart2DProps.setLabelsAxisLabelsTexts(labelsAxisLabels);
        int[] counts = new int[buckets];
        for (int r = 0; r < gray.getHeight(); r++) {
            for (int c = 0; c < gray.getWidth(); c++) {
                int v = (gray.getRGB(c, r) & 0xff) * buckets / 256;
                counts[v]++;
            }
        }
        Dataset dataset = new Dataset(1, counts.length, 1);
        for (int i = 0; i < counts.length; i++) {
            dataset.set(0, i, 0, counts[i]);
        }

        GraphProperties graphProps = new GraphProperties();
        MultiColorsProperties multiColorsProps = new MultiColorsProperties();
        LBChart2D chart2D = new LBChart2D();
        chart2D.setObject2DProperties(object2DProps);
        chart2D.setChart2DProperties(chart2DProps);
        chart2D.setLegendProperties(legendProps);
        chart2D.setGraphChart2DProperties(graphChart2DProps);
        chart2D.addGraphProperties(graphProps);
        chart2D.addDataset(dataset);
        chart2D.addMultiColorsProperties(multiColorsProps);

        //Optional validation:  Prints debug messages if invalid only.
        if (!chart2D.validate(false)) {
            chart2D.validate(true);
        }
        return chart2D;
    }

    private void display() {
        JFrame f = new JFrame("Histogram");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(this);
        f.pack();
        f.setSize(640, 480);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {

            //@Override
            public void run() {
                new Histogram().display();
            }
        });
    }
}*/