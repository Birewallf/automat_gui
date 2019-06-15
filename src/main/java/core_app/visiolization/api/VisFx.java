package core_app.visiolization.api;

import javafx.application.Platform;
import javafx.stage.Stage;
import core_app.visiolization.graph.VisGraph;
import core_app.visiolization.gui.GraphView;

import java.io.IOException;

public class VisFx{

    /**
     * Plots the given core_app.visiolization.graph to the mainStage.
     * @param graph the network core_app.visiolization.graph to be plotted.
     * @param mainStage the main Stage.
     */
    public static void graphNetwork(VisGraph graph , Stage mainStage){
        GraphView graphView = new GraphView(graph);
        Platform.runLater(() -> {
            try {
                graphView.start(mainStage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

}
