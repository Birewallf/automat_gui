package core_app.bwg;

import javafx.concurrent.Worker;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import core_app.visiolization.graph.VisGraph;

class Browser extends javafx.scene.layout.Region {

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
                try {
                    webEngine.executeScript(script);
                } catch (netscape.javascript.JSException ignore) {  }
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
