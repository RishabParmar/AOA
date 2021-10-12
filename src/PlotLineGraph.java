import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class PlotLineGraph extends ApplicationFrame {

    public PlotLineGraph(String title) {
        super(title);
    }

    public static void main( String[ ] args ) {
        PlotLineGraph chart = new PlotLineGraph("");
    }

    public void plot(DefaultCategoryDataset dataset, String chartTitle, String x, String y) {
        JFreeChart lineChart = ChartFactory.createLineChart(
                chartTitle,
                x,y,
                dataset,
                PlotOrientation.VERTICAL,
                true,true,false);

        ChartPanel chartPanel = new ChartPanel( lineChart );
        chartPanel.setPreferredSize( new java.awt.Dimension( 560 , 367 ) );
        setContentPane( chartPanel );
        pack( );
        RefineryUtilities.centerFrameOnScreen( this );
        setVisible( true );
    }
}
