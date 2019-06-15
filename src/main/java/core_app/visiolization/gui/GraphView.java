package core_app.visiolization.gui;

import javafx.application.Application;
import javafx.concurrent.Worker;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import core_app.visiolization.graph.VisGraph;

import java.io.IOException;


public class GraphView extends Application {

    public GraphView(VisGraph graph){
        this.graph = graph;
    }

    private VisGraph graph;

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(this.getClass().getClassLoader().getResource("sample.fxml"));
        stage.setTitle("Automat");

        stage.setScene(new Scene(root));
        stage.show();
        // create the scene
        /*stage.setTitle("Network view");
        Scene scene = new Scene(new Browser(graph),  Color.web("#666970"));
        stage.setScene(scene);
        stage.show();*/
    }

    public static void main(String[] args){
        launch(args);
    }
}

class Browser extends Region {

    private final WebView browser = new WebView();
    private final WebEngine webEngine = browser.getEngine();
    private VisGraph graph;

    public Browser(VisGraph g) {
        this.graph = g;
        //apply the styles
        getStyleClass().add("browser");
        // load the web page
        webEngine.load((getClass().getClassLoader().getResource("baseGraph.html")).toString());
        //add the web view to the scene
        getChildren().add(browser);
        setGraph();

    }

    private void setGraph(){
        String script = "setTheData(" + graph.getNodesJson() +  "," + graph.getEdgesJson() + ")";
        webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue == Worker.State.SUCCEEDED)
                webEngine.executeScript(script);
        });
    }

    @Override
    protected void layoutChildren() {
        double w = getWidth();
        double h = getHeight();
        layoutInArea(browser,0,0,w,h,0, HPos.CENTER, VPos.CENTER);
    }

    @Override
    protected double computePrefWidth(double height) {
        return 750;
    }

    @Override
    protected double computePrefHeight(double width) {
        return 500;
    }
}
